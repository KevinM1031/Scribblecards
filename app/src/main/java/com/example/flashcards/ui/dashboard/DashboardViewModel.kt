package com.example.flashcards.ui.dashboard

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.CardsRepository
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val cardsRepository: CardsRepository,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun softReset() {
        closeBundleCreator()
        closeCreateOptions()
        closeBundleCreatorDialog()
        closeDeckCreatorDialog()
        closeEditBundleNameDialog()
        drop()

        viewModelScope.launch {
            updateMasteryLevels()
            loadCards()
            deleteAllEmptyBundles()
        }

        _uiState.update { currentState ->
            currentState.copy(
                isBundleCloseAnimRequested = false,
                isCreateOptionsCloseAnimRequested = false,
            )
        }
    }

    fun reset() {
        softReset()

        closeBundle()
    }

    suspend fun loadCards() {

        val bundles = cardsRepository.getAllBundlesWithDecks()
        val decks = cardsRepository.getAllDecksNotInBundle()

        _uiState.update { currentState ->
            currentState.copy(
                bundles = bundles,
                decks = decks,
            )
        }
    }

    /**
     * WARNING - Expensive function
     */
    suspend fun updateMasteryLevels() {
        val decks = cardsRepository.getAllDecksWithCards()
        for (deck in decks) {
            deck.updateMasteryLevel()
            cardsRepository.updateDeck(deck.deck)
        }
    }

    fun dragStart(position: Offset, content: @Composable () -> Unit, data: DragData) {
        _uiState.update { currentState ->
            currentState.copy(
                isDragging = true,
                dragPosition = position,
                dragContent = content,
                dragData = data,
            )
        }
    }

    fun drop() {
        _uiState.update { currentState ->
            currentState.copy(
                isDragging = false,
                dragPosition = Offset.Zero,
                dragContent = null,
                dragData = null,
            )
        }
    }

    fun closeCreateOptions() {
        _uiState.update { currentState ->
            currentState.copy(
                isCreateOptionsOpen = false,
                isCreateOptionsCloseAnimRequested = false,
            )
        }
    }

    fun toggleCreateOptions() {
        _uiState.update { currentState ->
            currentState.copy(
                isCreateOptionsOpen = !_uiState.value.isCreateOptionsOpen,
                isCreateOptionsCloseAnimRequested = false,
            )
        }
    }

    fun requestCloseCreateOptionsAnim() {
        _uiState.update { currentState ->
            currentState.copy(
                isCreateOptionsCloseAnimRequested = true,
            )
        }
    }

    fun requestOpenAnim() {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenAnimRequested = true,
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
                isBundleCloseAnimRequested = false,
                isBundleOpen = true,
                isBundleFakeClosed = false,
            )
        }
    }

    fun closeBundle() {
        closeEditBundleNameDialog()
        if (_uiState.value.isRemoveDeckFromBundleUiOpen) closeRemoveDeckFromBundleUi()
        _uiState.update { currentState ->
            currentState.copy(
                currentBundleIndex = null,
                isBundleCloseAnimRequested = false,
                isBundleOpen = false,
                isBundleFakeClosed = false,
            )
        }
    }

    fun requestCloseBundleAnim() {
        _uiState.update { currentState ->
            currentState.copy(
                isBundleCloseAnimRequested = true,
            )
        }
    }

    fun fakeCloseBundle() {
        _uiState.update { currentState ->
            currentState.copy(
                isBundleCloseAnimRequested = true,
                isBundleFakeClosed = true,
            )
        }
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
        loadCards()
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
                isBundleOpen = false,
            )
        }

        deleteAllEmptyBundles()
    }

    suspend fun moveDeckToBundle(deckIndex: Int, bundleIndex: Int, deckBundleIndex: Int? = null) {
        if (deckBundleIndex != null && _uiState.value.bundles.getOrNull(deckBundleIndex) == null) return
        val deck = if (deckBundleIndex != null)
            _uiState.value.bundles[deckBundleIndex].decks.getOrNull(deckIndex)
            else _uiState.value.decks.getOrNull(deckIndex)
        val bundle = _uiState.value.bundles.getOrNull(bundleIndex)

        if (deck == null || bundle == null) return
        deck.bundleId = bundle.bundle.id
        deck.deselect()
        cardsRepository.updateDeck(deck)
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
        deleteAllEmptyBundles()
    }

    suspend fun moveDeckOutOfBundle(deckIndex: Int, bundleIndex: Int) {
        if (_uiState.value.bundles.getOrNull(bundleIndex) == null) return
        val deck = _uiState.value.bundles[bundleIndex].decks.getOrElse(deckIndex) { return }
        deck.bundleId = -1
        cardsRepository.updateDeck(deck)
        deleteAllEmptyBundles()
    }

    suspend fun mergeDecksIntoBundle(deck1Index: Int, deck2Index: Int, deck1BundleIndex: Int? = null) {
        if (deck1BundleIndex != null && _uiState.value.bundles.getOrNull(deck1BundleIndex) == null) return
        val deck1 = if (deck1BundleIndex != null)
            _uiState.value.bundles[deck1BundleIndex].decks.getOrNull(deck1Index)
        else _uiState.value.decks.getOrNull(deck1Index)
        if (deck1 == null) return

        val bundleId = cardsRepository.insertBundle(Bundle(
            name = deck1.name + " & " + _uiState.value.decks[deck2Index].name
        ))

        deck1.bundleId = bundleId
        deck1.deselect()
        cardsRepository.updateDeck(_uiState.value.decks[deck1Index])
        _uiState.value.decks[deck2Index].bundleId = bundleId
        _uiState.value.decks[deck2Index].deselect()
        cardsRepository.updateDeck(_uiState.value.decks[deck2Index])
        loadCards()
    }

    suspend fun mergeBundleWithBundle(selectedBundleIndex: Int, targetBundleIndex: Int) {
        for (deck in _uiState.value.bundles[selectedBundleIndex].decks) {
            deck.bundleId = _uiState.value.bundles[targetBundleIndex].bundle.id
            deck.deselect()
            cardsRepository.updateDeck(deck)
        }
        cardsRepository.deleteBundle(_uiState.value.bundles[selectedBundleIndex].bundle)
        loadCards()
    }

    suspend fun createDeck(name: String) {

        val deck = Deck(name = name.trim())

        if (_uiState.value.isBundleOpen) {
            cardsRepository.insertDeckToBundle(deck, _uiState.value.bundles[_uiState.value.currentBundleIndex!!].bundle.id)
        } else {
            cardsRepository.insertDeck(deck)
        }

        loadCards()
    }

    suspend fun updateCurrentBundleName(name: String) {
        val bundle = _uiState.value.bundles[_uiState.value.currentBundleIndex!!].bundle
        bundle.name = name
        bundle.deselect()
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

    fun getDeckFromCurrentBundle(index: Int) : Deck? {
        val bundleIndex = _uiState.value.currentBundleIndex
        if (bundleIndex == null) return null
        return _uiState.value.bundles[bundleIndex].decks[index]
    }

    fun getBundle(index: Int) : BundleWithDecks? {
        return _uiState.value.bundles.getOrNull(index)
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
            _uiState.value.bundles.getOrNull(index)?.decks?.size ?: 0
    }

    fun getNumBundles() : Int {
        return _uiState.value.bundles.size
    }
}