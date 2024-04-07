package com.example.flashcards.ui.deck

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.CardsRepository
import com.example.flashcards.data.entities.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckViewModel(
    private val cardsRepository: CardsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                param = checkNotNull(savedStateHandle["id"])
            )
        }
        reset()
    }

    fun softReset() {

        if (!_uiState.value.isDeckDeleted) {
            viewModelScope.launch {
                val deck = cardsRepository.getDeckWithCards(id = _uiState.value.param)
                _uiState.update { currentState ->
                    currentState.copy(
                        deck = deck,
                    )
                }
            }
        }
    }

    fun reset() {
        softReset()
        deselectAllCards()
        _uiState.update { currentState ->
            currentState.copy(
                isEditDeckNameDialogOpen = false,
                isDeleteCardDialogOpen = false,
                isDeleteDeckDialogOpen = false,
                isCardSelectorOpen = false,
                isTipOpen = false,
                userInput = null,
                isDeckDeleted = false,
            )
        }
    }

    suspend fun updateDeck() {
        _uiState.value.deck.deck.dateUpdated = System.currentTimeMillis()
        cardsRepository.updateDeck(_uiState.value.deck.deck)
    }

    suspend fun updateDeckName(name: String) {
        _uiState.value.deck.deck.name = name
        updateDeck()
    }

    fun setUserInput(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                userInput = text,
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

    fun toggleTip() {
        _uiState.update { currentState ->
            currentState.copy(
                isTipOpen = !currentState.isTipOpen,
            )
        }
    }

    fun toggleDeleteCardDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteCardDialogOpen = !currentState.isDeleteCardDialogOpen,
            )
        }
    }

    fun toggleDeleteDeckDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteDeckDialogOpen = !currentState.isDeleteDeckDialogOpen,
            )
        }
    }

    fun toggleEditDeckNameDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isEditDeckNameDialogOpen = !currentState.isEditDeckNameDialogOpen,
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

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }

    suspend fun deleteDeck() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeckDeleted = true,
            )
        }
        cardsRepository.deleteDeckWithCards(_uiState.value.deck)
    }

    fun toggleCardSelection(index: Int) {
        val num = _uiState.value.numSelectedCards

        _uiState.value.deck.cards[index].toggleSelection()
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = if (currentState.deck.cards[index].isSelected)
                    num+1 else num-1
            )
        }
    }

    fun selectAllCardsInCurrentDeck() {
        deselectAllCards()
        val cards = _uiState.value.deck.cards
        for (card in cards) {
            card.select()
        }
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = cards.size,
            )
        }
    }

    fun deselectAllCards() {
        if (_uiState.value.numSelectedCards == 0) return
        for (card in _uiState.value.deck.cards) {
            card.deselect()
        }
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = 0,
            )
        }
    }

    suspend fun deleteSelectedCardsInCurrentDeck() {
        for (card in _uiState.value.deck.cards) {
            if (card.isSelected) {
                cardsRepository.deleteCard(card)
            }
        }
        updateDeck()
        _uiState.update { currentState ->
            currentState.copy(
                deck = cardsRepository.getDeckWithCards(_uiState.value.param),
                numSelectedCards = 0,
            )
        }
    }

    fun getNumCardsInCurrentDeck() : Int {
        return _uiState.value.deck.cards.size
    }

    fun getCardFromCurrentDeck(index: Int) : Card {
        return _uiState.value.deck.cards[index]
    }
}