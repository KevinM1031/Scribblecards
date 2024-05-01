package com.example.flashcards.ui.mainMenu

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.data.CardsRepository
import com.example.flashcards.data.Settings
import com.example.flashcards.data.entities.SavedSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val cardsRepository: CardsRepository,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(MainMenuUiState())
    val uiState: StateFlow<MainMenuUiState> = _uiState.asStateFlow()

    fun softReset() {
        viewModelScope.launch {
            countProprityDecks()
        }
    }

    fun reset() {
        softReset()
        closeCloseDialog()
    }

    suspend fun loadSettings(context: Context, configuration: Configuration) {
        val savedSettings = cardsRepository.getAllSavedSettings()
        if (savedSettings.isEmpty()) {
            cardsRepository.insertSavedSettings(SavedSettings())
        }
        Settings.update(cardsRepository.getAllSavedSettings().first(), context, configuration)
        reset()
    }

    /**
     * WARNING - expensive function
     */
    suspend fun countProprityDecks() {
        val decksWithCards = cardsRepository.getAllDecksWithCards()
        for (deck in decksWithCards) {
            deck.updateMasteryLevel()
            cardsRepository.updateDeck(deck.deck)
        }

        Log.d("", "${decksWithCards[0].deck.masteryLevel} <= ${Settings.priorityDeckMasteryLevel}")

        val priorityDecksWithCards = decksWithCards.filter {
            it.deck.masteryLevel <= Settings.priorityDeckMasteryLevel &&
            it.cards.isNotEmpty() &&
            System.currentTimeMillis() - it.deck.dateStudied > Settings.priorityDeckRefreshTime
        }.sortedBy { it.deck.masteryLevel }

        _uiState.update { currentState ->
            currentState.copy(
                numPriorityDecks = priorityDecksWithCards.size,
            )
        }
    }

    fun openCloseDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isCloseDialogOpen = true,
            )
        }
    }
    fun closeCloseDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isCloseDialogOpen = false,
            )
        }
    }


    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }
}