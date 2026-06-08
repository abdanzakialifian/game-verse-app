package com.gameverse.app.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope

@Composable
fun LaunchEffectOnce(
    key1: Any?,
    block: suspend CoroutineScope.() -> Unit
) {
    var hasLaunched by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1) {
        if (!hasLaunched) {
            hasLaunched = true
            block()
        }
    }
}