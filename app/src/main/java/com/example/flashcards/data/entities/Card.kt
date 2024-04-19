package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashcards.data.Settings
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
        fun calculateMasteryLevel(numStudied: Int, numPerfect: Int, isAffectedByTime: Boolean = true, millisSinceStudied: Long = 0): Float {
            return if (numStudied == 0) {
                0f
            } else if (isAffectedByTime) {
                (numStudied.coerceAtMost(Settings.getMasteryStandard()).toFloat() / Settings.getMasteryStandard()) / (millisSinceStudied.toFloat() / (3600000f * numStudied.coerceAtMost(Settings.getMasteryStandard())).pow(numPerfect.coerceAtMost(Settings.getMasteryStandard()) + 1) + 1f)
            } else {
                numPerfect.coerceAtMost(Settings.getMasteryStandard()).toFloat() / Settings.getMasteryStandard()
            }
        }
    }

    fun applySessionResults(isPerfect: Boolean) {
        if (isPerfect) {
            numPerfect = (++numPerfect).coerceAtMost(Settings.getMasteryStandard())
        } else if (numStudied >= Settings.getMasteryStandard()) {
            numPerfect--
        }
        numStudied = (++numStudied).coerceAtMost(Settings.getMasteryStandard())
    }

    fun getMasteryLevel(isAffectedByTime: Boolean = true, millisSinceStudied: Long = 0): Float {
        numStudied = numStudied.coerceAtMost(Settings.getMasteryStandard())
        numPerfect = numPerfect.coerceAtMost(Settings.getMasteryStandard())
        return calculateMasteryLevel(numStudied, numPerfect, isAffectedByTime, millisSinceStudied)
    }

    fun clearHistory() {
        numStudied = 0
        numPerfect = 0
    }
}