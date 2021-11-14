package com.example.SuspendExperiment.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.springframework.stereotype.Service
import java.util.logging.Logger

/*
gives "hello world",
doing the "hello job" and "world job"
on two different threads (see logs), working concurrently
 */
@Service
class DelayService : HelloWorld {

    private val log = Logger.getLogger(DelayService::class.java.simpleName)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doHelloWorld(): Flow<String> = channelFlow {
        launch {
            delay(2000L)
            send(doWorld())
            log.info("world job done on thread: ${Thread.currentThread().name}")
        }
        send(doHello())
        log.info("hello job done on thread: ${Thread.currentThread().name}")
    }

    override suspend fun doWorld(): String =
        coroutineScope {
            async {
                "world "
            }
        }.await()

    override suspend fun doHello(): String =
        coroutineScope {
            async {
                "hello "
            }
        }.await()

}