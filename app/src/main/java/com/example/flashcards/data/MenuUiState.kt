package com.example.flashcards.data

data class MenuUiState (
    val currentBundle: Bundle? = DataSource.cards[1].toBundle(),
    val currentDeck: Deck? = null,
)