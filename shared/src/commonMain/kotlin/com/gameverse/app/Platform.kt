package com.gameverse.app

import io.ktor.client.HttpClient

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect object PlatformLogger {
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
}

expect class HttpClientFactory() {
    fun create(): HttpClient
}