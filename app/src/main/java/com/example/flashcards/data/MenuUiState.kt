package com.example.flashcards.data

data class MenuUiState (
    val cards: List<Cards>,
    val currentBundleIndex: Int? = null,
    val currentDeckIndex: Int? = null,
)