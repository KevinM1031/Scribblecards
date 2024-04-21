package com.example.flashcards.ui.priorityDecks

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks

data class PriorityDecksUiState(
    val decks: List<Deck>? = null,

    val lastUpdated: Long = 0,
)