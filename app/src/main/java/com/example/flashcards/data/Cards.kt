package com.example.flashcards.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class Cards(
    open val name: String,
    open val decks: List<Deck>? = null,
    open val cards: List<Card>? = null,
) {
    fun toBundle(): Bundle {
        if (decks == null) throw NullPointerException("List of Decks for the Bundle was never assigned")
        else return Bundle(name, decks!!)
    }

    fun toDeck(): Deck {
        if (cards == null) throw NullPointerException("List of Cards for the Deck was never assigned")
        else return Deck(name, cards!!)
    }

    fun isBundle(): Boolean {
        return decks != null
    }
}

data class Bundle(
    override val name: String = "Bundle",
    override val decks: List<Deck> = listOf(),
) : Cards(name = name, decks = decks)

data class Deck(
    override val name: String = "Deck",
    override val cards: List<Card> = listOf(),
) : Cards(name = name, cards = cards)

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