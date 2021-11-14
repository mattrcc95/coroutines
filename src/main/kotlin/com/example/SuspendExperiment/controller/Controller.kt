package com.example.SuspendExperiment.controller

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger


@RestController
class Controller {
    val logger = Logger.getLogger(Controller::class.java.simpleName)

    @GetMapping("/try")
    suspend fun index() = doHello() + doWorld()

    suspend fun doHello() =
        coroutineScope { async { hello() } }.await()

    suspend fun doWorld() =
        coroutineScope { async { world() } }.await()


    fun hello() = "hello"
    fun world() = "world"
}