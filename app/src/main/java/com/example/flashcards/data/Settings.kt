package com.example.flashcards.data

class Settings {
    companion object {
        private const val MASTERY_STANDARD = 5

        private var masteryStandard: Int = MASTERY_STANDARD

        fun getMasteryStandard(): Int { return masteryStandard }

    }
}
