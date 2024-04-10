package com.example.flashcards.ui.menu

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks

data class DashboardUiState(
    val decks: List<Deck> = listOf(),
    val bundles: List<BundleWithDecks> = listOf(),

    val currentBundleIndex: Int? = null,
    val currentDeckIndex: Int? = null,

    val numSelectedBundles: Int = 0,
    val numSelectedDecks: Int = 0,
    val numSelectedCards: Int = 0,

    val isCreateOptionsOpen: Boolean = false,
    val isBundleCreatorOpen: Boolean = false,
    val isBundleCreatorDialogOpen: Boolean = false,
    val isRemoveDeckFromBundleUiOpen: Boolean = false,
    val isEditBundleNameDialogOpen: Boolean = false,
    val isDeckCreatorDialogOpen: Boolean = false,
    val isTipOpen: Boolean = false,
    val isSessionOptionsOpen: Boolean = false,
    val isCardSelectorOpen: Boolean = false,

    val userInput: String? = null,
    val tipText: String = "",

    val lastUpdated: Long = 0,
)