package com.gameverse.app.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.img_error_illustration
import org.jetbrains.compose.resources.painterResource

@Composable
fun GeneralError(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(Res.drawable.img_error_illustration),
            contentDescription = null
        )

        Text(
            text = "Oops! Something went wrong.",
            style = GVTypography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "We couldn't load the games right now.",
            style = GVTypography.labelMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            shape = GVShapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = GVColor.outline),
            onClick = onButtonClicked
        ) {
            Text(
                text = "Try Again",
                style = GVTypography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GeneralErrorPreview() {
    GVTheme {
        GeneralError {}
    }
}