package com.gameverse.app

import androidx.compose.ui.window.ComposeUIViewController
import com.gameverse.app.data.config.defaultConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import platform.Foundation.NSLog
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual object PlatformLogger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {

        if (throwable != null) {
            NSLog("ERROR: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } else {
            NSLog("ERROR: [$tag] $message")
        }
    }

    actual fun d(tag: String, message: String) {
        NSLog("DEBUG: [$tag] $message")
    }

    actual fun i(tag: String, message: String) {
        NSLog("INFO: [$tag] $message")
    }
}

actual class HttpClientFactory {
    actual fun create(): HttpClient = HttpClient(Darwin) {
        defaultConfig()
    }
}

fun MainViewController() = ComposeUIViewController { App() }