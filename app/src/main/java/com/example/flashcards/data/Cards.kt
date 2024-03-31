package com.example.flashcards.data

import com.example.flashcards.data.entities.BundleEntity
import com.example.flashcards.data.entities.CardEntity
import com.example.flashcards.data.entities.DeckEntity

abstract class Selectable(
    private var isSelected: Boolean = false,
    ) {

    open fun toggleSelection() {
        isSelected = !isSelected
    }

    open fun select() {
        isSelected = true
    }

    open fun deselect() {
        isSelected = false
    }

    open fun isSelected(): Boolean {
        return isSelected
    }
}

data class Bundle(
    val name: String = "Bundle",
    val decks: List<Deck> = listOf(),
    val entity: BundleEntity,

) : Selectable() {

    fun toEntity(): BundleEntity {
        return entity.copy(
            name = name,
        )
    }
}

data class Deck(
    val cards: List<Card> = listOf(),
    val data: DeckData,
    val entity: DeckEntity,

) : Selectable() {

    var numSelected: Int = 0

    init {
        updateValues()
    }

    fun updateValues() {
        updateMasteryLevel()
        updateNumSelected()
    }

    fun updateMasteryLevel() {
        if (cards.isEmpty()) {
            data.masteryLevel = 0f
        } else {
            var sum = 0f
            for (card in cards) {
                sum += card.getMasteryLevel()
            }
            data.masteryLevel = sum / cards.size
        }
    }

    fun updateNumSelected() {
        if (cards.isEmpty()) {
            numSelected = 0
        } else {
            var num = 0
            for (card in cards) {
                if (card.isSelected()) num++
            }
            numSelected = num
        }
    }

    fun toEntity(): DeckEntity {
        return entity.copy(
            name = data.name,
            dateCreated = data.dateCreated,
            dateUpdated = data.dateUpdated,
            dateStudied = data.dateStudied,
            showHints = data.showHints,
            showExamples = data.showExamples,
            flipQnA = data.flipQnA,
            doubleDifficulty = data.doubleDifficulty,
            masteryLevel = data.masteryLevel
        )
    }

    fun toEntity(bundleId: Int): DeckEntity {
        return entity.copy(
            name = data.name,
            bundleId = bundleId,
            dateCreated = data.dateCreated,
            dateUpdated = data.dateUpdated,
            dateStudied = data.dateStudied,
            showHints = data.showHints,
            showExamples = data.showExamples,
            flipQnA = data.flipQnA,
            doubleDifficulty = data.doubleDifficulty,
            masteryLevel = data.masteryLevel
        )
    }
}

data class DeckData(
    val name: String = "Deck",
    var dateCreated: Long,
    var dateUpdated: Long,
    var dateStudied: Long,

    var showHints: Boolean = false,
    var showExamples: Boolean = false,
    var flipQnA: Boolean = false,
    var doubleDifficulty: Boolean = false,

    var masteryLevel: Float = 0f,
)

data class Card(
    val questionText: String,
    val answerText: String,
    val hintText: String? = null,
    val exampleText: String? = null,
    var numStudied: Int = 0,
    var numPerfect: Int = 0,
    var numCorrect: Int = 0,
    var numIncorrect: Int = 0,
    var entity: CardEntity,
) : Selectable() {

    private val MASTERY_STANDARD = 5

    private var isStudying = false

    fun startStudying() {
        if (isStudying) return
        isStudying = true
        clearSessionHistory()
    }

    fun endStudying() {
        if (!isStudying) return
        isStudying = false
        numStudied = (numStudied+1).coerceAtMost(MASTERY_STANDARD)
        if (numIncorrect == 0) {
            numPerfect = (numPerfect+1).coerceAtMost(MASTERY_STANDARD)
        } else if (numStudied == MASTERY_STANDARD) {
            numPerfect = (numPerfect-1).coerceAtLeast(0)
        }
        clearSessionHistory()
    }

    fun quitStudying() {
        if (!isStudying) return
        isStudying = false
        clearSessionHistory()
    }

    fun getMasteryLevel(): Float {
        return numPerfect.toFloat() / MASTERY_STANDARD
    }

    fun markAsCorrect() {
        if (!isStudying) return
        numCorrect.inc()
    }

    fun markAsIncorrect() {
        if (!isStudying) return
        numIncorrect.inc()
    }

    fun clearSessionHistory() {
        numStudied = 0
        numCorrect = 0
    }

    fun clearHistory() {
        clearSessionHistory()
        numStudied = 0
        numPerfect = 0
    }

    fun toEntity(deckId: Int): CardEntity {
        return entity.copy(
            deckId = deckId,
            questionText = questionText,
            answerText = answerText,
            hintText = hintText,
            exampleText = exampleText,
            numStudied = numStudied,
            numPerfect = numPerfect,
            numCorrect = numCorrect,
            numIncorrect = numIncorrect,
        )
    }
}