package com.example.flashcards.data

import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.data.relations.BundleWithDecksWithCards
import com.example.flashcards.data.relations.DeckWithCards

interface CardsRepository {

    suspend fun insertBundle(bundle: Bundle): Long

    suspend fun insertDeck(deck: Deck): Long

    suspend fun insertDeckToBundle(deck: Deck, bundleId: Long): Long

    suspend fun insertCard(card: Card): Long

    suspend fun insertCardToDeck(card: Card, deckId: Long): Long

    suspend fun updateBundle(bundle: Bundle)

    suspend fun updateDeck(deck: Deck)

    suspend fun updateCard(card: Card)

    suspend fun deleteBundle(bundle: Bundle)

    suspend fun deleteDeck(deck: Deck)

    suspend fun deleteCard(card: Card)

    suspend fun getBundle(id: Long): Bundle

    suspend fun getAllBundles(): List<Bundle>

    suspend fun getBundleWithDecks(id: Long): BundleWithDecks

    suspend fun getAllBundlesWithDecks(): List<BundleWithDecks>

    suspend fun getBundleWithDecksWithCards(id: Long): BundleWithDecksWithCards

    suspend fun getAllBundlesWithDecksWithCards(): List<BundleWithDecksWithCards>

    suspend fun getDeck(id: Long): Deck

    suspend fun getAllDecks(): List<Deck>

    suspend fun getDeckNotInBundle(id: Long): Deck

    suspend fun getAllDecksNotInBundle(): List<Deck>

    suspend fun getDeckWithCards(id: Long): DeckWithCards

    suspend fun getAllDecksWithCards(): List<DeckWithCards>

    suspend fun getDeckNotInBundleWithCards(id: Long): DeckWithCards

    suspend fun getAllDecksWithCardsNotInBundle(): List<DeckWithCards>
}
