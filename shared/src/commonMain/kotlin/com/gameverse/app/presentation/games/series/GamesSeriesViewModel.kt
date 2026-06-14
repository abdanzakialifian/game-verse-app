package com.gameverse.app.presentation.games.series

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class GamesSeriesViewModel(
    repository: GVRepository,
    @InjectedParam gamePk: String = ""
) : BaseViewModel<GamesSeriesReducer.State, GamesSeriesReducer.Event, GamesSeriesReducer.Effect, GamesSeriesReducer.Intent>(
    initialState = GamesSeriesReducer.State(),
    reducer = GamesSeriesReducer()
) {
    override fun sendIntent(intent: GamesSeriesReducer.Intent) {
        when (intent) {
            is GamesSeriesReducer.Intent.OnExpanded -> sendEvent(
                GamesSeriesReducer.Event.Expanded(intent.id)
            )
        }
    }

    val getGamesSeriesPaging = repository.getGamesSeriesPaging(gamePk).cachedIn(viewModelScope)
}