package com.gameverse.app.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gameverse.app.theme.GVTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    DetailContent(
        uiState = uiState,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun DetailContent(
    uiState: DetailReducer.State,
    onIntent: (DetailReducer.Intent) -> Unit,
) {

}

@Preview(showBackground = true)
@Composable
private fun DetailContentPreview() {
    GVTheme {
        DetailContent(
            uiState = DetailReducer.State(),
            onIntent = {}
        )
    }
}