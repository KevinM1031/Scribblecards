package com.example.flashcards.ui.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.CardsRepository
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.sql.DataSource

class DashboardViewModel(
    private val cardsRepository: CardsRepository,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        reset()
    }

    suspend fun loadCards() {

        //deleteAllCards()
        //loadCardsFromDataSource()

        _uiState.update { currentState ->
            currentState.copy(
                bundles = cardsRepository.getAllBundlesWithDecks(),
                decks = cardsRepository.getAllDecksNotInBundle(),
            )
        }
    }

    suspend fun loadCardsFromDataSource() {
        val bID1 = cardsRepository.insertBundle(Bundle(name="Bundle 1"))
        val bID2 = cardsRepository.insertBundle(Bundle(name="Bundle 2"))

        val dID1 = cardsRepository.insertDeckToBundle(Deck(name = "A", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f), bID1)
        val dID2 = cardsRepository.insertDeckToBundle(Deck(name = "B", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f), bID1)
        val dID3 = cardsRepository.insertDeckToBundle(Deck(name = "C", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f), bID1)
        val dID4 = cardsRepository.insertDeckToBundle(Deck(name = "D", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f), bID1)
        val dID5 = cardsRepository.insertDeckToBundle(Deck(name = "E", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f), bID2)
        val dID6 = cardsRepository.insertDeck(Deck(name = "F", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f))
        val dID7 = cardsRepository.insertDeck(Deck(name = "G", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f))
        val dID8 = cardsRepository.insertDeck(Deck(name = "H", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f))
        val dID9 = cardsRepository.insertDeck(Deck(name = "I", dateCreated = 0, dateUpdated = 0, dateStudied = 0, masteryLevel = 0.73f))

        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID1)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID2)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID3)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID4)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID5)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID6)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID7)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID8)}
        for (i in 1..10) { cardsRepository.insertCardToDeck( Card(questionText = "Rattlesnake", answerText = "Reptile", hintText = "HINT", exampleText = "EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE EXAMPLE",), dID9)}



    }

    suspend fun deleteAllCards() {
        cardsRepository.getAllBundles().let {
            for (bundle in it) {
                cardsRepository.deleteBundle(bundle)
            }
        }
        cardsRepository.getAllDecks().let {
            for (deck in it) {
                cardsRepository.deleteDeck(deck)
            }
        }
        cardsRepository.getAllCards().let {
            for (card in it) {
                cardsRepository.deleteCard(card)
            }
        }
    }

    fun softReset() {
        viewModelScope.launch {
            loadCards()
        }
    }

    fun reset() {
        softReset()

        closeBundle()
        closeBundleCreator()
        closeCreateOptions()
        closeBundleCreatorDialog()
        closeDeckCreatorDialog()
        closeEditBundleNameDialog()
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
        deselectAllDecks()
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

    fun openDeckCreatorDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeckCreatorDialogOpen = true,
                userInput = null,
            )
        }
    }

    fun closeDeckCreatorDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeckCreatorDialogOpen = false,
                userInput = null,
            )
        }
    }

    fun openEditBundleNameDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isEditBundleNameDialogOpen = true,
                userInput = _uiState.value.bundles[_uiState.value.currentBundleIndex!!].bundle.name
            )
        }
    }

    fun closeEditBundleNameDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isEditBundleNameDialogOpen = false,
                userInput = null,
            )
        }
    }

    fun openRemoveDeckFromBundleUi() {
        deselectAllDecks()
        _uiState.update { currentState ->
            currentState.copy(
                isRemoveDeckFromBundleUiOpen = true,
            )
        }
    }

    fun closeRemoveDeckFromBundleUi() {
        _uiState.update { currentState ->
            currentState.copy(
                isRemoveDeckFromBundleUiOpen = false,
            )
        }
        deselectAllDecks()
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
            )
        }
    }

    fun closeBundle() {
        closeEditBundleNameDialog()
        closeRemoveDeckFromBundleUi()
        _uiState.update { currentState ->
            currentState.copy(
                currentBundleIndex = null,
            )
        }
    }

    fun isBundleOpen(): Boolean {
        return _uiState.value.currentBundleIndex != null
    }

    suspend fun deleteAllEmptyBundles() {
        val bWDs = cardsRepository.getAllBundlesWithDecks()
        val bWDsToDelete = mutableListOf<Bundle>()
        for (bWD in bWDs) {
            if (bWD.decks.isEmpty()) {
                bWDsToDelete.add(bWD.bundle)
            }
        }
        for (bWD in bWDsToDelete) {
            cardsRepository.deleteBundle(bWD)
        }
    }

    /**
     * Uses selected decks to create a bundle.
     */
    suspend fun createBundle(name: String) {

        val bundleId = cardsRepository.insertBundle(Bundle(name = name.trim()))

        for (deck in _uiState.value.decks) {
            if (deck.isSelected) {
                deck.bundleId = bundleId
                deck.deselect()
                cardsRepository.updateDeck(deck)
            }
        }

        for (bundle in _uiState.value.bundles) {
            for (deck in bundle.decks) {
                if (deck.isSelected) {
                    deck.bundleId = bundleId
                    deck.deselect()
                    cardsRepository.updateDeck(deck)
                }
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentBundleIndex = null,
                currentDeckIndex = null,
                numSelectedCards = 0,
            )
        }

        deleteAllEmptyBundles()
        loadCards()
    }

    suspend fun moveDeckToBundle(deckIndex: Int, bundleIndex: Int) {
        _uiState.value.decks[deckIndex].bundleId = _uiState.value.bundles[bundleIndex].bundle.id
        cardsRepository.updateDeck(_uiState.value.decks[deckIndex])
        loadCards()
    }

    suspend fun moveSelectedDecksOutOfBundle() {
        for (bundle in _uiState.value.bundles) {
            for (deck in bundle.decks) {
                if (deck.isSelected) {
                    deck.bundleId = -1
                    deck.deselect()
                    cardsRepository.updateDeck(deck)
                }
            }
        }
        loadCards()
    }

    suspend fun mergeDecksIntoBundle(deck1Index: Int, deck2Index: Int) {
        val bundleId = cardsRepository.insertBundle(Bundle(
            name = _uiState.value.decks[deck1Index].name + " & " + _uiState.value.decks[deck2Index].name
        ))
        _uiState.value.decks[deck1Index].bundleId = bundleId
        cardsRepository.updateDeck(_uiState.value.decks[deck1Index])
        _uiState.value.decks[deck2Index].bundleId = bundleId
        cardsRepository.updateDeck(_uiState.value.decks[deck2Index])
        loadCards()
    }

    suspend fun mergeBundleWithBundle(selectedBundleIndex: Int, targetBundleIndex: Int) {
        for (deck in _uiState.value.bundles[selectedBundleIndex].decks) {
            deck.bundleId = _uiState.value.bundles[targetBundleIndex].bundle.id
            cardsRepository.updateDeck(deck)
        }
        cardsRepository.deleteBundle(_uiState.value.bundles[selectedBundleIndex].bundle)
        loadCards()
    }

    suspend fun createDeck(name: String) {

        val deck = Deck(name = name.trim())

        if (isBundleOpen()) {
            cardsRepository.insertDeckToBundle(deck, _uiState.value.bundles[_uiState.value.currentBundleIndex!!].bundle.id)
        } else {
            cardsRepository.insertDeck(deck)
        }

        loadCards()
    }

    suspend fun updateCurrentBundleName(name: String) {
        val bundle = _uiState.value.bundles[_uiState.value.currentBundleIndex!!].bundle
        bundle.name = name
        cardsRepository.updateBundle(bundle)
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
                    numSelectedDecks = if (currentState.decks[index].isSelected)
                        num+1 else num-1
                )
            }

        } else {
            _uiState.value.bundles[bundleIndex].decks[index].toggleSelection()
            _uiState.update { currentState ->
                currentState.copy(
                    numSelectedDecks = if (currentState.bundles[bundleIndex].decks[index].isSelected)
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
        return _uiState.value.bundles[index].bundle
    }

    fun toggleBundleSelection(index: Int) {
        _uiState.value.bundles[index].bundle.toggleSelection()
        val num = _uiState.value.numSelectedBundles
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedBundles = if (currentState.bundles[index].bundle.isSelected)
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
}