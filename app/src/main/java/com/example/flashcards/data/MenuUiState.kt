package com.example.flashcards.data

data class MenuUiState(
    val decks: List<Deck> = listOf(),
    val bundles: List<Bundle> = listOf(),

    val currentBundleIndex: Int? = null,
    val currentDeckIndex: Int? = 1,

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