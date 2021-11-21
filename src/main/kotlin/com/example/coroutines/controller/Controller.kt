package com.example.coroutines.controller

import com.example.coroutines.parallel.Selector
import com.example.coroutines.service.DelayService
import com.example.coroutines.service.JobWhichUsesAnotherJobService
import com.example.coroutines.service.ParallelizationService
import com.example.coroutines.service.ParentChildrenService
import kotlinx.coroutines.flow.Flow
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
class Controller(
    private val delayService: DelayService,
    private val parentChildrenService: ParentChildrenService,
    private val jobWhichUsesAnotherJobService: JobWhichUsesAnotherJobService,
    private val nParallelJobsService: ParallelizationService,
) {
    private val log = Logger.getLogger(Controller::class.java.simpleName)

    @GetMapping("/delay")
    suspend fun index(): Flow<String> = delayService.doHelloWorld()

    @GetMapping("/parentchildren")
    suspend fun index2(): Flow<String> = parentChildrenService.doHelloWorld()

    @GetMapping("/helpingJobs")
    suspend fun index3(): Flow<String> = jobWhichUsesAnotherJobService.doHelloWorld()

    @GetMapping("/killonfailure/{selector}")
    fun index4(@PathVariable @Validated selector: String): String {
        nParallelJobsService.doParallelJob(Selector.valueOf(selector).name)
        return "done"
    }

}