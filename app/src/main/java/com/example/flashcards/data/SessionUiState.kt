package com.example.flashcards.data

data class SessionUiState(
    val deck: Deck? = null,

    val lastUpdated: Long = 0,
)