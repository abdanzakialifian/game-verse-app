package com.gameverse.app.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gameverse.app.theme.GVColor

@Composable
fun Modifier.shimmer(
    cornerRadius: Dp = 0.dp,
    shimmerColors: List<Color> = listOf(
        GVColor.outline.copy(alpha = 0.8f),
        GVColor.onSurfaceVariant.copy(alpha = 0.3f),
        GVColor.outline.copy(alpha = 0.8f)
    )
): Modifier {
    val transition = rememberInfiniteTransition()

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
    )

    return this.drawWithCache {
        val cornerPx = cornerRadius.toPx()

        val startX = (progress * 3f - 1f) * size.width
        val endX = startX + size.width

        val brush = Brush.horizontalGradient(
            colors = shimmerColors,
            startX = startX,
            endX = endX
        )

        onDrawBehind {
            drawRoundRect(
                brush = brush,
                cornerRadius = CornerRadius(cornerPx, cornerPx),
                size = size
            )
        }
    }
}