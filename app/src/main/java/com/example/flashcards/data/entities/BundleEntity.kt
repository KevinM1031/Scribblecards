package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bundles")
data class BundleEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "Deck",
)