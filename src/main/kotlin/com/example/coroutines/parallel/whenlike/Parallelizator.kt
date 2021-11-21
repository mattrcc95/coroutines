package com.example.coroutines.parallel.whenlike

import com.example.coroutines.parallel.Selector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.logging.Logger
import kotlin.reflect.KFunction1

/*
When an exception is thrown it propagates up to
the parent, thus killing the whole concurrency tree
 when it is not thrown, it keeps going
 */
@Component
object Parallelizator {
    val log = Logger.getLogger(Parallelizator::class.java.simpleName)

    fun <T> CoroutineScope.doParallel(selector: Selector, fns: Array<out () -> T>) =
        when(selector){
            Selector.KILL_ON_ERROR -> doJobs(fns, ::killOnError)
            Selector.CONTINUE_ON_ERROR -> doJobs(fns, ::continueOnError)
        }

    private fun <T> CoroutineScope.doJobs(fns: Array<out () -> T>, executor: KFunction1<() -> T, Job>): Job =
        launch {
            val jobs = launch { fns.map { fn -> delay(500L); executor(fn) } }
            jobs.join()
            jobs.cancel()
        }
}

private fun <T> CoroutineScope.killOnError(fn: () -> T): Job =
    launch {
        try {
            Parallelizator.log.info("job ${fn()} run on thread ${Thread.currentThread().name}")
        } catch (ex: Exception) {
            Parallelizator.log.info("exception occurred on thread ${Thread.currentThread().name}")
            throw ex
        }
    }

private fun <T> CoroutineScope.continueOnError(fn: () -> T): Job =
    launch {
        try {
            Parallelizator.log.info("job ${fn()} run on thread ${Thread.currentThread().name}")
        } catch (ex: Exception) {
            Parallelizator.log.info("exception occurred on thread ${Thread.currentThread().name}")
            Parallelizator.log.info("... still running")
        }
    }