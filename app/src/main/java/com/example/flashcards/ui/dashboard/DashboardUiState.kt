package com.example.flashcards.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks

data class DashboardUiState(
    val decks: List<Deck> = listOf(),
    val bundles: List<BundleWithDecks> = listOf(),

    val currentBundleIndex: Int? = null,
    val currentDeckIndex: Int? = null,

    val sortType: SortType = SortType.MASTERY,

    val isDragging: Boolean = false,
    val dragPosition: Offset = Offset.Zero,

    val numSelectedBundles: Int = 0,
    val numSelectedDecks: Int = 0,
    val numSelectedCards: Int = 0,

    val isDeckEnabled: Boolean = true,
    val isBundleOpen: Boolean = false,
    val isCreateOptionsOpen: Boolean = false,
    val isBundleCreatorOpen: Boolean = false,
    val isBundleSelectorOpen: Boolean = false,
    val isBundleCreatorDialogOpen: Boolean = false,
    val isRemoveDeckFromBundleUiOpen: Boolean = false,
    val isEditBundleNameDialogOpen: Boolean = false,
    val isDeckCreatorDialogOpen: Boolean = false,
    val isTipOpen: Boolean = false,

    val isBundleCloseAnimRequested: Boolean = false,
    val isCreateOptionsCloseAnimRequested: Boolean = false,
    val isOpenAnimRequested: Boolean = false,

    val userInput: String? = null,
    val tipText: String = "",

    val lastUpdated: Long = 0,
)

enum class SortType {
    ALPHANUMERICAL,
    MASTERY,
}