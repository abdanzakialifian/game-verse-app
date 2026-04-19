package com.gameverse.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gameverse.app.domain.repository.GameVerseRepository
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(private val repository: GameVerseRepository) : ViewModel() {
    fun getGames() {
        viewModelScope.launch {
            repository.getGames()
        }
    }
}