package com.example.flashcards.ui.deck

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.CardsRepository
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImportCardsViewModel(
    private val cardsRepository: CardsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(ImportCardsUiState())
    val uiState: StateFlow<ImportCardsUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                param = checkNotNull(savedStateHandle["id"])
            )
        }
        reset()
    }

    fun softReset() {
    }

    fun reset() {
        softReset()

        viewModelScope.launch {
            val deck = cardsRepository.getDeckWithCards(id = _uiState.value.param)
            val decks = cardsRepository.getAllDecks()
            val bundles = cardsRepository.getAllBundlesWithDecks()
            _uiState.update { currentState ->
                currentState.copy(
                    deck = deck,
                    decks = decks,
                    bundles = bundles,
                    subDecks = listOf(),
                    isBringFromDecksDialogOpen = false,
                    isImportFromQuizletDialogOpen = false,
                    isUploadCsvFileDialogOpen = false,
                    numSelectedCards = 0,
                )
            }
        }
    }

    fun setExcludeMastered(excludeMastered: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                excludeMastered = excludeMastered
            )
        }
    }

    fun setResetHistory(resetHistory: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                resetHistory = resetHistory
            )
        }
    }

    fun toggleBringFromDecksDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isBringFromDecksDialogOpen = !_uiState.value.isBringFromDecksDialogOpen
            )
        }
    }

    fun toggleUploadCsvFileDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isUploadCsvFileDialogOpen = !_uiState.value.isUploadCsvFileDialogOpen
            )
        }
    }

    fun toggleImportFromQuizletDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isImportFromQuizletDialogOpen = !_uiState.value.isImportFromQuizletDialogOpen
            )
        }
    }

    fun addSubDeck(subDeck: SubDeck) {
        val subDecks = _uiState.value.subDecks.toMutableList()
        subDecks.add(subDeck)
        _uiState.update { currentState ->
            currentState.copy(
                subDecks = subDecks
            )
        }
    }

    fun removeSubDeck(subDeck: SubDeck) {
        val subDecks = _uiState.value.subDecks.toMutableList()
        subDecks.remove(subDeck)
        _uiState.update { currentState ->
            currentState.copy(
                subDecks = subDecks
            )
        }
    }

    suspend fun importAll() {
        for (subDeck in _uiState.value.subDecks) {
            for (card in subDeck.cards) {
                cardsRepository.insertCardToDeck(card, _uiState.value.deck.deck.id)
            }
        }
    }

    suspend fun getAllCardsFromDeck(
        deck: Deck,
    ): List<Card> {
        val cards = cardsRepository.getDeckWithCards(deck.id).cards
        val subDeckCards = mutableListOf<Card>()

        for (card in cards) {
            if (!_uiState.value.excludeMastered || card.getMasteryLevel() < 1f) {
                subDeckCards.add(
                    if (_uiState.value.resetHistory)
                        Card(
                            questionText = card.questionText,
                            answerText = card.answerText,
                            hintText = card.hintText,
                            exampleText = card.exampleText,
                        )
                    else
                        Card(
                            questionText = card.questionText,
                            answerText = card.answerText,
                            hintText = card.hintText,
                            exampleText = card.exampleText,
                            numStudied = card.numStudied,
                            numPerfect = card.numPerfect,
                        )
                )
            }
        }
        return subDeckCards
    }

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }
}