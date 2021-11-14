package com.example.SuspendExperiment.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.channelFlow
import org.springframework.stereotype.Service
import java.util.logging.Logger

/*
doing "hello job" and "intermediate job" concurrently
on two different threads (see logs)
THEN, when the "hello job" is done, the
world job is finally executed
 */
@Service
class ParentChildrenService(
    private val delayService: DelayService
) : HelloWorld by delayService {

    private val log = Logger.getLogger(ParentChildrenService::class.java.simpleName)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doHelloWorld() = channelFlow {
        val helloJob = launch {
            (1..10).forEach { _ ->
                delay(1000L)
                send(doHello())
            }
            log.info("hello job done on thread: ${Thread.currentThread().name}")
        }

        val intermediateJob = async {
            //doIntermediate()
            (1..5).forEach { _ ->
                delay(1000L)
                send(doIntermediate())
            }
            log.info("intermediate job done on thread: ${Thread.currentThread().name}")
        }.await()

        val worldJob = launch {
            helloJob.join()
            send(doWorld())
            log.info("world job done on thread: ${Thread.currentThread().name}")
        }
    }

    suspend fun doIntermediate(): String =
        coroutineScope {
            async {
                "meanwhile hello... "
            }
        }.await()

    override suspend fun doWorld(): String =
        coroutineScope {
            async {
                "world "
            }
        }.await()
}