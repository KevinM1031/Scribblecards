package com.example.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.ui.theme.FlashcardsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashcardsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.dice_1),
            contentDescription = null
        )
        Spacer(Modifier.weight(0.3f))
        Text(
            text = stringResource(R.string.loading_title),
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(2f))
    }
}

@Composable
fun MainMenu() {
    Button(
        onClick = {}
    ) {
        Text(
            text = stringResource(R.string.main_menu_button_cards)
        )
    }

    Button(
        onClick = {}
    ) {
        Text(
            text = stringResource(R.string.main_menu_button_cards)
        )
    }

    Button(
        onClick = {}
    ) {
        Text(
            text = stringResource(R.string.main_menu_button_cards)
        )
    }
}

@Composable
fun DecksMenu() {}

//TEMP
@Composable
fun DecksMenu_Open() {}

@Composable
fun DeckOverview() {}

@Composable
fun DeckContents() {}

@Composable
fun ImportCards() {}

@Composable
fun CreateCard() {}

@Composable
fun FlashcardSession() {}

@Composable
fun SessionSummary() {}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlashcardsTheme {
        Loading(modifier = Modifier.fillMaxSize())
    }
}