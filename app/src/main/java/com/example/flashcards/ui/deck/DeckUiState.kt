package com.example.flashcards.ui.deck

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.DeckWithCards

data class DeckUiState(
    val deck: DeckWithCards = DeckWithCards(Deck(), listOf()),
    val param: Long = -1,

    val numSelectedCards: Int = 0,
    val sortType: SortType = SortType.DATE_STUDIED,

    val isTipOpen: Boolean = false,
    val isDeckDeleted: Boolean = false,
    val isSessionOptionsOpen: Boolean = false,
    val isCardSelectorOpen: Boolean = false,

    val isDeleteCardDialogOpen: Boolean = false,
    val isDeleteDeckDialogOpen: Boolean = false,
    val isEditDeckNameDialogOpen: Boolean = false,

    val userInput: String? = null,
    val tipText: String = "",

    val lastUpdated: Long = 0,
)

enum class SortType {
    ALPHANUMERICAL,
    DATE_STUDIED,
    DATE_CREATED,

}