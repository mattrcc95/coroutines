package com.example.coroutines.service

interface HelloWorld {
    suspend fun doHello(): String
    suspend fun doWorld(): String
}