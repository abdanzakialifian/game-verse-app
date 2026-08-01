package com.gameverse.app.presentation.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gameverse.app.common.formatDate
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography

@Composable
fun GameListItem(
    game: GamesModel,
    expandedIds: Set<Int>,
    modifier: Modifier = Modifier,
    onShowMoreClicked: ((gamePk: String) -> Unit)? = null,
    onExpand: (id: Int) -> Unit,
    onItemClicked: (id: Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onItemClicked(game.id)
                }
            ),
        shape = GVShapes.medium,
        colors = CardDefaults.cardColors(contentColor = GVColor.secondary)
    ) {
        Column {
            AsyncImage(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                model = game.backgroundImage,
                placeholder = ColorPainter(GVColor.outline),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                filterQuality = FilterQuality.Medium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Platforms(
                modifier = Modifier.padding(horizontal = 16.dp),
                platforms = game.parentPlatforms
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = game.name,
                style = GVTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )

            GameMetadata(
                modifier = Modifier.padding(horizontal = 16.dp),
                released = game.released,
                genres = game.genres,
                isExpanded = game.id in expandedIds,
                onButtonClicked = onShowMoreClicked?.let { callback ->
                    {
                        callback(game.id.toString())
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onExpand(game.id)
                        }
                    ),
                text = if (game.id in expandedIds) "View less" else "View more",
                style = GVTypography.labelMedium.copy(textDecoration = TextDecoration.Underline),
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun GameMetadata(
    released: String,
    genres: List<String>,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onButtonClicked: (() -> Unit)? = null,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Release date:",
                    style = GVTypography.labelSmall
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = released.formatDate(),
                    style = GVTypography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Genres:",
                    style = GVTypography.labelSmall
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = genres.joinToString(", "),
                    style = GVTypography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (onButtonClicked != null) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = GVShapes.small,
                    colors = ButtonDefaults.buttonColors(containerColor = GVColor.outline),
                    onClick = onButtonClicked
                ) {
                    Text(
                        text = "Show more like this",
                        style = GVTypography.labelSmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameListItemPreview() {
    GVTheme {
        GameListItem(
            game = GamesModel(
                id = 1,
                name = "Grand Theft Auto V",
                backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                released = "2013-09-17",
                genres = listOf("Action", "RPG", "Shooter"),
                parentPlatforms = (1..10).toList(),
            ),
            expandedIds = setOf(1),
            onShowMoreClicked = {},
            onExpand = {},
            onItemClicked = {}
        )
    }
}
