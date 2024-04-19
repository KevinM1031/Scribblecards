package com.example.flashcards.ui.editCard

import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck

data class EditCardUiState(
    val card: Card = Card(questionText = "", answerText = ""),
    val param: Long = -1,

    val questionTextInput: String = "",
    val answerTextInput: String = "",
    val hintTextInput: String = "",
    val exampleTextInput: String = "",

    val clearCardHistory: Boolean = false,

    val lastUpdated: Long = 0,
)