package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bundles")
data class Bundle (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "Bundle",

    ): Selectable() {

    }