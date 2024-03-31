package com.example.flashcards.data.relations

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.flashcards.data.Card
import com.example.flashcards.data.Deck
import com.example.flashcards.data.DeckData
import com.example.flashcards.data.entities.CardEntity
import com.example.flashcards.data.entities.DeckEntity

data class DeckWithCards(
    @Embedded val deck: DeckEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckId",
    )
    val cards: List<CardEntity>
) {
    fun toDeck(): Deck {
        val cards = mutableListOf<Card>()
        for (card in this.cards) {
            cards.add(card.toCard())
        }
        return Deck(
            data = DeckData(
                name = deck.name,
                dateCreated = deck.dateCreated,
                dateUpdated = deck.dateUpdated,
                dateStudied = deck.dateStudied,
                showHints = deck.showHints,
                showExamples = deck.showExamples,
                flipQnA = deck.flipQnA,
                doubleDifficulty = deck.doubleDifficulty,
                masteryLevel = deck.masteryLevel
            ),
            cards = cards,
            entity = deck,
        )
    }
}