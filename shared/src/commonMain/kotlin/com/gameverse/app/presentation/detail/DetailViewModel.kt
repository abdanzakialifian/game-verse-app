package com.gameverse.app.presentation.detail

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DetailViewModel(
    private val repository: GVRepository,
    @InjectedParam val gameId: String,
) : BaseViewModel<DetailReducer.State, DetailReducer.Event, DetailReducer.Effect, DetailReducer.Intent>(
    initialState = DetailReducer.State(),
    reducer = DetailReducer()
) {
    init {
        getGameDetail(gameId)
    }

    override fun sendIntent(intent: DetailReducer.Intent) {}

    private fun getGameDetail(gameId: String) {
        viewModelScope.launch {
            sendEvent(DetailReducer.Event.GetGameDetailLoading(true))
            try {
                val detail = repository.getGameDetail(gameId)
                sendEvent(DetailReducer.Event.GetGameDetailData(detail))
            } catch (e: Exception) {
                sendEvent(DetailReducer.Event.GetGameDetailError(e))
            } finally {
                sendEvent(DetailReducer.Event.GetGameDetailLoading(false))
            }
        }
    }

    val gamesScreenshotsPaging =
        repository.getGamesScreenshotsPaging(gameId).cachedIn(viewModelScope)
}