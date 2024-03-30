package com.example.flashcards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.flashcards.ui.MenuViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flashcards.ui.CreateCardScreen
import com.example.flashcards.ui.DashboardScreen
import com.example.flashcards.ui.DeckScreen
import com.example.flashcards.ui.ImportCardsScreen
import com.example.flashcards.ui.MainMenuScreen
import com.example.flashcards.ui.SessionScreen
import com.example.flashcards.ui.SessionViewModel
import com.example.flashcards.ui.SummaryScreen

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
    menuViewModel: MenuViewModel = viewModel(),
    sessionViewModel: SessionViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = FlashcardScreen.MainMenu.name
    ) {
        composable(route = FlashcardScreen.MainMenu.name) {
            MainMenuScreen(
                onCardButtonClicked = { navController.navigate(FlashcardScreen.Dashboard.name) },
                onTutorialButtonClicked = { navController.navigate(FlashcardScreen.Tutorial.name) },
                onSettingsButtonClicked = { navController.navigate(FlashcardScreen.Settings.name) },
            )
        }
        composable(route = FlashcardScreen.Dashboard.name) {
            DashboardScreen(
                menuViewModel,
                onDeckButtonClicked = { navController.navigate(FlashcardScreen.Deck.name) },
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(route = FlashcardScreen.Deck.name) {
            DeckScreen(
                menuViewModel,
                onBackButtonClicked = { navController.navigateUp() },
                onStartButtonClicked = {navController.navigate("${FlashcardScreen.Session.name}/$it") },
                onCreateButtonClicked = { navController.navigate(FlashcardScreen.CreateCard.name) },
                onImportButtonClicked = { navController.navigate(FlashcardScreen.ImportCards.name) },
            )
        }
        composable(route = FlashcardScreen.CreateCard.name) {
            CreateCardScreen(
                menuViewModel,
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(route = FlashcardScreen.ImportCards.name) {
            ImportCardsScreen(
                menuViewModel,
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.Session.name}/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.StringType },
            )
        ) {
            val param = it.arguments?.getString("index") ?: ""
            SessionScreen(
                sessionViewModel,
                param,
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(route = FlashcardScreen.Summary.name) {
            SummaryScreen(
                sessionViewModel,
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
    }
}