package com.example.flashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.DeckCardCrossRef
import com.example.flashcards.data.entities.Deck

@Database(entities = [Bundle::class, Deck::class, Card::class, DeckCardCrossRef::class], version = 4, exportSchema = false)
abstract class CardsDatabase : RoomDatabase() {

    abstract fun cardsDao(): CardsDao

    companion object {

        @Volatile
        private var Instance: CardsDatabase? = null

        fun getDatabase(context: Context): CardsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CardsDatabase::class.java, "cards_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}