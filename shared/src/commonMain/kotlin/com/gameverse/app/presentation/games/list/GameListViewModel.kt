package com.gameverse.app.presentation.games.list

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class GameListViewModel(
    repository: GVRepository
) : BaseViewModel<GameListReducer.State, GameListReducer.Event, GameListReducer.Effect, GameListReducer.Intent>(
    initialState = GameListReducer.State(),
    reducer = GameListReducer()
) {
    override fun sendIntent(intent: GameListReducer.Intent) {
        when (intent) {
            is GameListReducer.Intent.OnSearchValueChanged -> sendEvent(
                GameListReducer.Event.SearchValueChanged(intent.value)
            )

            is GameListReducer.Intent.OnExpanded -> sendEvent(
                GameListReducer.Event.Expanded(intent.id)
            )

            is GameListReducer.Intent.OnNavigateToGameSeries -> sendEffect(
                GameListReducer.Effect.NavigateToGameSeries(intent.gamePk)
            )
        }
    }

    val getGamesPaging = state
        .map { it.searchValue }
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { value ->
            repository.getGamesPaging(value)
        }
        .cachedIn(viewModelScope)
}