package com.example.flashcards.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["id", "deckId"])
data class DeckCardCrossRef(
    val id: Long,
    val deckId: Long
)