package com.example.flashcards.ui.mainMenu

import com.example.flashcards.data.Settings
import com.example.flashcards.data.entities.Language
import com.example.flashcards.data.entities.SavedSettings

data class SettingsUiState(
    val savedSettings: SavedSettings = SavedSettings(),

    val masteryStandard: Int? = null,
    val priorityDeckMasteryLevel: Float? = null,
    val timeImpactCoefficient: Float? = null,
    val priorityDeckRefreshTime: Long? = null,
    val isMasteryAffectedByTime: Boolean? = null,
    val language: Language? = null,

    val tip: String = "",
    val changeMade: Boolean = false,

    val isTipOpen: Boolean = false,
    val isResetDialogOpen: Boolean = false,

    val lastUpdated: Long = 0,
)