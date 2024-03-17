package com.example.flashcards.data

data class MenuUiState (
    val decks: List<Deck> = listOf<Deck>(),
    val bundles: List<Bundle> = listOf<Bundle>(),
    val currentBundleIndex: Int? = null,
    val currentDeckIndex: Int? = null,
    val isBundleOpen: Boolean = false,
    val isCreateOptionsOpen: Boolean = false,
    val isBundleCreatorOpen: Boolean = false,
    val isBundleCreatorDialogOpen: Boolean = false,
    val numSelectedDecks: Int = 0,
    val numSelectedBundles: Int = 0,
    val userInput: String? = null,
    )