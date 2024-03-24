package com.example.flashcards.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.Card
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen (
    viewModel: SessionViewModel,
    param: String,
    onBackButtonClicked: () -> Unit,
) {

    viewModel.setup(param)

    val uiState by viewModel.uiState.collectAsState()
    val deck = uiState.deck

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Flashcard(
            card = viewModel.getCurrentCard(),
            isFlipped = uiState.isFlipped,
            isHintShown = uiState.isHintShown,
            isExampleShown = uiState.isExampleShown,
            flipQnA = deck.data.flipQnA,
            onHintButtonClicked = { viewModel.toggleHint() },
            onExampleButtonClicked = { viewModel.toggleExample() },
        )
        FlipBar()
        Notepad()
    }
}

@Composable
fun Flashcard(
    card: Card,
    isFlipped: Boolean,
    isHintShown: Boolean,
    isExampleShown: Boolean,
    flipQnA: Boolean,
    onHintButtonClicked: () -> Unit,
    onExampleButtonClicked: () -> Unit,
    ) {

    val cardText = if (isFlipped || flipQnA) card.answerText else card.questionText

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
        ) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Show cards"
                )
            }
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Skip"
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = cardText,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(Alignment.CenterVertically),
            )

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.5f)
            ) {
                if (isFlipped || flipQnA) {
                    if (isExampleShown) {
                        OutlinedButton(
                            onClick = onExampleButtonClicked,
                            enabled = card.exampleText != null,
                        ) {
                            Text("Hide example")
                        }
                    } else {
                        Button(
                            onClick = onExampleButtonClicked,
                            enabled = card.exampleText != null,
                        ) {
                            Text("Show example")
                        }
                        Text(
                            text = card.exampleText ?: "",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f).wrapContentHeight(Alignment.CenterVertically),
                        )
                    }
                } else {
                    if (isHintShown) {
                        OutlinedButton(
                            onClick = onHintButtonClicked,
                            enabled = card.hintText != null,
                        ) {
                            Text("Hide hint")
                        }
                        Text(
                            text = card.hintText ?: "",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f).wrapContentHeight(Alignment.CenterVertically),
                        )
                    } else {
                        Button(
                            onClick = onHintButtonClicked,
                            enabled = card.hintText != null,
                        ) {
                            Text("Show hint")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlipBar() {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .fillMaxWidth()
            .height(64.dp)
    ) {

    }
}

@Composable
fun Notepad() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

    }
}

@Preview(
    showBackground = true,
    device = "spec:width=393dp,height=808dp"
    //device = "spec:width=650dp,height=900dp"
    //device = "spec:orientation=landscape,width=393dp,height=808dp"
)
@Composable
fun SessionScreenPreview() {
    FlashcardsTheme() {
        SessionScreen(viewModel(), "0", {})
    }
}