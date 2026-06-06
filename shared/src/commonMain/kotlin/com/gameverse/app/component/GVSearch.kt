package com.gameverse.app.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource

@Composable
fun GVSearch(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = GVColor.secondary,
                shape = GVShapes.large
            )
            .padding(horizontal = 16.dp),
        value = value,
        onValueChange = onValueChange,
        textStyle = GVTypography.labelLarge.copy(color = GVColor.onSurfaceVariant),
        singleLine = true,
        cursorBrush = SolidColor(GVColor.onSurfaceVariant),
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(Res.drawable.ic_search),
                    tint = GVColor.onSurfaceVariant,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box {
                    if (value.isBlank()) {
                        Text(
                            text = hint,
                            color = GVColor.outline,
                            style = GVTypography.labelLarge
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}