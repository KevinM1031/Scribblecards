package com.example.flashcards.ui.mainMenu

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks

data class MainMenuUiState(
    val numPriorityDecks: Int = 0,

    val isCloseDialogOpen: Boolean = false,

    val allCardsBtnAnimRequested: Boolean = false,
    val priorityDecksBtnAnimRequested: Boolean = false,

    val lastUpdated: Long = 0,
)