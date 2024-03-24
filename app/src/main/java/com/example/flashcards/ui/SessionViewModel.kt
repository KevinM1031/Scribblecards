package com.example.flashcards.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Card
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.flashcards.data.MenuUiState
import com.example.flashcards.data.SessionUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SessionViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        reset()
    }

    fun setup(param: String) {
        setDeck(param)
    }

    fun softReset() {
    }

    fun reset() {
        softReset()
    }

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }

    fun toggleHint() {
        _uiState.update { currentState ->
            currentState.copy(
                isHintShown = !currentState.isHintShown
            )
        }
    }

    fun toggleExample() {
        _uiState.update { currentState ->
            currentState.copy(
                isExampleShown = !currentState.isExampleShown
            )
        }
    }

    fun setDeck(param: String) {
        val splitter = param.indexOf('&')
        if (splitter == -1) {
            _uiState.update { currentState ->
                currentState.copy(
                    deck = DataSource.decks[param.toInt()]
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    deck = DataSource.bundles[param.substring(0..<splitter).toInt()].decks[param.substring(splitter+1).toInt()]
                )
            }
        }
    }

    fun getCurrentCard(): Card {
        return _uiState.value.deck.cards[_uiState.value.currentCardIndex]
    }

}