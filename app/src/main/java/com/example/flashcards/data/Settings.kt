package com.example.flashcards.data

class Settings {
    companion object {
        private const val DEFAULT_MASTERY_STANDARD = 5
        private const val DEFAULT_PRIORITY_DECK_THRESHOLD = 0.3f

        private var masteryStandard: Int = DEFAULT_MASTERY_STANDARD
        private var priorityDeckThreshold: Float = DEFAULT_PRIORITY_DECK_THRESHOLD

        fun getMasteryStandard(): Int { return masteryStandard }

        fun getPriorityDeckThreshold(): Float { return priorityDeckThreshold }

    }
}
