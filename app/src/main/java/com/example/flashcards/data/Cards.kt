package com.example.flashcards.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class CardCollection(
    open val name: String,
    private var isSelected: Boolean = false,
) {

    fun toggleSelection() {
        isSelected = !isSelected
    }

    fun deselect() {
        isSelected = false
    }

    fun isSelected(): Boolean {
        return isSelected
    }
}

data class Bundle(
    override val name: String = "Bundle",
    val decks: List<Deck> = listOf(),
) : CardCollection(name = name)

data class Deck(
    override val name: String = "Deck",
    val cards: List<Card> = listOf(),
) : CardCollection(name = name)

data class Card(
    val questionText: String,
    val answerText: String,
    val hintText: String? = null,
    val exampleText: String? = null,
) {
    var numStudied by mutableStateOf(0); private set
    var numCorrect by mutableStateOf(0); private set
    var numIncorrect by mutableStateOf(0); private set

    fun markedAsCorrect() {
        numStudied.inc()
        numCorrect.inc()
    }

    fun markedAsIncorrect() {
        numStudied.inc()
        numIncorrect.inc()
    }

    fun clearHistory() {
        numStudied = 0
        numCorrect = 0
        numIncorrect = 0
    }
}