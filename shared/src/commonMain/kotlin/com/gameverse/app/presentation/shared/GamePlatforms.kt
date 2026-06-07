package com.gameverse.app.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gameverse.app.common.Platform
import com.gameverse.app.domain.model.GamesModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun GamePlatforms(
    modifier: Modifier = Modifier,
    platforms: List<GamesModel.ParentPlatformsItem>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val platformIds = platforms.map { it.id }
        val platformIcons = Platform.iconFromIds(platformIds)
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