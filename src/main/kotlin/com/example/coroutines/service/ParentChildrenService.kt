package com.example.coroutines.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.springframework.stereotype.Service
import java.util.logging.Logger

/*
doing "hello job" and "intermediate job" concurrently
on two different threads (see logs)
THEN, when the "hello job" is done, the
world job is finally executed.
Note that "Hello job and "intermediate job" run on different threads
thanks to different coroutines context.
The job depending on each other use default context, while the
intermediate one uses Dispatcher.IO context
 */
@Service
class ParentChildrenService(
    private val delayService: DelayService
) : HelloWorld by delayService {

    private val log = Logger.getLogger(ParentChildrenService::class.java.simpleName)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doHelloWorld(): Flow<String> = channelFlow {
        val jobDependingOnEachOther = launch {
            val helloJob = launch {
                (1..10).forEach { _ ->
                    delay(1000L)
                    send(doHello())
                }
                log.info("hello job done on thread: ${Thread.currentThread().name}")
            }

            val worldJob = launch {
                helloJob.join()
                send(doWorld())
                log.info("world job done on thread: ${Thread.currentThread().name}")
            }
        }

        val intermediateJob = launch(context = Dispatchers.IO) {
            (1..5).forEach { _ ->
                delay(1000L)
                send(doIntermediate())
            }
            log.info("intermediate job done on thread: ${Thread.currentThread().name}")
        }
    }

    suspend fun doIntermediate(): String =
        coroutineScope {
            async {
                "-meanwhile hello...- "
            }
        }.await()
}