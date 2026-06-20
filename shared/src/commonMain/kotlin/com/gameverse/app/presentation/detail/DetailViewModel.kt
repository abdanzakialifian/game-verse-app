package com.gameverse.app.presentation.detail

import com.gameverse.app.mvi.BaseViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DetailViewModel(

) : BaseViewModel<DetailReducer.State, DetailReducer.Event, DetailReducer.Effect, DetailReducer.Intent>(
    initialState = DetailReducer.State(),
    reducer = DetailReducer()
) {
    override fun sendIntent(intent: DetailReducer.Intent) {}
}