package com.gameverse.app.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DispatcherModule::class, NetworkModule::class])
@ComponentScan("com.gameverse.app")
class AppModule