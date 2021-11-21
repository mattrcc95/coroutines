package com.example.SuspendExperiment.parallel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.logging.Logger

/*
troubles with suspend .. kotlin version is too low probably
<kotlin.version>1.5.30</kotlin.version> <!-- current version managed by spring boot 2.4.5 is kotlin 1.4.32 -->
 */

object SuspendParallel {
    private val log = Logger.getLogger(SuspendParallel::class.java.simpleName)

    suspend fun <T> parallelLauncher(vararg fns: () -> T) =
        coroutineScope {
            fns.forEach { fn -> doJob(fn) }
            cancel()
        }

    private fun <T> CoroutineScope.doJob(fn: () -> T) =
        launch {
            log.info("${fn()} -> running on thread: ${Thread.currentThread().name}")
            fn()
        }
}