package com.example.flashcards.ui.menu

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.Card
import com.example.flashcards.data.CardsRepository
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
        val param: Int = checkNotNull(savedStateHandle["id"])

        Log.d("debug", "$param")

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    deck = cardsRepository.getDeck(id = param).toDeck()
                )
            }
        }

        reset()
    }

    fun softReset() {
        deselectAllCards()
        closeCardSelector()
    }

    fun reset() {
        softReset()
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

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }

    fun toggleCardSelection(index: Int) {
        val num = _uiState.value.numSelectedCards

        _uiState.value.deck!!.cards[index].toggleSelection()
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = if (currentState.deck!!.cards[index].isSelected())
                    num+1 else num-1
            )
        }
    }

    fun selectAllCardsInCurrentDeck() {
        deselectAllCards()
        val cards = _uiState.value.deck!!.cards
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
        val cards = _uiState.value.deck!!.cards
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
        for (card in _uiState.value.deck!!.cards) {
            card.deselect()
        }
        _uiState.update { currentState ->
            currentState.copy(
                numSelectedCards = 0,
            )
        }
    }

    fun deleteSelectedCardsInCurrentDeck() {
        val newCards = mutableListOf<Card>()
        for (card in _uiState.value.deck!!.cards) {
            if (!card.isSelected())
                newCards.add(card)
        }
        update()
        deselectAllCardsInCurrentDeck()
    }

    fun getNumCardsInCurrentDeck() : Int {
        //return DataSource.decks[1].cards.size //TODO remove
        return _uiState.value.deck!!.cards.size
    }

    fun getCardFromCurrentDeck(index: Int) : Card {
        //return DataSource.decks[1].cards[index] //TODO remove
        return _uiState.value.deck!!.cards[index]
    }
}