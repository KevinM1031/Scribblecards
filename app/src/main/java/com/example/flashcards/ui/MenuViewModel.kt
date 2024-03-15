package com.example.flashcards.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var isBundleOpen by mutableStateOf(false); private set
    var isCreateOptionsOpen by mutableStateOf(false); private set
    var isBundleCreatorOpen by mutableStateOf(false); private set

    var numSelectedDecks by mutableStateOf(0); private set
    var numSelectedBundles by mutableStateOf(0); private set

    val cardsList = remember { mutableStateOf(viewModel.uiState.value.cardsList) }



    fun openCreateOptions() {
        isCreateOptionsOpen = true
    }

    fun closeCreateOptions() {
        isCreateOptionsOpen = false
    }

    fun toggleCreateOptions() {
        isCreateOptionsOpen = !isCreateOptionsOpen
    }

    fun openBundleCreator() {
        isBundleCreatorOpen = true
        isCreateOptionsOpen = false
    }

    fun closeBundleCreator() {
        isBundleCreatorOpen = false
    }

    fun openDeck(deckIndex: Int) {
        _uiState.value = copyOfUiState(currentDeckIndex = deckIndex)
    }

    fun closeDeck() {
        _uiState.value = copyOfUiState(currentDeckIndex = null)
    }

    fun toggleSelection(cards: Cards, cardsIndex: Int, onChange: (Cards, Int) -> Unit) {
        cards.toggleSelection()
        onChange(cards, cardsIndex)
        if (cards.isSelected()) numSelectedDecks++ else numSelectedDecks--
    }

    fun deselectAll(cardsList: List<Cards>) : List<Cards> {
        for (i in cardsList.indices) {
            val cards = cardsList[i]
            if (cards.isBundle()) {
                for (deck in cards.decks!!) {
                    deck.deselect()
                }
            } else {
                cards.deselect()
            }
        }
        numSelectedDecks = 0
        return cardsList
    }

    fun openBundle(bundleIndex: Int) {
        _uiState.value = copyOfUiState(currentBundleIndex = bundleIndex)
        isBundleOpen = true
    }

    fun closeBundle() {
        _uiState.value = copyOfUiState(currentBundleIndex = null)
        isBundleOpen = false
    }

    private fun copyOfUiState(
        cardsList: List<Cards> = _uiState.value.cardsList,
        currentDeckIndex: Int? = _uiState.value.currentDeckIndex,
        currentBundleIndex: Int? = _uiState.value.currentBundleIndex,
        ) : MenuUiState {

        return MenuUiState(
            cardsList = cardsList,
            currentDeckIndex = currentDeckIndex,
            currentBundleIndex = currentBundleIndex,
        )
    }
}