package com.example.flashcards.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.dialog.Dialog
import com.example.flashcards.R
import com.example.flashcards.data.Card
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen (
    viewModel: MenuViewModel,
    onBackButtonClicked: () -> Unit,
    onStartButtonClicked: () -> Unit,
    onCreateButtonClicked: () -> Unit,
    onImportButtonClicked: () -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.uiState.collectAsState()

    val deckIndex = uiState.currentDeckIndex ?: 1 //TODO remove ?:
    val deck = uiState.currentDeck ?: DataSource.decks[1] //TODO remove ?:

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = deck.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Deck"
                        )
                    }
                },
            )
        },
        bottomBar = {
            Column() {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = { onStartButtonClicked() },
                                modifier = Modifier
                                    .width(160.dp),
                            ) {
                                Text(
                                    text = "Start",
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                )
                            }
                            IconButton(
                                onClick = { viewModel.toggleSessionOptions() },
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (uiState.isSessionOptionsOpen)
                                        Icons.Default.KeyboardArrowDown
                                        else Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    }
                )
                if (uiState.isSessionOptionsOpen) {
                    SessionOptions(
                        showHints = deck.showHints,
                        showExamples = deck.showExamples,
                        flipQnA = deck.flipQnA,
                        doubleDifficulty = deck.doubleDifficulty,
                        setShowHints = {
                            val temp = deck.copy()
                            temp.showHints = it
                            viewModel.updateDeck(deckIndex, temp)
                        },
                        setShowExamples = {
                            val temp = deck.copy()
                            temp.showExamples = it
                            viewModel.updateDeck(deckIndex, temp)
                        },
                        setFlipQnA = {
                            val temp = deck.copy()
                            temp.flipQnA = it
                            viewModel.updateDeck(deckIndex, temp)
                        },
                        setDoubleDifficulty = {
                            val temp = deck.copy()
                            temp.doubleDifficulty = it
                            viewModel.updateDeck(deckIndex, temp)
                        },
                        onTipButtonClicked = {
                            viewModel.setTipText("In \"Double Difficulty\" mode, a card isn't considered completed until you have guessed it correctly two times in a row.",)
                            viewModel.openTip()
                        },
                    )
                }
            }
        },
    ) { innerPadding ->

        val scrollAmount = rememberLazyListState() // State for the first Row, X
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollableState { delta ->
            scope.launch {
                scrollAmount.scrollBy(-delta)
            }
            delta
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .scrollable(scrollState, Orientation.Horizontal, flingBehavior = ScrollableDefaults.flingBehavior())
        ) {

            DeckStats(
                deck = deck,
            )
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Card Manager",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {  },
                actions = {  },
            )
            CardList(
                getNumCards = { viewModel.getNumCardsInCurrentDeck() },
                getCard = { viewModel.getCardFromCurrentDeck(it) },
                onCardSelected = { viewModel.toggleCardSelection(deckIndex, it) },
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
            )
        }
    }

    if (uiState.isTipOpen) {
        TipDialog(
            tip = uiState.tipText,
            onDismissRequest = { viewModel.closeTip() }
        )
    }
}

@Composable
fun CardList(
    getNumCards: () -> Int,
    getCard: (Int) -> Card,
    onCardSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = dimensionResource(R.dimen.padding_small))
    ) {
        for (i in 0..<getNumCards()) {
            CardComponent(
                getCard = { getCard(i) },
                onCardSelected = { onCardSelected(i) },
            )
        }
    }
}

@Composable
fun CardComponent(
    getCard: () -> Card,
    onCardSelected: () -> Unit,
) {
    val card = getCard()
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = smallPadding, start = smallPadding, end = smallPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = mediumPadding, end = smallPadding)
        ) {
            Text(
                text = card.questionText,
                fontSize = 22.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = mediumPadding)
            )
            Text(
                text = "${Math.round(card.getMasteryLevel()*100)}%",
                fontSize = 22.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.3f)
            )
            Checkbox(
                onCheckedChange = { onCardSelected() },
                checked = card.isSelected(),
            )
        }
    }
}

@Composable
fun DeckStats(
    deck: Deck,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val circleSize = 164.dp

    val masteryLevel = deck.getMasteryLevel()
    val dateStudied = Date(System.currentTimeMillis() - deck.dateStudied.time)

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(mediumPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(bottom = mediumPadding)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier
                    .height(circleSize)
                    .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        progress = masteryLevel,
                        modifier = Modifier.size(circleSize),
                        strokeWidth = 8.dp,
                    )
                    Box(modifier = Modifier
                        .size(circleSize)
                        .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "${Math.round(masteryLevel*100)}%",
                            fontSize = 64.sp,
                        )
                    }
                }
                Text(
                    text = "mastered",
                    modifier = Modifier
                        .padding(bottom = mediumPadding)
                        .weight(1f)
                )
            }
            Text(
                text = "${dateStudied.day} days, ${dateStudied.hours} hours",
                fontSize = 32.sp,
                modifier = Modifier
            )
            Text(
                text = "since last studied",
                modifier = Modifier
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SessionOptions(
    showHints: Boolean,
    showExamples: Boolean,
    flipQnA: Boolean,
    doubleDifficulty: Boolean,
    setShowHints: (Boolean) -> Unit,
    setShowExamples: (Boolean) -> Unit,
    setFlipQnA: (Boolean) -> Unit,
    setDoubleDifficulty: (Boolean) -> Unit,
    onTipButtonClicked: () -> Unit,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .fillMaxWidth()
            .padding(smallPadding)
            .verticalScroll(rememberScrollState())
    ) {
        CustomSwitch(
            label = "Display hints by default",
            checked = showHints,
            onChecked = setShowHints,
            modifier = Modifier
                .padding(horizontal = smallPadding)
        )
        CustomSwitch(
            label = "Display examples by default",
            checked = showExamples,
            onChecked = setShowExamples,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
        CustomSwitch(
            label = "Flip questions and answers",
            checked = flipQnA,
            onChecked = setFlipQnA,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
        CustomSwitch(
            label = "Double Difficulty",
            checked = doubleDifficulty,
            onChecked = setDoubleDifficulty,
            showTip = true,
            onTipButtonClicked = onTipButtonClicked,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
    }
}

@Composable
fun CustomSwitch(
    label: String,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onChecked: (Boolean) -> Unit,
    showTip: Boolean = false,
    onTipButtonClicked: () -> Unit = {},
    ) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Switch(
            checked = checked,
            onCheckedChange = { onChecked(it) },
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, overflow = TextOverflow.Ellipsis)

        if (showTip) {
            IconButton(onClick = onTipButtonClicked) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Back"
                )
            }
        }
    }
}

@Composable
fun TipDialog(
    onDismissRequest: () -> Unit,
    tip: String,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(mediumPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxSize()
            ) {
                Text(text = tip, textAlign = TextAlign.Center)
                Button(onClick = { onDismissRequest() }) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=393dp,height=808dp"
    //device = "spec:width=650dp,height=900dp"
    //device = "spec:orientation=landscape,width=393dp,height=808dp"
)
@Composable
fun DeckScreenPreview() {
    FlashcardsTheme() {
        DeckScreen(viewModel(), {}, {}, {}, {})
    }
}