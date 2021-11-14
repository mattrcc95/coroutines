package com.example.SuspendExperiment.service

interface HelloWorld {
    suspend fun doHello(): String
    suspend fun doWorld(): String
}