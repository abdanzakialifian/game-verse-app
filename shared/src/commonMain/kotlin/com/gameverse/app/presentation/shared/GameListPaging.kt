package com.gameverse.app.presentation.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTypography

@Composable
fun GameListPaging(
    expandedIds: Set<Int>,
    gamesPaging: LazyPagingItems<GamesModel>,
    modifier: Modifier = Modifier,
    onExpand: (id: Int) -> Unit,
    onShowMoreClicked: ((gamePk: String) -> Unit)? = null,
) {
    when (gamesPaging.loadState.refresh) {
        is LoadState.Loading -> GamePlaceholders()
        is LoadState.Error -> {}
        else -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(gamesPaging.itemCount, key = gamesPaging.itemKey { it.id }) { index ->
                    val result = gamesPaging[index] ?: return@items

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = GVShapes.medium,
                        colors = CardDefaults.cardColors(contentColor = GVColor.secondary)
                    ) {
                        Column {
                            AsyncImage(
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                model = result.backgroundImage,
                                placeholder = ColorPainter(GVColor.outline),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                filterQuality = FilterQuality.Medium,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            GamePlatforms(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                platforms = result.parentPlatforms
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = result.name,
                                style = GVTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            )

                            GameInformation(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                released = result.released,
                                genres = result.genres,
                                isExpanded = result.id in expandedIds,
                                onButtonClicked = onShowMoreClicked?.let { callback ->
                                    {
                                        callback(result.id.toString())
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
                                            onExpand(result.id)
                                        }
                                    ),
                                text = if (result.id in expandedIds) "View less" else "View more",
                                style = GVTypography.labelMedium.copy(textDecoration = TextDecoration.Underline),
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}