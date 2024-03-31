package com.example.flashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flashcards.data.entities.BundleEntity
import com.example.flashcards.data.entities.CardEntity
import com.example.flashcards.data.entities.DeckEntity

@Database(entities = [BundleEntity::class, DeckEntity::class, CardEntity::class], version = 1, exportSchema = false)
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