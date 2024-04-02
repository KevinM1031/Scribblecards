package com.example.flashcards.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.Selectable

data class BundleWithDecksWithCards(
    @Embedded val bundle: Bundle,
    @Relation(
        //entity = Deck::class,
        parentColumn = "id",
        entityColumn = "bundleId",
    )
    //val decks: List<DeckWithCards>
    val decks: List<Deck>
) {
}