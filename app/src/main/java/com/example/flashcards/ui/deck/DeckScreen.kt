package com.example.flashcards.ui.deck

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.relations.DeckWithCards
import com.example.flashcards.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen (
    viewModel: DeckViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackButtonClicked: () -> Unit,
    onStartButtonClicked: (Long) -> Unit,
    onCreateButtonClicked: (Long) -> Unit,
    onImportButtonClicked: (Long) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = uiState.deck.deck.name,
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
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Import cards"
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit deck name"
                        )
                    }
                    IconButton(onClick = { viewModel.toggleDeleteDeckDialog() }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete deck"
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
                                onClick = { onStartButtonClicked(uiState.deck.deck.id) },
                                enabled = uiState.deck.cards.isNotEmpty(),
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
                        deck = uiState.deck,
                        setShowHints = {
                            uiState.deck.deck.showHints = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        setShowExamples = {
                            uiState.deck.deck.showExamples = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        setFlipQnA = {
                            uiState.deck.deck.flipQnA = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        setDoubleDifficulty = {
                            uiState.deck.deck.doubleDifficulty = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        onTipButtonClicked = {
                            viewModel.setTipText("In \"Double Difficulty\" mode, a card isn't considered completed until you have guessed it correctly two times in a row.",)
                            viewModel.toggleTip()
                        },
                    )
                }
            }
        },
    ) { innerPadding ->

        val scrollState = rememberScrollState()
        val deckStatsHeightDp = 300.dp
        val deckStatsHeightPx = with(LocalDensity.current) { deckStatsHeightDp.toPx() }.toInt()
        var hidden by remember { mutableStateOf(true) }

        if (!hidden && scrollState.value >= deckStatsHeightPx) {
            hidden = true

        } else if (hidden && scrollState.value < deckStatsHeightPx) {
            hidden = false
        }

        val customCardEditorBar = @Composable {
            CardEditorBar(
                onAllCardsSelected = { viewModel.selectAllCardsInCurrentDeck() },
                onAllCardsDeselected = { viewModel.deselectAllCards() },
                onCardSelectorOpened = { viewModel.openCardSelector() },
                onCardSelectorClosed = { viewModel.closeCardSelector() },
                numCards = viewModel.getNumCardsInCurrentDeck(),
                numSelectedCards = uiState.numSelectedCards,
                isCardSelectorOpen = uiState.isCardSelectorOpen,
                onCreateButtonClicked = { onCreateButtonClicked(uiState.deck.deck.id) },
                onCardDeleteButtonClicked = { viewModel.toggleDeleteCardDialog() },
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (hidden) customCardEditorBar()

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                DeckStats(
                    deck = uiState.deck,
                    modifier = Modifier
                        .height(deckStatsHeightDp)
                )
                if (!hidden) customCardEditorBar()
                CardList(
                    getNumCards = { viewModel.getNumCardsInCurrentDeck() },
                    getCard = { viewModel.getCardFromCurrentDeck(it) },
                    onCardSelected = {
                        viewModel.toggleCardSelection(it)
                        viewModel.openCardSelector()
                    },
                )
            }
        }
    }

    if (uiState.isTipOpen) {
        TipDialog(
            tip = uiState.tipText,
            onDismissRequest = { viewModel.toggleTip() }
        )

    } else if (uiState.isDeleteCardDialogOpen) {
        DeleteCardDialog(
            onDismissRequest = { viewModel.toggleDeleteCardDialog() },
            onDeleteButtonClicked = {
                coroutineScope.launch {
                    viewModel.deleteSelectedCardsInCurrentDeck()
                    viewModel.toggleDeleteCardDialog()
                }
            },
            isMultipleCardsSelected = uiState.numSelectedCards > 1,
        )

    } else if (uiState.isDeleteDeckDialogOpen) {
        DeleteDeckDialog(
            onDismissRequest = { viewModel.toggleDeleteDeckDialog() },
            onDeleteButtonClicked = {
                coroutineScope.launch {
                    viewModel.deleteDeck()
                    viewModel.toggleDeleteDeckDialog()
                    onBackButtonClicked()
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEditorBar(
    onAllCardsSelected: () -> Unit,
    onAllCardsDeselected: () -> Unit,
    onCardSelectorOpened: () -> Unit,
    onCardSelectorClosed: () -> Unit,
    numCards: Int,
    numSelectedCards: Int,
    isCardSelectorOpen: Boolean,
    onCreateButtonClicked: () -> Unit,
    onCardDeleteButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium_small)

    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = if (isCardSelectorOpen)
                    "$numSelectedCards / $numCards Selected"
                    else if (numCards == 1) "$numCards Card total"
                    else "$numCards Cards total",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (isCardSelectorOpen) {
                IconButton(onClick = onCardSelectorClosed) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Quit selector"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onCreateButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add card"
                )
            }
            if (isCardSelectorOpen) {
                IconButton(
                    onClick = onCardDeleteButtonClicked,
                    enabled = numCards > 0 && numSelectedCards > 0,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete card"
                    )
                }
            }
            Checkbox(
                onCheckedChange = {
                    if (numCards == numSelectedCards) {
                        onAllCardsDeselected()
                    } else {
                        onCardSelectorOpened()
                        onAllCardsSelected()
                    }
                },
                checked = numCards > 0 && numCards == numSelectedCards,
                enabled = numCards > 0,
                modifier = Modifier
                    .padding(end = mediumPadding)
            )
        },
        modifier = modifier
            .wrapContentHeight(Alignment.CenterVertically)
    )
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
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = smallPadding)
            )
            Text(
                text = "${Math.round(card.getMasteryLevel()*100)}%",
                fontSize = 16.sp,
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(56.dp)
                    .padding(end = smallPadding)
            )
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit card"
                )
            }
            Checkbox(
                onCheckedChange = { onCardSelected() },
                checked = card.isSelected,
            )
        }
    }
}

@Composable
fun DeckStats(
    deck: DeckWithCards,
    modifier: Modifier = Modifier,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val circleSize = 164.dp

    val masteryLevel = deck.deck.masteryLevel
    val dateStudied = Date(System.currentTimeMillis() - deck.deck.dateStudied)

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
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

@Composable
fun SessionOptions(
    deck: DeckWithCards,
    setShowHints: (Boolean) -> Unit,
    setShowExamples: (Boolean) -> Unit,
    setFlipQnA: (Boolean) -> Unit,
    setDoubleDifficulty: (Boolean) -> Unit,
    onTipButtonClicked: () -> Unit,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Log.d("debug", "updated")

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
            checked = deck.deck.showHints,
            onChecked = setShowHints,
            modifier = Modifier
                .padding(horizontal = smallPadding)
        )
        CustomSwitch(
            label = "Display examples by default",
            checked = deck.deck.showExamples,
            onChecked = setShowExamples,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
        CustomSwitch(
            label = "Flip questions and answers",
            checked = deck.deck.flipQnA,
            onChecked = setFlipQnA,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
        CustomSwitch(
            label = "Double Difficulty",
            checked = deck.deck.doubleDifficulty,
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
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
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

@Composable
fun DeleteDeckDialog(
    onDismissRequest: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127)))
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(mediumPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxSize()


            ) {
                Text(
                    text = "Delete this deck?",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "This action cannot be undone.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) { Text("Cancel") }
                    Button(
                        onClick = {
                            onDeleteButtonClicked()
                        }
                    ) { Text("Delete") }
                }
            }
        }
    }
}

@Composable
fun DeleteCardDialog(
    onDismissRequest: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    isMultipleCardsSelected: Boolean,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127)))
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(mediumPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxSize()


            ) {
                Text(
                    text = "Delete selected card" + if (isMultipleCardsSelected) "s?" else "?",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "This action cannot be undone.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) { Text("Cancel") }
                    Button(
                        onClick = {
                            onDeleteButtonClicked()
                        }
                    ) { Text("Delete") }
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
        DeckScreen(viewModel(), {}, {a -> {a}}, {}, {})
    }
}