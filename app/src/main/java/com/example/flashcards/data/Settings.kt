package com.example.flashcards.data

class Settings {
    companion object {
        private const val DEFAULT_MASTERY_STANDARD = 5
        private const val DEFAULT_PRIORITY_DECK_MASTERY_LEVEL = 0.3f
        private const val DEFAULT_TIME_IMPACT_COEFFICIENT = 1f
        private const val DEFAULT_PRIORITY_DECK_REFRESH_TIME = 3600000.toLong()

        private var masteryStandard: Int = DEFAULT_MASTERY_STANDARD
        private var priorityDeckMasteryLevel: Float = DEFAULT_PRIORITY_DECK_MASTERY_LEVEL
        private var timeImpactCoefficient: Float = DEFAULT_TIME_IMPACT_COEFFICIENT
        private var priorityDeckRefreshTime: Long = DEFAULT_PRIORITY_DECK_REFRESH_TIME

        fun getMasteryStandard(): Int { return masteryStandard }

        fun getPriorityDeckMasteryLevel(): Float { return priorityDeckMasteryLevel }

        fun getTimeImpactCoefficient(): Float { return timeImpactCoefficient }

        fun getPriorityDeckRefreshTime(): Long { return priorityDeckRefreshTime }

    }
}
