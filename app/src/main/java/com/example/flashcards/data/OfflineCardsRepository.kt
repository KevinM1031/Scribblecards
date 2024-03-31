package com.example.flashcards.data

import com.example.flashcards.data.entities.BundleEntity
import com.example.flashcards.data.entities.CardEntity
import com.example.flashcards.data.entities.DeckEntity
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.data.relations.DeckWithCards

class OfflineCardsRepository(private val cardsDao: CardsDao) : CardsRepository {

    override suspend fun insertBundle(bundle: BundleEntity) = cardsDao.insertBundle(bundle)

    override suspend fun insertDeck(deck: DeckEntity) = cardsDao.insertDeck(deck)

    override suspend fun insertCard(card: CardEntity) = cardsDao.insertCard(card)

    override suspend fun updateBundle(bundle: BundleEntity) = cardsDao.updateBundle(bundle)

    override suspend fun updateDeck(deck: DeckEntity) = cardsDao.updateDeck(deck)

    override suspend fun updateCard(card: CardEntity) = cardsDao.updateCard(card)

    override suspend fun deleteBundle(bundle: BundleEntity) = cardsDao.deleteBundle(bundle)

    override suspend fun deleteDeck(deck: DeckEntity) = cardsDao.deleteDeck(deck)

    override suspend fun deleteCard(card: CardEntity) = cardsDao.deleteCard(card)

    override suspend fun getBundle(id: Int): BundleWithDecks = cardsDao.getBundle(id)

    override suspend fun getAllBundles(): List<BundleWithDecks> = cardsDao.getAllBundles()

    override suspend fun getDeck(id: Int): DeckWithCards = cardsDao.getDeck(id)

    override suspend fun getAllDecks(): List<DeckWithCards> = cardsDao.getAllDecks()
}