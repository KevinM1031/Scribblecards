
package com.example.flashcards

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.ui.navigation.FlashcardNavHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlashcardApp(navController: NavHostController = rememberNavController()) {
    FlashcardNavHost(navController = navController)
}