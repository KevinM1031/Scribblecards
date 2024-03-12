package com.example.flashcards.ui

import androidx.lifecycle.ViewModel
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Card
import com.example.flashcards.data.Cards
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.flashcards.data.MenuUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MenuViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState(DataSource.cards))
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        reset()
    }

    fun reset() {
        deselect()
    }

    fun selectDeck(deck: Deck) {
        _uiState.value = copyOfUiState(currentDeck = deck)
    }

    fun selectBundle(bundle: Bundle) {
        _uiState.value = copyOfUiState(currentBundle = bundle)
    }

    fun deselect() {
        _uiState.value = copyOfUiState()
    }

    fun createBundle(name: String = "Bundle", decks: List<Deck> = listOf()) {
        val cards = _uiState.value.cards.toMutableList()
        val bundle = Bundle(name, decks)
        cards.add(bundle)
        _uiState.value = copyOfUiState(cards = cards)
    }

    fun createDeck(name: String, cards: List<Card>) {

    }

    fun copyOfUiState(
        cards: List<Cards> = _uiState.value.cards,
        currentDeck: Deck? = _uiState.value.currentDeck,
        currentBundle: Bundle? = _uiState.value.currentBundle,
        ) : MenuUiState {

        return MenuUiState(
            cards = cards,
            currentDeck = currentDeck,
            currentBundle = currentBundle,
        )
    }
}