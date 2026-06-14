package com.gameverse.app.presentation.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gameverse.app.common.formatDate
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTypography

@Composable
fun GameInformation(
    modifier: Modifier = Modifier,
    released: String,
    genres: List<GamesModel.GenresItem>,
    isExpanded: Boolean,
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
                    text = genres.joinToString(", ") { it.name },
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