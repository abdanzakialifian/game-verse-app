package com.gameverse.app.mvi

/**
 * Reducer is responsible ONLY for updating ViewState.
 *
 * Important:
 * - ViewEvent is NOT a user action (e.g. button click).
 * - ViewEvent represents an internal state update trigger,
 *   usually produced after an Intent is processed.
 *
 * Flow:
 * Intent -> ViewModel logic -> ViewEvent -> Reducer -> New ViewState
 *
 * Reducer must be a pure function:
 * - No side effects
 * - No async work
 * - No navigation or effect emission
 */
interface Reducer<State : Reducer.ViewState, Event : Reducer.ViewEvent> {
    /**
     * Marker interface for representing UI state.
     * Each feature should define its own State that implements this.
     */
    interface ViewState

    /**
     * Marker interface for state update events.
     *
     * ViewEvent is used ONLY to update state,
     * not to represent user interactions.
     *
     * Example:
     * - DataLoaded
     * - LoadingStarted
     * - ErrorReceived
     */
    interface ViewEvent

    /**
     * Marker interface for one-time effects.
     * Example:
     * - Navigation
     * - Toast
     * - Snackbar
     */
    interface ViewEffect

    /**
     * Marker interface representing user intent.
     *
     * ViewIntent is emitted directly from the UI layer and represents
     * what the user wants to do, not how the state should change.
     *
     * Examples:
     * - ScreenOpened
     * - RetryClicked
     * - RefreshRequested
     */
    interface ViewIntent

    /**
     * Produces a new state from the current state and a ViewEvent.
     *
     * This function must be deterministic:
     * same state + same event = same new state.
     */
    fun reduce(state: State, event: Event): State
}