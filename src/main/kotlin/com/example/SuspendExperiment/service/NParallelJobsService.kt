package com.example.SuspendExperiment.service

import com.example.SuspendExperiment.parallel.SuspendParallel.parallelLauncher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Service

@Service
class NParallelJobsService {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doParallelJobs() =
        parallelLauncher(
            { job1() },
            { job2() },
            { job3() },
        )


private fun job1() = (1..5).map { "job1" }
private fun job2() = (1..5).map { "job2" }
private fun job3() = (1..5).map { "job3" }
}