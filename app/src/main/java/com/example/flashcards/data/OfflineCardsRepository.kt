package com.example.flashcards.data

import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.data.relations.BundleWithDecksWithCards
import com.example.flashcards.data.relations.DeckWithCards

class OfflineCardsRepository(private val cardsDao: CardsDao) : CardsRepository {

    override suspend fun insertBundle(bundle: Bundle): Long = cardsDao.insertBundle(bundle)

    override suspend fun insertDeck(deck: Deck): Long = cardsDao.insertDeck(deck)

    override suspend fun insertDeckToBundle(deck: Deck, bundleId: Long): Long = run {
        deck.bundleId = bundleId
        cardsDao.insertDeck(deck)
    }

    override suspend fun insertCard(card: Card): Long = cardsDao.insertCard(card)

    override suspend fun insertCardToDeck(card: Card, deckId: Long): Long = run {
        card.deckId = deckId
        cardsDao.insertCard(card)
    }

    override suspend fun updateBundle(bundle: Bundle) = cardsDao.updateBundle(bundle)

    override suspend fun updateDeck(deck: Deck) = cardsDao.updateDeck(deck)

    override suspend fun updateCard(card: Card) = cardsDao.updateCard(card)

    override suspend fun deleteBundle(bundle: Bundle) = cardsDao.deleteBundle(bundle)

    override suspend fun deleteDeck(deck: Deck) = cardsDao.deleteDeck(deck)

    override suspend fun deleteCard(card: Card) = cardsDao.deleteCard(card)

    override suspend fun getBundle(id: Long): Bundle = cardsDao.getBundle(id)

    override suspend fun getAllBundles(): List<Bundle> = cardsDao.getAllBundles()

    override suspend fun getBundleWithDecks(id: Long): BundleWithDecks = cardsDao.getBundleWithDecks(id)

    override suspend fun getAllBundlesWithDecks(): List<BundleWithDecks> = cardsDao.getAllBundlesWithDecks()

    override suspend fun getBundleWithDecksWithCards(id: Long): BundleWithDecksWithCards = cardsDao.getBundleWithDecksWithCards(id)

    override suspend fun getAllBundlesWithDecksWithCards(): List<BundleWithDecksWithCards> = cardsDao.getAllBundlesWithDecksWithCards()

    override suspend fun getDeck(id: Long): Deck = cardsDao.getDeck(id)

    override suspend fun getAllDecks(): List<Deck> = cardsDao.getAllDecks()

    override suspend fun getDeckNotInBundle(id: Long): Deck = cardsDao.getDeckNotInBundle(id)

    override suspend fun getAllDecksNotInBundle(): List<Deck> = cardsDao.getAllDecksNotInBundle()

    override suspend fun getDeckWithCards(id: Long): DeckWithCards = cardsDao.getDeckWithCards(id)

    override suspend fun getAllDecksWithCards(): List<DeckWithCards> = cardsDao.getAllDecksWithCards()

    override suspend fun getDeckNotInBundleWithCards(id: Long): DeckWithCards = cardsDao.getDeckNotInBundleWithCards(id)

    override suspend fun getAllDecksWithCardsNotInBundle(): List<DeckWithCards> = cardsDao.getAllDecksNotInBundleWithCards()
}