package com.example.coroutines.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class JobWhichUsesAnotherJobService(
    private val parentChildrenService: ParentChildrenService
) : HelloWorld by parentChildrenService {

    private val log = Logger.getLogger(JobWhichUsesAnotherJobService::class.java.simpleName)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun doHelloWorld(): Flow<String> = channelFlow {
        val jobDependingOnEachOther = launch {
            val helloJob = async {
                (1..10).map {
                    delay(1000L)
                    "${doHello()} $it"
                }.also {
                    log.info("hello job done on thread: ${Thread.currentThread().name}")
                }
            }
            val worldJob = launch {
                helloJob.join()
                send("I do: ${doWorld()} and helloJob gave me: ${helloJob.await()}")
                log.info("world job done on thread: ${Thread.currentThread().name}")
            }
        }

        val intermediateJob = launch(context = Dispatchers.IO) {
            (1..5).forEach { _ ->
                delay(1000L)
                send(doIntermediate())
            }
            log.info("intermediate job done on thread: ${Thread.currentThread().name}")
        }
    }

    suspend fun doIntermediate(): String =
        coroutineScope {
            async {
                "-meanwhile hello...- "
            }
        }.await()
}