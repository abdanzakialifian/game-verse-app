package com.gameverse.app

import androidx.compose.runtime.Composable
import com.gameverse.app.data.database.AppDatabase
import com.gameverse.app.di.AppModule
import com.gameverse.app.presentation.main.MainApp
import com.gameverse.app.theme.GVTheme
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import org.koin.plugin.module.dsl.koinConfiguration

@Composable
fun App(database: AppDatabase) {
    KoinApplication(
        configuration = koinConfiguration<AppModule> {
            modules(
                module {
                    single<AppDatabase> { database }
                }
            )
        },
    ) {
        GVTheme {
            MainApp()
        }
    }
}