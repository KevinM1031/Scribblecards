package com.example.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashcards.data.Settings

@Entity(tableName = "savedSettings")
data class SavedSettings (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var masteryStandard: Int = Settings.DEFAULT_MASTERY_STANDARD,
    var priorityDeckMasteryLevel: Float = Settings.DEFAULT_PRIORITY_DECK_MASTERY_LEVEL,
    var timeImpactCoefficient: Float = Settings.DEFAULT_TIME_IMPACT_COEFFICIENT,
    var priorityDeckRefreshTime: Long = Settings.DEFAULT_PRIORITY_DECK_REFRESH_TIME,
    var isMasteryAffectedByTime: Boolean = Settings.DEFAULT_IS_MASTERY_AFFECTED_BY_TIME,
    var language: Language = Settings.DEFAULT_LANGUAGE,
)

enum class Language(val localeString: String, val displayName: String) {
    KOR(localeString = "kr", displayName = "한국어"),
    ENG(localeString = "", displayName = "English"),
    JPN(localeString = "jp", displayName = "日本語"),
    UNSET(localeString = "unset", displayName = "Default")
}