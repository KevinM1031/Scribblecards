package com.example.flashcards.data.relations

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Deck
import com.example.flashcards.data.entities.BundleEntity
import com.example.flashcards.data.entities.DeckEntity

data class BundleWithDecks(
    @Embedded val bundle: BundleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "bundleId",
    )
    val decks: List<DeckEntity>
) {
    fun toBundle(): Bundle {
        val decks = mutableListOf<Deck>()
        for (deck in this.decks) {
            decks.add(deck.toDeck())
        }
        return Bundle(
            name = bundle.name,
            decks = decks,
            entity = bundle,
        )
    }
}