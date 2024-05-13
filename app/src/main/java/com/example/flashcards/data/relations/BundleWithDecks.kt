package com.example.flashcards.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.Selectable

data class BundleWithDecks(
    @Embedded val bundle: Bundle,
    @Relation(
        parentColumn = "id",
        entityColumn = "bundleId",
    )
    var decks: List<Deck>
) {

    fun sortByName() {
        decks = decks.sortedBy { it.isLocked.toString() + it.name }
    }

    fun sortByMastery() {
        decks = decks.sortedBy { it.masteryLevel + if (it.isLocked) 1f else 0f }
    }
}