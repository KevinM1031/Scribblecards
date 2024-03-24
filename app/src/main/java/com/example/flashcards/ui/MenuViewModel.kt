package com.example.flashcards.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Card
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.flashcards.data.MenuUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                decks = DataSource.decks,
                bundles = DataSource.bundles,
            )
        }

        reset()
    }

    fun softReset() {
        deselectAllCards()
        closeCardSelector()
    }

    fun reset() {
        softReset()

        closeBundle()
        closeDeck()
        closeBundleCreator()
        closeCreateOptions()
        closeBundleCreatorDialog()
    }

    fun saveCards() {
        DataSource.decks = _uiState.value.decks
        DataSource.bundles = _uiState.value.bundles
    }

    fun openCreateOptions() {
        _uiState.update { currentState ->
            currentState.copy(
                isCreateOptionsOpen = true
            )
        }
    }

    fun closeCreateOptions() {
        _uiState.update { currentState ->
            currentState.copy(
                isCreateOptionsOpen = false
            )
        }
    }

    fun toggleCreateOptions() {
        _uiState.update { currentState ->
            currentState.copy(
                isCreateOptionsOpen = !_uiState.value.isCreateOptionsOpen
            )
        }
    }

    fun toggleSessionOptions() {
        _uiState.update { currentState ->
            currentState.copy(
                isSessionOptionsOpen = !_uiState.value.isSessionOptionsOpen
            )
        }
    }

    fun openBundleCreator() {
        _uiState.update { currentState ->
            currentState.copy(
                isBundleCreatorOpen = true,
                isCreateOptionsOpen = false,
            )
        }
    }

    fun closeBundleCreator() {
        _uiState.update { currentState ->
            currentState.copy(
                isBundleCreatorOpen = false,
                isBundleCreatorDialogOpen = false,
            )
        }
        deselectAllDecks()
    }

    fun openBundleCreatorDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isBundleCreatorDialogOpen = true,
                userInput = null,
            )
        }
    }

    fun closeBundleCreatorDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isBundleCreatorDialogOpen = false,
                userInput = null,
            )
        }
    }

    fun openCardSelector() {
        _uiState.update { currentState ->
            currentState.copy(
                isCardSelectorOpen = true,
            )
        }
    }

    fun closeCardSelector() {
        _uiState.update { currentState ->
            currentState.copy(
                isCardSelectorOpen = false,
            )
        }
        deselectAllCards()
    }

    fun setUserInput(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                userInput = input,
            )
        }
    }

    fun openTip() {
        _uiState.update { currentState ->
            currentState.copy(
                isTipOpen = true,
            )
        }
    }

    fun closeTip() {
        _uiState.update { currentState ->
            currentState.copy(
                isTipOpen = false,
            )
        }
    }

    fun setTipText(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                tipText = text,
            )
        }
    }

    fun openBundle(bundleIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentBundleIndex = bundleIndex,
            )
        }
    }

    fun closeBundle() {
        _uiState.update { currentState ->
            currentState.copy(
                currentBundleIndex = null,
            )
        }
    }

    fun isBundleOpen(): Boolean {
        return _uiState.value.currentBundleIndex != null
    }

    /**
     * Uses selected decks to create a bundle.
     */
    fun createBundle(name: String) {
        val newDecks = mutableListOf<Deck>()

        // searching for selected decks
        val tempDecks = mutableListOf<Deck>()
        for (deck in _uiState.value.decks) {
            if (deck.isSelected()) {
                newDecks.add(deck)
            } else {
                tempDecks.add(deck)
            }
        }

        // searching for selected decks from bundles
        val tempBundles = mutableListOf<Bundle>()
        for (bundle in _uiState.value.bundles) {

            val remainingDecks = mutableListOf<Deck>()
            for (deck in bundle.decks) {
                if (deck.isSelected()) {
                    newDecks.add(deck)
                } else {
                    remainingDecks.add(deck)
                }
            }

            if (remainingDecks.isNotEmpty()) {
                tempBundles.add(Bundle(name = bundle.name, decks = remainingDecks))
            }
        }

        tempBundles.add(Bundle(name, newDecks))

        _uiState.update { currentState ->
            currentState.copy(
                decks = tempDecks,
                bundles = tempBundles,
            )
        }
    }

    fun getCurrentDeck(): Deck {
        return if (isBundleOpen()) {
            _uiState.value.bundles[_uiState.value.currentBundleIndex!!].decks[_uiState.value.currentDeckIndex!!]
        } else {
            _uiState.value.decks[_uiState.value.currentDeckIndex!!]
        }

        /* TODO remove
        return if (isBundleOpen()) {
            _uiState.value.bundles[_uiState.value.currentBundleIndex!!].decks[_uiState.value.currentDeckIndex!!]
        } else {
            _uiState.value.decks[1]
        }

         */
    }

    fun openDeck(index: Int) {
        _uiState.update { currentState ->
            if (isBundleOpen()) {
                currentState.copy(
                    currentDeckIndex = index,
                )
            } else {
                currentState.copy(
                    currentDeckIndex = index,
                )
            }
        }
    }

    fun closeDeck() {
        _uiState.update { currentState ->
            currentState.copy(
                currentDeckIndex = null,
            )
        }
    }

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }

    fun toggleDeckSelection(index: Int) {
        val bundleIndex: Int? = _uiState.value.currentBundleIndex
        val num = _uiState.value.numSelectedDecks

        if (bundleIndex == null) {
            _uiState.value.decks[index].toggleSelection()
            _uiState.update { currentState ->
                currentState.copy(
                    numSelectedDecks = if (currentState.decks[index].isSelected())
                        num+1 else num-1
                )
            }

        } else {
            _uiState.value.bundles[bundleIndex].decks[index].toggleSelection()
            _uiState.update { currentState ->
                currentState.copy(
                    numSelectedDecks = if (currentState.bundles[bundleIndex].decks[index].isSelected())
                        num+1 else num-1
                )
            }
        }
    }

    fun toggleCardSelection(deckIndex: Int, index: Int) {
        val bundleIndex: Int? = _uiState.value.currentBundleIndex
        val num = _uiState.value.numSelectedCards

        if (bundleIndex == null) {
            _uiState.value.decks[deckIndex].cards[index].toggleSelection()
            _uiState.update { currentState ->
                currentState.copy(
                    numSelectedCards = if (currentState.decks[deckIndex].cards[index].isSelected())
                        num+1 else num-1
                )
            }

        } else {
            _uiState.value.bundles[bundleIndex].decks[deckIndex].cards[index].toggleSelection()
            _uiState.update { currentState ->
                currentState.copy(
                    numSelectedCards = if (currentState.bundles[bundleIndex].decks[deckIndex].cards[index].isSelected())
                        num+1 else num-1
                )
            }
        }
    }

    fun getDeck(index: Int) : Deck {
        return _uiState.value.decks[index]
    }

    fun getDeckFromCurrentBundle(index: Int) : Deck {
        val bundleIndex = _uiState.value.currentBundleIndex
        return _uiState.value.bundles[bundleIndex!!].decks[index]
    }

    fun getBundle(index: Int) : Bundle {
        return _uiState.value.bundles[index]
    }

    fun toggleBundleSelection(index: Int) {
        _uiState.value.bundles[index].toggleSelection()
        val num = _uiState.value.numSelectedBundles
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedBundles = if (currentState.bundles[index].isSelected())
                    num+1 else num-1
            )
        }

    }

    fun deselectAllDecks() {
        for (bundle in _uiState.value.bundles) {
            for (deck in bundle.decks) {
                deck.deselect()
            }
        }
        for (deck in _uiState.value.decks) {
            deck.deselect()
        }
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedDecks = 0,
            )
        }
    }

    fun selectAllCardsInCurrentDeck() {
        deselectAllCards()
        val cards = getCurrentDeck().cards
        for (card in cards) {
            card.select()
        }
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = cards.size,
            )
        }
    }

    fun deselectAllCardsInCurrentDeck() {
        val cards = getCurrentDeck().cards
        for (card in cards) {
            card.deselect()
        }
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = 0,
            )
        }
    }

    fun deselectAllCards() {
        if (_uiState.value.numSelectedCards == 0) return
        for (bundle in _uiState.value.bundles) {
            for (deck in bundle.decks) {
                for (card in deck.cards) {
                    card.deselect()
                }
            }
        }
        for (deck in _uiState.value.decks) {
            for (card in deck.cards) {
                card.deselect()
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = 0,
            )
        }
    }

    fun deleteSelectedCardsInCurrentDeck() {
        val deck = getCurrentDeck()
        val newCards = mutableListOf<Card>()
        for (card in deck.cards) {
            if (!card.isSelected())
                newCards.add(card)
        }
        update()
        deselectAllCardsInCurrentDeck()
    }

    fun getNumDecks() : Int {
        return _uiState.value.decks.size
    }

    fun getNumDecksInCurrentBundle() : Int {
        val index = _uiState.value.currentBundleIndex
        return if (index == null)
            0
        else
            _uiState.value.bundles[index].decks.size
    }

    fun getNumBundles() : Int {
        return _uiState.value.bundles.size
    }

    fun getNumTotalCardCollection() : Int {
        return getNumDecks() + getNumBundles()
    }

    fun getNumCardsInCurrentDeck() : Int {
        //return DataSource.decks[1].cards.size //TODO remove
        return getCurrentDeck().cards.size
    }

    fun getCardFromCurrentDeck(index: Int) : Card {
        //return DataSource.decks[1].cards[index] //TODO remove
        return getCurrentDeck().cards[index]
    }
}