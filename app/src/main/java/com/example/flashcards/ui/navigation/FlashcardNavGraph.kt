package com.example.flashcards.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.ui.menu.CreateCardScreen
import com.example.flashcards.ui.menu.DashboardScreen
import com.example.flashcards.ui.menu.DeckScreen
import com.example.flashcards.ui.menu.ImportCardsScreen
import com.example.flashcards.ui.menu.MainMenuScreen
import com.example.flashcards.ui.session.SessionScreen
import com.example.flashcards.ui.session.SummaryScreen

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
fun FlashcardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = FlashcardScreen.MainMenu.name,
        modifier = modifier
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
                onDeckButtonClicked = { navController.navigate("${FlashcardScreen.Deck.name}/$it") },
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.Deck.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            DeckScreen(
                onBackButtonClicked = { navController.navigateUp() },
                onStartButtonClicked = { navController.navigate("${FlashcardScreen.Session.name}/$it") },
                onCreateButtonClicked = { navController.navigate(FlashcardScreen.CreateCard.name) },
                onImportButtonClicked = { navController.navigate(FlashcardScreen.ImportCards.name) },
            )
        }
        composable(route = FlashcardScreen.CreateCard.name) {
            CreateCardScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(route = FlashcardScreen.ImportCards.name) {
            ImportCardsScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.Session.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            SessionScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
    }
}
