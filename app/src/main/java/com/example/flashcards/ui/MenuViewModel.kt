package com.example.flashcards.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Card
import com.example.flashcards.data.CardCollection
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
        reset()
    }

    fun softReset() {
        _uiState.update { currentState ->
            currentState.copy(
                decks = DataSource.decks,
                bundles = DataSource.bundles,
            )
        }
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
        deselectAll()
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

    fun setUserInput(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                userInput = input,
            )
        }
    }

    fun openBundle(bundleIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentBundleIndex = bundleIndex,
                isBundleOpen = true
            )
        }
    }

    fun closeBundle() {
        _uiState.update { currentState ->
            currentState.copy(
                currentBundleIndex = null,
                isBundleOpen = false
            )
        }
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
                decks = tempDecks.toList(),
                bundles = tempBundles.toList(),
            )
        }
    }

    fun openDeck(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentDeckIndex = index
            )
        }
    }

    fun closeDeck() {
        _uiState.update { currentState ->
            currentState.copy(
                currentDeckIndex = null
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
                    numSelectedDecks = if (_uiState.value.decks[index].isSelected())
                        num+1 else num-1
                )
            }

        } else {
            _uiState.value.bundles[bundleIndex].decks[index].toggleSelection()
            _uiState.update { currentState ->
                currentState.copy(
                    numSelectedDecks = if (_uiState.value.bundles[bundleIndex].decks[index].isSelected())
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
        return if (bundleIndex != null) {
            _uiState.value.bundles[bundleIndex].decks[index]
        } else {
            _uiState.value.decks[index]
        }
    }

    fun getBundle(index: Int) : Bundle {
        return _uiState.value.bundles[index]
    }

    fun toggleBundleSelection(index: Int) {
        _uiState.value.bundles[index].toggleSelection()
        val num = _uiState.value.numSelectedBundles
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedBundles = if (_uiState.value.bundles[index].isSelected())
                    num+1 else num-1
            )
        }

    }

    fun deselectAll() {
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
                numSelectedBundles = 0,
            )
        }
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
}