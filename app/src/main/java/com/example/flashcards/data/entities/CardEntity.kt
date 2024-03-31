package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashcards.data.Card

@Entity(tableName = "cards")
data class CardEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deckId: Int = -1, //-1 if not in deck
    val questionText: String,
    val answerText: String,
    val hintText: String? = null,
    val exampleText: String? = null,
    var numStudied: Int = 0,
    var numPerfect: Int = 0,
    var numCorrect: Int = 0,
    var numIncorrect: Int = 0,
) {
    fun toCard(): Card {
        return Card(
            questionText = questionText,
            answerText = answerText,
            hintText = hintText,
            exampleText = exampleText,
            numStudied = numStudied,
            numPerfect = numPerfect,
            numCorrect = numCorrect,
            numIncorrect = numIncorrect,
            entity = this,
        )
    }
}