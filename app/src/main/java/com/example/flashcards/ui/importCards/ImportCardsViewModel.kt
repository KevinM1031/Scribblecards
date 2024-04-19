package com.example.flashcards.ui.deck

import android.util.Log
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.CardsRepository
import com.example.flashcards.data.Constants
import com.example.flashcards.data.Settings
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
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
        resetBringFromDecksScreen()
        resetImportThroughTextScreen()

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

    fun resetBringFromDecksScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_selectedBundle = null,
                bFD_selectedDeck = null,
                bFD_excludeMastered = false,
                bFD_resetHistory = true,
            )
        }
    }

    fun resetImportThroughTextScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_errorState = ITT_ErrorState.NO_ERROR,
                iTT_errorState2 = ITT_ErrorState.NO_ERROR,
                iTT_inputText = "",
                iTT_questionLines = "",
                iTT_answerLines = "",
                iTT_hintLines = "",
                iTT_exampleLines = "",
                iTT_ignoredLines = "",
                iTT_focusRequested = false,
                iTT_focusRequesterT = FocusRequester(),
                iTT_focusRequesterQ = FocusRequester(),
                iTT_focusRequesterA = FocusRequester(),
                iTT_focusRequesterH = FocusRequester(),
                iTT_focusRequesterE = FocusRequester(),
                iTT_focusRequesterI = FocusRequester(),
            )
        }
    }

    fun getTotalNumCards(): Int {
        var n = 0
        for (subDeck in _uiState.value.subDecks) {
            n += subDeck.cards.size
        }
        return n
    }

    fun setInputText(inputText: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_inputText = inputText
            )
        }
    }

    fun setQuestionLines(questionLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_questionLines = questionLines
            )
        }
    }

    fun setAnswerLines(answerLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_answerLines = answerLines
            )
        }
    }

    fun setHintLines(hintLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_hintLines = hintLines
            )
        }
    }

    fun setExampleLines(exampleLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_exampleLines = exampleLines
            )
        }
    }

    fun setIgnoredLines(ignoredLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_ignoredLines = ignoredLines
            )
        }
    }

    fun setExcludeMastered(excludeMastered: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_excludeMastered = excludeMastered
            )
        }
    }

    fun setResetHistory(resetHistory: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_resetHistory = resetHistory
            )
        }
    }

    fun toggleBringFromDecksScreen() {
        resetBringFromDecksScreen()
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
        resetImportThroughTextScreen()
        _uiState.update { currentState ->
            currentState.copy(
                isImportThroughTextScreenOpen = !_uiState.value.isImportThroughTextScreenOpen,
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

    fun setImportThroughTextScreenErrorState(errorState: ITT_ErrorState) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_errorState = errorState
            )
        }
    }

    fun selectBundle(bundle: BundleWithDecks?) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_selectedBundle = bundle
            )
        }
    }

    fun selectDeck(deck: Deck?) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_selectedDeck = deck
            )
        }
    }

    fun setImportThroughScreenFocusRequest(requested: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_focusRequested = requested
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
            if (!_uiState.value.bFD_excludeMastered || card.getMasteryLevel() < 1f) {
                subDeckCards.add(
                    if (_uiState.value.bFD_resetHistory)
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

    fun textToCards(maxCards: Int = -1, checkForErrors: Boolean = false): List<Card>? {
        val qL = getParsedInputLines(_uiState.value.iTT_questionLines)
        val aL = getParsedInputLines(_uiState.value.iTT_answerLines)
        val hL = getParsedInputLines(_uiState.value.iTT_hintLines)
        val eL = getParsedInputLines(_uiState.value.iTT_exampleLines)
        val iL = getParsedInputLines(_uiState.value.iTT_ignoredLines)

        val QUESTION = 1
        val ANSWER = 2
        val HINT = 3
        val EXAMPLE = 4
        val IGNORED = 5

        val lineMap = mutableMapOf<Int, Int>()
        var max = 0
        val testError: (Int, Int) -> Boolean = { n, curr ->
            val isError = when (lineMap[n]) {
                curr -> false
                QUESTION -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.QUESTION_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                ANSWER -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.ANSWER_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                HINT -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.HINT_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                EXAMPLE -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.EXAMPLE_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                IGNORED -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.IGNORED_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                else -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.NO_ERROR,
                        )
                    }
                    false
                }
            }

            isError
        }

        for (n in qL) {
            if (checkForErrors && testError(n, QUESTION)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.QUESTION_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = QUESTION
            if (n > max) max = n
        }
        for (n in aL) {
            if (checkForErrors && testError(n, ANSWER)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.ANSWER_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = ANSWER
            if (n > max) max = n
        }
        for (n in hL) {
            if (checkForErrors && testError(n, HINT)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.HINT_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = HINT
            if (n > max) max = n
        }
        for (n in eL) {
            if (checkForErrors && testError(n, EXAMPLE)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.EXAMPLE_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = EXAMPLE
            if (n > max) max = n
        }
        for (n in iL) {
            if (checkForErrors && testError(n, IGNORED)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.IGNORED_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = IGNORED
            if (n > max) max = n
        }

        val subDeckCards = mutableListOf<Card>()
        var i = 0
        var qT = ""
        var aT = ""
        var hT = ""
        var eT = ""

        val temp = _uiState.value.iTT_inputText.split("\n")
        var numCards = 0
        for (segment in temp) {
            if (segment.isNotBlank()) {
                when (lineMap[i+1]) {
                    QUESTION -> qT += (if (qT.isBlank()) "" else "\n") + segment
                    ANSWER -> aT += (if (aT.isBlank()) "" else "\n") + segment
                    HINT -> hT += (if (hT.isBlank()) "" else "\n") + segment
                    EXAMPLE -> eT += (if (eT.isBlank()) "" else "\n") + segment
                    else -> {}
                }
                i++

                if (i == max && qT.isNotBlank() && aT.isNotBlank()) {
                    subDeckCards.add(
                        Card(
                            questionText = qT,
                            answerText = aT,
                            hintText = hT,
                            exampleText = eT,
                        )
                    )
                    i = 0
                    qT = ""
                    aT = ""
                    hT = ""
                    eT = ""
                    numCards++
                    if (checkForErrors && numCards > Constants.MAX_CARDS) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                iTT_errorState = ITT_ErrorState.TEXT_TOO_LONG
                            )
                        }
                        return null
                    } else if (numCards == maxCards) {
                        return subDeckCards
                    }
                }
            }
        }

        if (i > 0 && checkForErrors) {
            _uiState.update { currentState ->
                currentState.copy(
                    iTT_errorState = ITT_ErrorState.TEXT_INCOMPLETE
                )
            }
            return null
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