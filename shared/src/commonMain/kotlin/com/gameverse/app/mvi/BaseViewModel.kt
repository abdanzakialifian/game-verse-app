package com.gameverse.app.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * BaseViewModel implements the core MVI flow using Intent–Event–State separation.
 *
 * Responsibilities:
 * - Hold UI state
 * - Process Intents (in feature ViewModel)
 * - Convert Intent results into ViewEvent
 * - Delegate state updates to Reducer
 * - Emit one-time ViewEffect
 *
 * High-level flow:
 *
 * UI -> Intent
 * Intent -> ViewModel logic
 * ViewModel -> ViewEvent
 * ViewEvent -> Reducer -> New State
 * UI observes State
 */
abstract class BaseViewModel<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect, Intent : Reducer.ViewIntent>(
    initialState: State,
    private val reducer: Reducer<State, Event>,
) : ViewModel() {
    /**
     * Holds the current UI state.
     *
     * State can ONLY be updated through ViewEvent
     * processed by the Reducer.
     */
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    /**
     * Channel for one-time effects.
     *
     * Effects are triggered by Intent handling,
     * not by the Reducer.
     */
    private val _effects: Channel<Effect> = Channel(capacity = Channel.CONFLATED)

    /**
     * Flow exposed to the UI for collecting effects.
     */
    val effects get() = _effects.receiveAsFlow()

    /**
     * Emits a one-time effect to the UI.
     *
     * Example:
     * - Navigate to another screen
     * - Show toast/snackbar
     */
    protected fun sendEffect(effect: Effect) {
        _effects.trySend(effect)
    }

    /**
     * Applies a ViewEvent to update the current state.
     *
     * This should be called ONLY after an Intent
     * has been processed.
     */
    protected fun sendEvent(event: Event) {
        _state.value = reducer.reduce(state.value, event)
    }

    /**
     * Entry point for user actions from the UI.
     *
     * UI must communicate with ViewModel ONLY through ViewIntent.
     * Implementations should:
     * - handle business logic
     * - perform async work
     * - emit ViewEvent to update state
     * - emit ViewEffect for one-time actions
     */
    abstract fun sendIntent(intent: Intent)
}