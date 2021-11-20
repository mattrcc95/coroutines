package com.example.SuspendExperiment.parallel

import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import java.util.logging.Logger

/*
troubles with suspend .. kotlin version is too low probably
<kotlin.version>1.5.30</kotlin.version> <!-- current version managed by spring boot 2.4.5 is kotlin 1.4.32 -->
 */


@Component
class MakeParallel {
    private val log = Logger.getLogger(MakeParallel::class.java.simpleName)

    suspend fun <T> parallelLauncher(vararg fns: () -> T) =
        coroutineScope {
            fns.forEach { fn -> doJob(fn) }
        }

    private fun <T> CoroutineScope.doJob(fn: () -> T) =
        launch(context = Dispatchers.IO) {
            log.info("${fn()} -> running on thread: ${Thread.currentThread().name}")
            fn()
        }

    suspend fun <T> twoLauncher(fn: () -> T, gn: () -> T) =
        coroutineScope {
            launch {
                log.info("${fn()} -> running on thread: ${Thread.currentThread().name}")
                fn()
            }
            launch {
                delay(1000L)
                log.info("${gn()} -> running on thread: ${Thread.currentThread().name}")
                gn()
            }
        }

}