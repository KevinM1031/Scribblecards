package com.example.flashcards.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.ui.createCard.CreateCardScreen
import com.example.flashcards.ui.menu.DashboardScreen
import com.example.flashcards.ui.deck.DeckScreen
import com.example.flashcards.ui.editCard.EditCardScreen
import com.example.flashcards.ui.importCards.ImportCardsScreen
import com.example.flashcards.ui.menu.MainMenuScreen
import com.example.flashcards.ui.session.SessionScreen

enum class FlashcardScreen() {
    MainMenu,
    Dashboard,
    Deck,
    CreateCard,
    EditCard,
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
            BackHandler(true) { navController.navigate(FlashcardScreen.MainMenu.name) }
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
            BackHandler(true) { navController.navigate(FlashcardScreen.Dashboard.name) }
            DeckScreen(
                onBackButtonClicked = { navController.navigate(FlashcardScreen.Dashboard.name) },
                onStartButtonClicked = { navController.navigate("${FlashcardScreen.Session.name}/$it") },
                onCreateCardButtonClicked = { navController.navigate("${FlashcardScreen.CreateCard.name}/$it") },
                onEditCardButtonClicked = { navController.navigate("${FlashcardScreen.EditCard.name}/$it") },
                onImportCardsButtonClicked = { navController.navigate("${FlashcardScreen.ImportCards.name}/$it") },
            )
        }
        composable(
            route = "${FlashcardScreen.CreateCard.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            CreateCardScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.EditCard.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            EditCardScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.ImportCards.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
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
            BackHandler(true) {}
            SessionScreen(
                onQuit = { navController.navigateUp() }
            )
        }
    }
}
