package com.example.flashcards.data

data class MenuUiState(
    val decks: List<Deck> = listOf(),
    val bundles: List<Bundle> = listOf(),

    val currentBundleIndex: Int? = null,
    val currentDeckIndex: Int? = null,

    val numSelectedBundles: Int = 0,
    val numSelectedDecks: Int = 0,
    val numSelectedCards: Int = 0,

    val isBundleOpen: Boolean = false,
    val isCreateOptionsOpen: Boolean = false,
    val isBundleCreatorOpen: Boolean = false,
    val isBundleCreatorDialogOpen: Boolean = false,
    val isTipOpen: Boolean = false,
    val isSessionOptionsOpen: Boolean = true,

    val currentDeck: Deck? = null,
    val userInput: String? = null,
    val tipText: String = "",
)