package com.example.flashcards.ui

import androidx.lifecycle.ViewModel
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.flashcards.data.MenuUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MenuViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    fun selectDeck(deck: Deck) {
        _uiState.value = MenuUiState(currentDeck = deck)
    }

    fun selectBundle(bundle: Bundle) {
        _uiState.value = MenuUiState(currentBundle = bundle)
    }
}