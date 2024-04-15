package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var deckId: Long = -1, //-1 if not in deck
    var questionText: String,
    var answerText: String,
    var hintText: String? = null,
    var exampleText: String? = null,
    var numStudied: Int = 0,
    var numPerfect: Int = 0,
    var isFavorite: Boolean = false,
) : Selectable() {

    companion object {
        const val MASTERY_STANDARD = 5
    }

    fun applySessionResults(isPerfect: Boolean) {
        if (isPerfect) {
            numPerfect = (++numPerfect).coerceAtMost(MASTERY_STANDARD)
        }
        numStudied = (++numStudied).coerceAtMost(MASTERY_STANDARD)
    }

    fun getMasteryLevel(): Float {
        numStudied = numStudied.coerceAtMost(MASTERY_STANDARD)
        numPerfect = numPerfect.coerceAtMost(MASTERY_STANDARD)
        return numPerfect.toFloat() / MASTERY_STANDARD
    }

    fun clearHistory() {
        numStudied = 0
        numPerfect = 0
    }
}