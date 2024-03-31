package com.example.flashcards.data

import com.example.flashcards.data.entities.BundleEntity
import com.example.flashcards.data.entities.CardEntity
import com.example.flashcards.data.entities.DeckEntity
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.data.relations.DeckWithCards

interface CardsRepository {

    suspend fun insertBundle(bundle: BundleEntity)

    suspend fun insertDeck(deck: DeckEntity)

    suspend fun insertCard(card: CardEntity)

    suspend fun updateBundle(bundle: BundleEntity)

    suspend fun updateDeck(deck: DeckEntity)

    suspend fun updateCard(card: CardEntity)

    suspend fun deleteBundle(bundle: BundleEntity)

    suspend fun deleteDeck(deck: DeckEntity)

    suspend fun deleteCard(card: CardEntity)

    suspend fun getBundle(id: Int): BundleWithDecks

    suspend fun getAllBundles(): List<BundleWithDecks>

    suspend fun getDeck(id: Int): DeckWithCards

    suspend fun getAllDecks(): List<DeckWithCards>
}
