package com.example.flashcards.ui.editor

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.DeckWithCards

data class CreateCardUiState(
    val deck: Deck = Deck(),
    val param: Long = -1,

    val numSelectedBundles: Int = 0,
    val numSelectedDecks: Int = 0,
    val numSelectedCards: Int = 0,

    val questionTextInput: String = "",
    val answerTextInput: String = "",
    val hintTextInput: String = "",
    val exampleTextInput: String = "",

    val lastUpdated: Long = 0,
)