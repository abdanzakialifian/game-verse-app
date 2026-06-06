package com.gameverse.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gameverse.app.theme.GVTheme

@Composable
fun ProfileScreen() {
    ProfileContent()
}

@Composable
private fun ProfileContent() {

}

@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview() {
    GVTheme {
        ProfileContent()
    }
}