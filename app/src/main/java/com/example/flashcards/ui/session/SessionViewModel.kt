package com.example.flashcards.ui.session

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.CardsRepository
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.DeckWithCards
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionViewModel(
    private val cardsRepository: CardsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        reset()

        viewModelScope.launch {
            startSession(checkNotNull(savedStateHandle["id"]))
        }
    }

    fun softReset() {
        _uiState.update { currentState ->
            currentState.copy(
                isFlipped = false,
                isHintShown = false,
                isExampleShown = false,
            )
        }
    }

    fun reset() {
        softReset()

        _uiState.update { currentState ->
            currentState.copy(
                deck = DeckWithCards(Deck(), listOf()),
                isHistoryShown = false,
                isSessionCompleted = false,
                isQuitDialogOpen = false,
                isRestartDialogOpen = false,
                currentCardIndex = 0,
                usedCards = listOf(),
                completedCards = listOf(),
                cardHistory = mapOf(),
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

    fun showHint() {
        _uiState.update { currentState ->
            currentState.copy(
                isHintShown = true
            )
        }
    }

    fun showExample() {
        _uiState.update { currentState ->
            currentState.copy(
                isExampleShown = true
            )
        }
    }

    fun toggleInfo() {
        _uiState.update { currentState ->
            currentState.copy(
                isHistoryShown = !currentState.isHistoryShown
            )
        }
    }

    fun toggleQuitDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isQuitDialogOpen = !currentState.isQuitDialogOpen
            )
        }
    }

    fun toggleRestartDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isRestartDialogOpen = !currentState.isRestartDialogOpen
            )
        }
    }

    fun flipCard() {
        _uiState.update { currentState ->
            currentState.copy(
                isFlipped = !currentState.isFlipped,
            )
        }
    }

    fun setContentFlip(flipContent: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                flipContent = flipContent,
            )
        }
    }

    suspend fun startSession(param: Long) {
        val deck = cardsRepository.getDeckWithCards(param)

        if (deck.deck.showHints) showHint()
        if (deck.deck.showExamples) showExample()

        val activeCards = (0..<deck.cards.size).toMutableList()
        val usedCards = mutableListOf<Int>()
        val completedCards = mutableListOf<Int>()

        val cardHistory = mutableMapOf<Int, CardHistory>()
        for (i in activeCards) {
            cardHistory[i] = CardHistory()
        }

        activeCards.shuffle()
        val currentCardIndex = activeCards.removeFirst()

        _uiState.update { currentState ->
            currentState.copy(
                deck = deck,
                currentCardIndex = currentCardIndex,
                activeCards = activeCards,
                usedCards = usedCards,
                completedCards = completedCards,
                cardHistory = cardHistory,
            )
        }
    }

    fun endSession() {
        _uiState.update { currentState ->
            currentState.copy(
                isSessionCompleted = true,
            )
        }
    }

    fun getCurrentDeck(): DeckWithCards {
        return _uiState.value.deck
    }

    fun getCurrentCard(): Card {
        return getCurrentDeck().cards[_uiState.value.currentCardIndex]
    }

    fun getNumPerfect(): Int {
        var num = 0
        for (history in _uiState.value.cardHistory) {
            if (history.value.isPerfect()) {
                num++
            }
        }
        return num
    }

    fun skipCard() {
        var currentCardIndex = _uiState.value.currentCardIndex
        val activeCards = _uiState.value.activeCards.toMutableList()
        val usedCards = _uiState.value.usedCards.toMutableList()

        usedCards.add(currentCardIndex)

        // if active cards are empty, move used cards back to active cards and shuffle
        if (activeCards.isEmpty()) {
            for (i in usedCards) {
                activeCards.add(i)
            }
            activeCards.shuffle()
            usedCards.clear()
        }

        currentCardIndex = activeCards.removeFirst()

        _uiState.update { currentState ->
            currentState.copy(
                currentCardIndex = currentCardIndex,
                activeCards = activeCards,
                usedCards = usedCards,
            )
        }
        softReset()
    }

    fun nextCard(isCorrect: Boolean) {
        val deck = getCurrentDeck()
        var currentCardIndex = _uiState.value.currentCardIndex
        val activeCards = _uiState.value.activeCards.toMutableList()
        val usedCards = _uiState.value.usedCards.toMutableList()
        val completedCards = _uiState.value.completedCards.toMutableList()
        val currentCardHistory = _uiState.value.cardHistory[currentCardIndex]!!

        currentCardHistory.add(isCorrect)

        // if current card is completed, move to completed cards
        if (currentCardHistory.isComplete(isDoubleDifficulty = deck.deck.doubleDifficulty)) {
            completedCards.add(currentCardIndex)

        // if current card is not completed, add to used cards
        } else {
            usedCards.add(currentCardIndex)
        }

        if (activeCards.isEmpty()) {
            // if there are no cards left, end session
            if (usedCards.isEmpty()) {
                completedCards.clear()
                endSession()

            // if active cards is empty but there are still cards left in used cards, move them back to active cards and shuffle, then get new card
            } else {
                for (i in usedCards) {
                    activeCards.add(i)
                }
                activeCards.shuffle()
                usedCards.clear()
                currentCardIndex = activeCards.removeFirst()
            }

        } else {
            currentCardIndex = activeCards.removeFirst()
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentCardIndex = currentCardIndex,
                activeCards = activeCards,
                usedCards = usedCards,
                completedCards = completedCards,
            )
        }
        softReset()
    }

}