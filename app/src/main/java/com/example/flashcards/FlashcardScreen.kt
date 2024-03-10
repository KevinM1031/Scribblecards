package com.example.flashcards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.flashcards.ui.MenuViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.ui.DashboardScreen
import com.example.flashcards.ui.DeckScreen
import com.example.flashcards.ui.MainMenuScreen

enum class FlashcardScreen() {
    MainMenu,
    Dashboard,
    Deck,
    CreateCard,
    ImportCards,
    Session,
    Summary,
    Tutorial,
    Settings,
}

@Composable
fun FlashcardApp(
    viewModel: MenuViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FlashcardScreen.valueOf(backStackEntry?.destination?.route ?: FlashcardScreen.MainMenu.name)
    
    NavHost(
        navController = navController,
        startDestination = FlashcardScreen.MainMenu.name
    ) {
        composable(route = FlashcardScreen.MainMenu.name) {
            MainMenuScreen(
                onCardButtonClicked = { navController.navigate(FlashcardScreen.Dashboard.name) },
                onTutorialButtonClicked = { navController.navigate(FlashcardScreen.Tutorial.name) },
                onSettingsButtonClicked = { navController.navigate(FlashcardScreen.Settings.name) }
            )
        }
        composable(route = FlashcardScreen.Dashboard.name) {
            DashboardScreen(
                viewModel,
                onDeckButtonClicked = { navController.navigate(FlashcardScreen.Deck.name) },
            )
        }
        composable(route = FlashcardScreen.Deck.name) {
            DeckScreen(
                viewModel,
                onStartButtonClicked = { navController.navigate(FlashcardScreen.Session.name) },
                onCreateButtonClicked = { navController.navigate(FlashcardScreen.CreateCard.name) },
                onImportButtonClicked = { navController.navigate(FlashcardScreen.ImportCards.name) },
                )
        }
    }
}