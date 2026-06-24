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
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.theme.GVTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun GamePlatforms(
    platforms: List<GamesModel.ParentPlatformsItem>,
    modifier: Modifier = Modifier,
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

@Preview
@Composable
private fun GamePlatformsPreview() {
    GVTheme {
        GamePlatforms(
            platforms = listOf(
                GamesModel.ParentPlatformsItem(
                    id = 1,
                    name = "Windows"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 2,
                    name = "PlayStation"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 3,
                    name = "Xbox"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 4,
                    name = "Apple"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 5,
                    name = "Apple Mac"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 6,
                    name = "Linux"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 7,
                    name = "Nintendo"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 8,
                    name = "Android"
                ),
                GamesModel.ParentPlatformsItem(
                    id = 9,
                    name = "Others"
                )
            )
        )
    }
}