package com.gameverse.app

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gameverse.app.data.config.defaultConfig
import com.gameverse.app.data.database.AppDatabase
import com.gameverse.app.data.database.DATABASE_NAME
import com.gameverse.app.data.database.getRoomDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

class AndroidPlatform : Platform {
    override val type: PlatformType
        get() = PlatformType.Android
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

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

@Composable
fun MainView() {
    val context = LocalContext.current
    val database = remember(context) {
        val builder = getDatabaseBuilder(context)
        getRoomDatabase(builder)
    }
    App(database)
}