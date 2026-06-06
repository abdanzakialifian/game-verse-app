package com.gameverse.app.presentation.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gameverse.app.theme.GVTheme

@Composable
fun CatalogueScreen() {
    CatalogueContent()
}

@Composable
private fun CatalogueContent() {

}

@Preview(showBackground = true)
@Composable
private fun CatalogueContentPreview() {
    GVTheme {
        CatalogueContent()
    }
}