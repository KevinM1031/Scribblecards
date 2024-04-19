package com.example.flashcards.ui.deck

import androidx.compose.ui.focus.FocusRequester
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.data.relations.BundleWithDecksWithCards
import com.example.flashcards.data.relations.DeckWithCards

data class ImportCardsUiState(
    val deck: DeckWithCards = DeckWithCards(Deck(), listOf()),
    val decks: List<Deck> = listOf(),
    val bundles: List<BundleWithDecks> = listOf(),
    val param: Long = -1,

    val numSelectedCards: Int = 0,
    val subDecks: List<SubDeck> = listOf(),

    val isTipOpen: Boolean = false,
    val tipText: String = "",

    val isBringFromDecksScreenOpen: Boolean = false,
    val isImportThroughTextScreenOpen: Boolean = false,
    val isUploadCsvFileScreenOpen: Boolean = false,

    val lastUpdated: Long = 0,

    // bring from decks screen states
    val bFD_selectedBundle: BundleWithDecks? = null,
    val bFD_selectedDeck: Deck? = null,
    val bFD_excludeMastered: Boolean = false,
    val bFD_resetHistory: Boolean = true,

    // import through text screen states
    val iTT_inputText: String = "",
    val iTT_questionLines: String = "",
    val iTT_answerLines: String = "",
    val iTT_hintLines: String = "",
    val iTT_exampleLines: String = "",
    val iTT_ignoredLines: String = "",

    val iTT_errorState: ITT_ErrorState = ITT_ErrorState.NO_ERROR,
    val iTT_errorState2: ITT_ErrorState = ITT_ErrorState.NO_ERROR,

    val iTT_focusRequested: Boolean = false,
    val iTT_focusRequesterT: FocusRequester = FocusRequester(),
    val iTT_focusRequesterQ: FocusRequester = FocusRequester(),
    val iTT_focusRequesterA: FocusRequester = FocusRequester(),
    val iTT_focusRequesterH: FocusRequester = FocusRequester(),
    val iTT_focusRequesterE: FocusRequester = FocusRequester(),
    val iTT_focusRequesterI: FocusRequester = FocusRequester(),
)

data class SubDeck(
    val name: String,
    val type: SubDeckType,
    val cards: List<Card>,
)

enum class SubDeckType {
    DEFAULT,
    TEXT,
    CSV,
}

enum class ITT_ErrorState(
        val isTextError: Boolean,
        val isQuestionLineError: Boolean,
        val isAnswerLineError: Boolean,
        val isHintLineError: Boolean,
        val isExampleLineError: Boolean,
        val isIgnoredLineError: Boolean,
        ) {
    NO_ERROR(false, false, false, false, false, false),
    TEXT_INCOMPLETE(true, false, false, false, false, false),
    TEXT_TOO_LONG(true, false, false, false, false, false),
    QUESTION_LINES_DUPLICATE(false, true, false, false, false, false),
    ANSWER_LINES_DUPLICATE(false, false, true, false, false, false),
    HINT_LINES_DUPLICATE(false, false, false, true, false, false),
    EXAMPLE_LINES_DUPLICATE(false, false, false, false, true, false),
    IGNORED_LINES_DUPLICATE(false, false, false, false, false, true),

}