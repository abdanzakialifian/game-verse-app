package com.gameverse.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.Warning
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_start_filled
import gameverse.shared.generated.resources.ic_start_outline
import org.jetbrains.compose.resources.painterResource

@Composable
fun GVRatingBadge(
    rating: Double,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    starSize: Dp = 18.dp,
    filledColor: Color = Warning,
    unfilledColor: Color = GVColor.outlineVariant,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(maxRating) { index ->
            val fillFraction = (rating - index).coerceIn(0.0, 1.0)
            PartialStar(
                fillFraction = fillFraction,
                size = starSize,
                filledColor = filledColor,
                unfilledColor = unfilledColor
            )
        }
    }
}

@Composable
private fun PartialStar(
    fillFraction: Double,
    size: Dp,
    filledColor: Color,
    unfilledColor: Color
) {
    Box(modifier = Modifier.size(size)) {
        Icon(
            painter = painterResource(Res.drawable.ic_start_outline),
            contentDescription = null,
            tint = unfilledColor,
            modifier = Modifier.size(size)
        )
        if (fillFraction > 0.0) {
            Box(
                modifier = Modifier
                    .size(size)
                    .drawWithContent {
                        clipRect(right = this.size.width * fillFraction.toFloat()) {
                            this@drawWithContent.drawContent()
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_start_filled),
                    contentDescription = null,
                    tint = filledColor,
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}

@Preview
@Composable
private fun GVRatingBadgePreview() {
    GVTheme {
        GVRatingBadge(4.6)
    }
}