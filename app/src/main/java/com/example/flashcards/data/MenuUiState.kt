package com.example.flashcards.data

data class MenuUiState (
    val cardsList: List<Cards>,
    val currentBundleIndex: Int? = null,
    val currentDeckIndex: Int? = null,
)