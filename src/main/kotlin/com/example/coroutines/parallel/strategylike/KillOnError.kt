package com.example.coroutines.parallel.strategylike

import com.example.coroutines.parallel.Selector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

class KillOnError : Parallelizable {
    private val log = Logger.getLogger(KillOnError::class.java.simpleName)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun <T> handleJob(fn: () -> T): Job =
        launch(coroutineContext) {
            try {
                log.info("job ${fn()} run on thread ${Thread.currentThread().name}")
            } catch (ex: Exception) {
                log.info("exception occurred on thread ${Thread.currentThread().name}")
                throw ex
            }
        }
    override fun supports(p0: Selector): Boolean = (p0 == Selector.KILL_ON_ERROR)
}