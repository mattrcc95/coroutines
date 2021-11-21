package com.example.coroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SuspendExperimentApplication

fun main(args: Array<String>) {
	runApplication<SuspendExperimentApplication>(*args)
}
