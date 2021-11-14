package com.example.SuspendExperiment.controller

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

/*
endpoint which gives "hello world",
doing the "hello job" and "world job"
on two different threads, working concurrently
 */
@RestController
class DelayController {
    private val log = Logger.getLogger(DelayController::class.java.simpleName)

    @GetMapping("/try")
    suspend fun index(): Flow<String> = doHelloWorldJob()


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doHelloWorldJob(): Flow<String> = channelFlow {
        launch {
            send(doSuspendWorldJob())
            log.info("world job done on thread: ${Thread.currentThread().name}")
        }
        send(doSuspendHelloJob())
        log.info("hello job done on thread: ${Thread.currentThread().name}")
    }

    suspend fun doSuspendWorldJob(): String =
        coroutineScope {
            async {
                delay(2000L);
                "world "
            }
        }.await()

    suspend fun doSuspendHelloJob(): String =
        coroutineScope {
            async {
                "hello "
            }
        }.await()

}