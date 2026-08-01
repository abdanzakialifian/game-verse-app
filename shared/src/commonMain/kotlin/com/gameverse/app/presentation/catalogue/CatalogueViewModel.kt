package com.gameverse.app.presentation.catalogue

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class CatalogueViewModel(
    repository: GVRepository
) : BaseViewModel<CatalogueReducer.State, CatalogueReducer.Event, CatalogueReducer.Effect, CatalogueReducer.Intent>(
    initialState = CatalogueReducer.State(),
    reducer = CatalogueReducer()
) {
    override fun sendIntent(intent: CatalogueReducer.Intent) {
        when (intent) {
            is CatalogueReducer.Intent.SelectCategory -> sendEffect(
                CatalogueReducer.Effect.NavigateToGameList(
                    intent.id
                )
            )
        }
    }

    val getGenresPaging = repository.getGenresPaging().cachedIn(viewModelScope)
}