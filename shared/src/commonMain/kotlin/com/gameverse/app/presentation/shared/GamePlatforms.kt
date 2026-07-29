package com.gameverse.app.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gameverse.app.common.Platform
import com.gameverse.app.theme.GVTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun GamePlatforms(
    platforms: List<Int>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val platformIcons = Platform.iconFromIds(platforms)
        platformIcons.forEach { iconDrawable ->
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(iconDrawable),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun GamePlatformsPreview() {
    GVTheme {
        GamePlatforms(
            platforms = (1..10).toList()
        )
    }
}