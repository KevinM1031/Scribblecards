package com.example.flashcards.data

import java.util.Date

data class SessionUiState(
    val deck: Deck = Deck(listOf(), DeckData("Deck", Date(0), Date(0), Date(0))),

    val currentCardIndex: Int = 0,

    val isFlipped: Boolean = false,
    val isHintShown: Boolean = false,
    val isExampleShown: Boolean = false,

    val activeCards: List<Int> = listOf(),
    val usedCards: List<Int> = listOf(),

    val lastUpdated: Long = 0,
)