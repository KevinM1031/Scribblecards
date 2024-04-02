package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var bundleId: Long = -1, //-1 if not in bundle
    var name: String = "Deck",
    var dateCreated: Long = 0,
    var dateUpdated: Long = 0,
    var dateStudied: Long = 0,

    var showHints: Boolean = false,
    var showExamples: Boolean = false,
    var flipQnA: Boolean = false,
    var doubleDifficulty: Boolean = false,

    var masteryLevel: Float = 0f,
    var numSelected: Int = 0,
) : Selectable() {

}