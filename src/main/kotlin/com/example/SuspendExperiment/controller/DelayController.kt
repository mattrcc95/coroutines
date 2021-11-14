package com.example.SuspendExperiment.controller

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.channelFlow

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
class DelayController {
    private val log = Logger.getLogger(DelayController::class.java.simpleName)

    @GetMapping("/try")
    suspend fun index() = doHelloWorldJob()


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doHelloWorldJob() = channelFlow {
        coroutineScope {
            launch {
                send(doSuspendWorldJob())
                log.info("world job done on thread: ${Thread.currentThread().name}")
            }
            launch {
                send(doSuspendHelloJob())
                log.info("hello job done on thread: ${Thread.currentThread().name}")
            }
        }
    }

    suspend fun doSuspendWorldJob() =
        coroutineScope { async { delay(2000L); "world " } }.await()

    suspend fun doSuspendHelloJob() =
        coroutineScope { async { "hello " } }.await()

}