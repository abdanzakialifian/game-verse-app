package com.gameverse.app.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable

@Composable
fun DetailVideoPlayer(
    url: String,
    onBack: () -> Unit,
) {
    val playerHost = remember { MediaPlayerHost(mediaUrl = url, isFullScreen = true) }

    VideoPlayerComposable(
        playerHost = playerHost,
        playerConfig = VideoPlayerConfig(
            enablePIPControl = false,
            isScreenLockEnabled = false,
            isScreenResizeEnabled = false,
            isFullScreenEnabled = false,
            backActionCallback = onBack
        )
    )
}