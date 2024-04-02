package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

val MASTERY_STANDARD = 5

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
    var numCorrect: Int = 0,
    var numIncorrect: Int = 0,
) : Selectable() {

        var isStudying = false

        fun startStudying() {
            if (isStudying) return
            isStudying = true
            clearSessionHistory()
        }

        fun endStudying() {
            if (!isStudying) return
            isStudying = false
            numStudied = (numStudied+1).coerceAtMost(MASTERY_STANDARD)
            if (numIncorrect == 0) {
                numPerfect = (numPerfect+1).coerceAtMost(MASTERY_STANDARD)
            } else if (numStudied == MASTERY_STANDARD) {
                numPerfect = (numPerfect-1).coerceAtLeast(0)
            }
            clearSessionHistory()
        }

        fun quitStudying() {
            if (!isStudying) return
            isStudying = false
            clearSessionHistory()
        }

        fun getMasteryLevel(): Float {
            return numPerfect.toFloat() / MASTERY_STANDARD
        }

        fun markAsCorrect() {
            if (!isStudying) return
            numCorrect.inc()
        }

        fun markAsIncorrect() {
            if (!isStudying) return
            numIncorrect.inc()
        }

        fun clearSessionHistory() {
            numStudied = 0
            numCorrect = 0
        }

        fun clearHistory() {
            clearSessionHistory()
            numStudied = 0
            numPerfect = 0
        }
}