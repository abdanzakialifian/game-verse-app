package com.gameverse.app.presentation.games.series

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class GameSeriesViewModel(
    repository: GVRepository,
    @InjectedParam gamePk: String = ""
) : BaseViewModel<GameSeriesReducer.State, GameSeriesReducer.Event, GameSeriesReducer.Effect, GameSeriesReducer.Intent>(
    initialState = GameSeriesReducer.State(),
    reducer = GameSeriesReducer()
) {
    override fun sendIntent(intent: GameSeriesReducer.Intent) {
        when (intent) {
            is GameSeriesReducer.Intent.OnExpanded -> sendEvent(
                GameSeriesReducer.Event.Expanded(intent.id)
            )

            is GameSeriesReducer.Intent.OnNavigateToDetailGame -> sendEffect(
                GameSeriesReducer.Effect.NavigateToDetailGame(intent.id)
            )

            GameSeriesReducer.Intent.OnNavigateBack -> sendEffect(
                GameSeriesReducer.Effect.NavigateBack
            )
        }
    }

    val getGamesSeriesPaging = repository.getSeriesPaging(gamePk).cachedIn(viewModelScope)
}