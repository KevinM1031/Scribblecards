package com.example.flashcards.data.entities

import android.util.Log

abstract class Selectable(
    var isSelected: Boolean = false, ) {

    open fun toggleSelection() {
        isSelected = !isSelected
    }

    open fun select() {
        isSelected = true
    }

    open fun deselect() {
        isSelected = false
    }
}