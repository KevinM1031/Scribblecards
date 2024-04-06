package com.example.flashcards.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.DeckCardCrossRef
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.entities.Selectable

data class DeckWithCards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckId",
        //associateBy = Junction(DeckCardCrossRef::class),
    )
    val cards: List<Card>,
) {

    init {
        updateValues()
    }

    fun updateValues() {
        updateMasteryLevel()
        updateNumSelected()
    }

    fun updateMasteryLevel() {
        if (cards.isEmpty()) {
            deck.masteryLevel = 0f
        } else {
            var sum = 0f
            for (card in cards) {
                sum += card.getMasteryLevel()
            }
            deck.masteryLevel = sum / cards.size
        }
    }

    fun updateNumSelected() {
        if (cards.isEmpty()) {
            deck.numSelected = 0
        } else {
            var num = 0
            for (card in cards) {
                if (card.isSelected) num++
            }
            deck.numSelected = num
        }
    }
}