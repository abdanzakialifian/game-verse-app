package com.gameverse.app.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gameverse.app.common.shimmer
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme

@Composable
fun GamePlaceholders(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(10) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = GVShapes.medium,
                colors = CardDefaults.cardColors(contentColor = GVColor.secondary)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .shimmer()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .width(200.dp)
                            .height(24.dp)
                            .shimmer(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .width(150.dp)
                            .height(24.dp)
                            .shimmer(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .shimmer(12.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GamePlaceholdersPreview() {
    GVTheme {
        GamePlaceholders()
    }
}