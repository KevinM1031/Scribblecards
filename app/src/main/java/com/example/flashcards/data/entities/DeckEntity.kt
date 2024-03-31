package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashcards.data.Deck
import com.example.flashcards.data.DeckData

@Entity(tableName = "decks")
data class DeckEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bundleId: Int = -1, //-1 if not in bundle
    val name: String = "Deck",
    var dateCreated: Long = 0,
    var dateUpdated: Long = 0,
    var dateStudied: Long = 0,

    var showHints: Boolean = false,
    var showExamples: Boolean = false,
    var flipQnA: Boolean = false,
    var doubleDifficulty: Boolean = false,

    var masteryLevel: Float = 0f,
) {
    fun toDeck(): Deck {
        return Deck(
            data = DeckData(
                name = name,
                dateCreated = dateCreated,
                dateUpdated = dateUpdated,
                dateStudied = dateStudied,
                showHints = showHints,
                showExamples = showExamples,
                flipQnA = flipQnA,
                doubleDifficulty = doubleDifficulty,
                masteryLevel = masteryLevel
            ),
            cards = listOf(),
            entity = this,
        )
    }
}