package com.gameverse.app.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.img_empty_illustration
import org.jetbrains.compose.resources.painterResource

@Composable
fun GeneralEmpty(
    modifier: Modifier = Modifier,
    title: String = "No games found",
    description: String = "There are no games to display at the moment.",
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.img_empty_illustration),
            contentDescription = null
        )

        Text(
            text = title,
            style = GVTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = description,
            style = GVTypography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GeneralEmptyPreview() {
    GVTheme {
        GeneralEmpty()
    }
}