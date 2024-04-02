package com.example.flashcards.ui.session

import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.DeckWithCards

data class SessionUiState(
    val deck: DeckWithCards = DeckWithCards(Deck(), listOf()),

    val currentCardIndex: Int = 0,

    val isFlipped: Boolean = false,
    val isHintShown: Boolean = false,
    val isExampleShown: Boolean = false,
    val isHistoryShown: Boolean = false,
    val isQuitDialogOpen: Boolean = false,
    val isRestartDialogOpen: Boolean = false,
    val isSessionCompleted: Boolean = false,
    val flipContent: Boolean = false,

    val activeCards: List<Int> = listOf(),
    val usedCards: List<Int> = listOf(),
    val completedCards: List<Int> = listOf(),
    val cardHistory: Map<Int, CardHistory> = mapOf(),

    val lastUpdated: Long = 0,
)

class CardHistory {
    private val MAX_HISTORY_COUNT = 5
    private val history: ArrayDeque<Boolean> = ArrayDeque()

    fun getHistory(): List<Boolean> {
        return history.toList()
    }

    fun add(data: Boolean) {
        history.addLast(data)
        if (history.size > MAX_HISTORY_COUNT) {
            history.removeFirst()
        }
    }

    fun clearRecent() {
        history.removeLast()
    }

    fun isComplete(isDoubleDifficulty: Boolean): Boolean {
        return if (isDoubleDifficulty) {
            history.size >= 2 && history.last() && history[history.size-2]
        } else {
            !history.isEmpty() && history.last()
        }
    }

    fun isPerfect(): Boolean {
        val history = getHistory()
        return history.isNotEmpty() && !history.contains(false)
    }
}