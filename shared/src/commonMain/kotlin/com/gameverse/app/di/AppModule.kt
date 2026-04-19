package com.gameverse.app.di

import com.gameverse.app.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan("com.gameverse.app")
class AppModule {
    @Singleton
    fun providesHttpClientFactory(): HttpClient = HttpClientFactory().create()
}