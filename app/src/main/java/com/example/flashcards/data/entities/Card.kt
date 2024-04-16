package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.pow

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

        fun calculateMasteryLevel(numStudied: Int, numPerfect: Int, isAffectedByTime: Boolean = true, millisSinceStudied: Long = 0): Float {
            return if (numStudied == 0) {
                0f
            } else if (isAffectedByTime) {
                (numStudied.toFloat() / MASTERY_STANDARD) / (millisSinceStudied.toFloat() / (3600000f * numStudied).pow(numPerfect + 1) + 1f)
            } else {
                numPerfect.toFloat() / MASTERY_STANDARD
            }
        }
    }

    fun applySessionResults(isPerfect: Boolean) {
        if (isPerfect) {
            numPerfect = (++numPerfect).coerceAtMost(MASTERY_STANDARD)
        } else if (numStudied >= MASTERY_STANDARD) {
            numPerfect--
        }
        numStudied = (++numStudied).coerceAtMost(MASTERY_STANDARD)
    }

    fun getMasteryLevel(isAffectedByTime: Boolean = true, millisSinceStudied: Long = 0): Float {
        numStudied = numStudied.coerceAtMost(MASTERY_STANDARD)
        numPerfect = numPerfect.coerceAtMost(MASTERY_STANDARD)
        return calculateMasteryLevel(numStudied, numPerfect, isAffectedByTime, millisSinceStudied)
    }

    fun clearHistory() {
        numStudied = 0
        numPerfect = 0
    }
}