package com.example.flashcards.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.ui.editor.CreateCardScreen
import com.example.flashcards.ui.menu.DashboardScreen
import com.example.flashcards.ui.deck.DeckScreen
import com.example.flashcards.ui.editor.ImportCardsScreen
import com.example.flashcards.ui.menu.MainMenuScreen
import com.example.flashcards.ui.session.SessionScreen

enum class FlashcardScreen() {
    MainMenu,
    Dashboard,
    Deck,
    CreateCard,
    ImportCards,
    Session,
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
                onBackButtonClicked = { navController.navigate(FlashcardScreen.MainMenu.name) },
            )
        }
        composable(
            route = "${FlashcardScreen.Deck.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            DeckScreen(
                onBackButtonClicked = { navController.navigate(FlashcardScreen.Dashboard.name) },
                onStartButtonClicked = { navController.navigate("${FlashcardScreen.Session.name}/$it") },
                onCreateButtonClicked = { navController.navigate("${FlashcardScreen.CreateCard.name}/$it") },
                onImportButtonClicked = { navController.navigate("${FlashcardScreen.ImportCards.name}/$it") },
            )
        }
        composable(
            route = "${FlashcardScreen.CreateCard.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            CreateCardScreen(
                onBackButtonClicked = { navController.navigate("${FlashcardScreen.Deck.name}/$it") },
            )
        }
        composable(route = "${FlashcardScreen.ImportCards.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            ImportCardsScreen(
                onBackButtonClicked = { navController.navigate("${FlashcardScreen.Deck.name}/$it") },
            )
        }
        composable(
            route = "${FlashcardScreen.Session.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            SessionScreen(
                onQuit = { navController.navigate("${FlashcardScreen.Deck.name}/$it") }
            )
        }
    }
}
