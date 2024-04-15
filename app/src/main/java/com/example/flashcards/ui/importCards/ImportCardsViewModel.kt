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
                    isBringFromDecksScreenOpen = false,
                    isImportThroughTextScreenOpen = false,
                    isUploadCsvFileScreenOpen = false,
                    isTipOpen = false,
                    numSelectedCards = 0,
                )
            }
        }
    }

    fun setInputText(inputText: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputText = inputText
            )
        }
    }

    fun setQuestionLines(questionLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                questionLines = questionLines
            )
        }
    }

    fun setAnswerLines(answerLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                answerLines = answerLines
            )
        }
    }

    fun setHintLines(hintLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                hintLines = hintLines
            )
        }
    }

    fun setExampleLines(exampleLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                exampleLines = exampleLines
            )
        }
    }

    fun setIgnoredLines(ignoredLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                ignoredLines = ignoredLines
            )
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

    fun toggleBringFromDecksScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                isBringFromDecksScreenOpen = !_uiState.value.isBringFromDecksScreenOpen
            )
        }
    }

    fun toggleUploadCsvFileScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                isUploadCsvFileScreenOpen = !_uiState.value.isUploadCsvFileScreenOpen
            )
        }
    }

    fun toggleImportThroughTextScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                isImportThroughTextScreenOpen = !_uiState.value.isImportThroughTextScreenOpen
            )
        }
    }

    fun toggleTip() {
        _uiState.update { currentState ->
            currentState.copy(
                isTipOpen = !_uiState.value.isTipOpen
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

    fun textToCards(maxCards: Int = -1): List<Card> {
        val qL = getParsedInputLines(_uiState.value.questionLines)
        val aL = getParsedInputLines(_uiState.value.answerLines)
        val hL = getParsedInputLines(_uiState.value.hintLines)
        val eL = getParsedInputLines(_uiState.value.exampleLines)
        val iL = getParsedInputLines(_uiState.value.ignoredLines)

        val QUESTION = 1
        val ANSWER = 2
        val HINT = 3
        val EXAMPLE = 4
        val IGNORED = 5

        val lineMap = mutableMapOf<Int, Int>()
        var max = 0
        for (n in qL) { lineMap[n] = QUESTION; if (n > max) max = n }
        for (n in aL) { lineMap[n] = ANSWER; if (n > max) max = n }
        for (n in hL) { lineMap[n] = HINT; if (n > max) max = n }
        for (n in eL) { lineMap[n] = EXAMPLE; if (n > max) max = n }
        for (n in iL) { lineMap[n] = IGNORED; if (n > max) max = n }

        val subDeckCards = mutableListOf<Card>()
        var i = 0
        var qT = ""
        var aT = ""
        var hT = ""
        var eT = ""

        val temp = _uiState.value.inputText.split("\n")
        var numCards = 0
        for (segment in temp) {
            if (segment.isNotBlank()) {
                when (lineMap[i+1]) {
                    QUESTION -> qT += (if (qT.isBlank()) "\n" else "") + segment
                    ANSWER -> aT += (if (aT.isBlank()) "\n" else "") + segment
                    HINT -> hT += (if (hT.isBlank()) "\n" else "") + segment
                    EXAMPLE -> eT += (if (eT.isBlank()) "\n" else "") + segment
                    else -> {}
                }
                i++

                if (i == max && qT.isNotBlank() && aT.isNotBlank()) {
                    i = 0
                    subDeckCards.add(
                        Card(
                            questionText = qT,
                            answerText = aT,
                            hintText = hT,
                            exampleText = eT,
                        )
                    )
                    qT = ""
                    aT = ""
                    hT = ""
                    eT = ""
                    numCards++
                    if (numCards == maxCards) {
                        return subDeckCards
                    }
                }
            }
        }

        return subDeckCards
    }

    private fun getParsedInputLines(inputLines: String): Set<Int> {
        val parsedLines = mutableSetOf<Int>()
        inputLines.trim()
        val lines = inputLines.split(",").toTypedArray()
        for (line in lines) {
            if (line.contains("-")) {
                val range = inputLines.split("-").toTypedArray()
                val start: Int? = range[0].toIntOrNull()
                val end: Int? = range[1].toIntOrNull()
                if (start != null && end != null) {
                    for (i in start..end) {
                        parsedLines.add(i)
                    }
                }
            }
            val parsedLine: Int? = line.toIntOrNull()
            if (parsedLine != null) {
                parsedLines.add(parsedLine)
            }
        }
        return parsedLines
    }

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }
}