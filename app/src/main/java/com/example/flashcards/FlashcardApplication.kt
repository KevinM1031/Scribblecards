package com.example.flashcards

import android.app.Application
import com.example.flashcards.data.AppContainer
import com.example.flashcards.data.AppDataContainer

class FlashcardApplication : Application() {


    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}