package com.example.coroutines.service

import com.example.coroutines.parallel.Selector
import com.example.coroutines.parallel.whenlike.Parallelizator
import com.example.coroutines.parallel.whenlike.Parallelizator.doParallel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class ParallelizationService(private val parallelizator: Parallelizator) {
    private val log = Logger.getLogger(ParallelizationService::class.java.simpleName)
    private val coroutineContext = Dispatchers.Default
    fun doParallelJob(selector: String) =
        CoroutineScope(context = Dispatchers.Default).launch {
            val parallelJob = doParallel(
                Selector.valueOf(selector),
                arrayOf(
                    { job1() },
                    { job7() },
                    { job3() },
                    { job4() },
                    { job5() },
                    { job6() },
                    { job2() },
                    { job8() },
                    { job9() },
                ),
            )
            parallelJob.join()
            parallelJob.cancel()
        }.also {
            it.invokeOnCompletion { log.info("parallel job terminated") }
        }

    private fun job1() = (1..5).map { "job1" }
    private fun job2(): Nothing = throw Exception("this job threw an exception")
    private fun job3() = (1..5).map { "job3" }
    private fun job4() = (1..5).map { "job4" }
    private fun job5() = (1..5).map { "job5" }
    private fun job6(): Nothing = throw Exception("this job threw an exception")
    private fun job7() = (1..5).map { "job7" }
    private fun job8() = (1..5).map { "job8" }
    private fun job9() = (1..5).map { "job9" }
}