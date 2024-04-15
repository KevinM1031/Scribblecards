package com.example.flashcards.ui.deck

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
    val excludeMastered: Boolean = false,
    val resetHistory: Boolean = true,

    val inputText: String = "",
    val questionLines: String = "",
    val answerLines: String = "",
    val hintLines: String = "",
    val exampleLines: String = "",
    val ignoredLines: String = "",

    val isBringFromDecksScreenOpen: Boolean = false,
    val isImportThroughTextScreenOpen: Boolean = false,
    val isUploadCsvFileScreenOpen: Boolean = false,

    val isTipOpen: Boolean = false,
    val tipText: String = "",

    val lastUpdated: Long = 0,
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