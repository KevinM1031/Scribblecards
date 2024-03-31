package com.example.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.flashcards.data.entities.BundleEntity
import com.example.flashcards.data.entities.CardEntity
import com.example.flashcards.data.entities.DeckEntity
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.data.relations.DeckWithCards

@Dao
interface CardsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBundle(bundle: BundleEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeck(deck: DeckEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCard(card: CardEntity)

    @Update
    suspend fun updateBundle(bundle: BundleEntity)

    @Update
    suspend fun updateDeck(deck: DeckEntity)

    @Update
    suspend fun updateCard(card: CardEntity)

    @Delete
    suspend fun deleteBundle(bundle: BundleEntity)

    @Delete
    suspend fun deleteDeck(deck: DeckEntity)

    @Delete
    suspend fun deleteCard(card: CardEntity)

    @Transaction
    @Query("SELECT * FROM bundles WHERE id = :id")
    suspend fun getBundle(id: Int): BundleWithDecks

    @Transaction
    @Query("SELECT * FROM bundles")
    suspend fun getAllBundles(): List<BundleWithDecks>

    @Transaction
    @Query("SELECT * FROM decks WHERE id = :id AND bundleId = -1")
    suspend fun getDeck(id: Int): DeckWithCards

    @Transaction
    @Query("SELECT * FROM decks WHERE bundleId = -1")
    suspend fun getAllDecks(): List<DeckWithCards>
}