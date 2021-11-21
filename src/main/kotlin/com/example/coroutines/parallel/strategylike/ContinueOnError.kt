package com.example.coroutines.parallel.strategylike

import com.example.coroutines.parallel.Selector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

class ContinueOnError : Parallelizable {
    private val log = Logger.getLogger(ContinueOnError::class.java.simpleName)
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun <T> handleJob(fn: () -> T): Job =
        launch(coroutineContext) {
            try {
                log.info("job ${fn()} run on thread ${Thread.currentThread().name}")
            } catch (ex: Exception) {
                log.info("exception occurred on thread ${Thread.currentThread().name}")
                log.info("... still running")
            }
        }

    override fun supports(p0: Selector): Boolean = (p0 == Selector.CONTINUE_ON_ERROR)
}