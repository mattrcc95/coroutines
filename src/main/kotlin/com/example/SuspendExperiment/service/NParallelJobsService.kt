package com.example.SuspendExperiment.service

import com.example.SuspendExperiment.parallel.MakeParallel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Service

@Service
class NParallelJobsService(private val makeParallel: MakeParallel) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doParallelJobs() =
        makeParallel
            .parallelLauncher(
                { job1("merda") },
                { job2() },
                { job3() },
            )

    suspend fun twoParallelJobs() = makeParallel
        .twoLauncher(
            { job1("merda") },
            { job2() }
        )


    private fun job1(str: String) = (1..100).map { str }
    private fun job2() = (1..100).map {"job2"}
    private fun job3() = "job3 "
}