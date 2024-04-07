package com.example.flashcards.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flashcards.FlashcardApplication
import com.example.flashcards.ui.menu.DashboardViewModel
import com.example.flashcards.ui.deck.DeckViewModel
import com.example.flashcards.ui.createCard.CreateCardViewModel
import com.example.flashcards.ui.editCard.EditCardViewModel
import com.example.flashcards.ui.session.SessionViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DashboardViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
            )
        }
        initializer {
            DeckViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            SessionViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            CreateCardViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            EditCardViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
    }
}

fun CreationExtras.flashcardApplication(): FlashcardApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlashcardApplication)
