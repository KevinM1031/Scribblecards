package com.example.flashcards.ui.menu

import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Deck

data class DeckUiState(
    val deck: Deck? = null,

    val numSelectedBundles: Int = 0,
    val numSelectedDecks: Int = 0,
    val numSelectedCards: Int = 0,

    val isCreateOptionsOpen: Boolean = false,
    val isBundleCreatorOpen: Boolean = false,
    val isBundleCreatorDialogOpen: Boolean = false,
    val isTipOpen: Boolean = false,
    val isSessionOptionsOpen: Boolean = false,
    val isCardSelectorOpen: Boolean = false,

    val userInput: String? = null,
    val tipText: String = "",

    val lastUpdated: Long = 0,
)