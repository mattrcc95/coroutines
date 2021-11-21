package com.example.coroutines.parallel.strategylike

import com.example.coroutines.parallel.Selector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.springframework.plugin.core.Plugin
import org.springframework.stereotype.Component

@Component
interface Parallelizable: Plugin<Selector>, CoroutineScope {
    fun <T> handleJob(fn: () -> T): Job
}