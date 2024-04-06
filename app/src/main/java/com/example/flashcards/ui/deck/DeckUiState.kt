package com.example.flashcards.ui.deck

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.DeckWithCards

data class DeckUiState(
    val deck: DeckWithCards = DeckWithCards(Deck(), listOf()),
    val param: Long = -1,

    val numSelectedCards: Int = 0,

    val isTipOpen: Boolean = false,
    val isSessionOptionsOpen: Boolean = false,
    val isCardSelectorOpen: Boolean = false,

    val isDeleteCardDialogOpen: Boolean = false,
    val isDeleteDeckDialogOpen: Boolean = false,


    val userInput: String? = null,
    val tipText: String = "",

    val lastUpdated: Long = 0,
)