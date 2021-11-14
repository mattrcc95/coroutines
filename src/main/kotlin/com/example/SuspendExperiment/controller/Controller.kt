package com.example.SuspendExperiment.controller

import com.example.SuspendExperiment.service.DelayService
import com.example.SuspendExperiment.service.ParentChildrenService
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
class Controller(
    private val delayService: DelayService,
    private val parentChildrenService: ParentChildrenService
) {
    private val log = Logger.getLogger(Controller::class.java.simpleName)

    @GetMapping("/delay")
    suspend fun index(): Flow<String> = delayService.doHelloWorld()

    @GetMapping("/parentChildren")
    suspend fun index2(): Flow<String> = parentChildrenService.doHelloWorld()

}