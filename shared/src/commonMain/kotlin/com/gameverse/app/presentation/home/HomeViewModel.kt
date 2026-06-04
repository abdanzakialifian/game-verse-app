package com.gameverse.app.presentation.home

import androidx.lifecycle.viewModelScope
import com.gameverse.app.domain.repository.GameVerseRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val repository: GameVerseRepository
) : BaseViewModel<HomeReducer.State, HomeReducer.Event, HomeReducer.Effect, HomeReducer.Intent>(
    initialState = HomeReducer.State(),
    reducer = HomeReducer()
) {
    override fun sendIntent(intent: HomeReducer.Intent) {
        when (intent) {
            is HomeReducer.Intent.OnSearchValueChanged -> sendEvent(
                HomeReducer.Event.SearchValueChanged(intent.value)
            )
        }
    }

    fun getGames() {
        viewModelScope.launch {
            repository.getGames()
        }
    }
}