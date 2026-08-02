package com.gameverse.app

import androidx.compose.ui.window.ComposeUIViewController
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gameverse.app.data.config.defaultConfig
import com.gameverse.app.data.database.AppDatabase
import com.gameverse.app.data.database.DATABASE_NAME
import com.gameverse.app.data.database.getRoomDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSLog
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual object PlatformLogger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        val fullMessage = if (throwable != null) {
            "[ERROR] [$tag] $message | ${throwable.message} | cause: ${throwable.cause?.message}"
        } else {
            "[ERROR] [$tag] $message"
        }
        NSLog("%@", fullMessage)
    }

    actual fun d(tag: String, message: String) {
        NSLog("%@", "[DEBUG] [$tag] $message")
    }

    actual fun i(tag: String, message: String) {
        NSLog("%@", "[INFO] [$tag] $message")
    }
}

actual class HttpClientFactory {
    actual fun create(): HttpClient = HttpClient(Darwin) {
        defaultConfig()
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/$DATABASE_NAME"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

fun MainViewController() = ComposeUIViewController {
    val builder = getDatabaseBuilder()
    val database = getRoomDatabase(builder)
    App(database)
}