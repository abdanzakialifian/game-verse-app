package com.gameverse.app

import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import com.gameverse.app.data.config.defaultConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual object PlatformLogger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }

    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}

actual class HttpClientFactory {
    actual fun create(): HttpClient = HttpClient(OkHttp) {
        defaultConfig()
    }
}

@Composable
fun MainView() = App()