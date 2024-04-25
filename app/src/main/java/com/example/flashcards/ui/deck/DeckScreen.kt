package com.example.flashcards.ui.deck

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.Constants
import com.example.flashcards.data.Settings
import com.example.flashcards.data.StringLength
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
    onCreateCardButtonClicked: (Long) -> Unit,
    onEditCardButtonClicked: (Long) -> Unit,
    onImportCardsButtonClicked: (Long) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.softReset()
    }

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
                    IconButton(onClick = { onImportCardsButtonClicked(uiState.param) }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Import cards"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.setUserInput(uiState.deck.deck.name)
                        viewModel.toggleEditDeckNameDialog()
                    }) {
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->

        val customCardEditorBar = @Composable {
            CardEditorBar(
                onAllCardsSelected = { viewModel.selectAllCardsInCurrentDeck() },
                onAllCardsDeselected = { viewModel.deselectAllCards() },
                onCardSelectorOpened = { viewModel.openCardSelector() },
                onCardSelectorClosed = { viewModel.closeCardSelector() },
                onSortButtonClicked = { viewModel.cycleCardSort() },
                currentSortType = uiState.sortType,
                numCards = viewModel.getNumCardsInCurrentDeck(),
                numSelectedCards = uiState.numSelectedCards,
                isCardSelectorOpen = uiState.isCardSelectorOpen,
                onCreateButtonClicked = { onCreateCardButtonClicked(uiState.deck.deck.id) },
                onTooManyCards = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Card limit reached (maximum ${Constants.MAX_CARDS} cards).",
                            withDismissAction = true,
                        )
                    }
                },
                onCardDeleteButtonClicked = { viewModel.toggleDeleteCardDialog() },
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            val lazyColumnState = rememberLazyListState()
            val isFirstItemVisible by remember { derivedStateOf { lazyColumnState.firstVisibleItemIndex == 0 } }
            var hidden by remember { mutableStateOf(true) }

            if (!hidden && !isFirstItemVisible) {
                hidden = true
            } else if (hidden && isFirstItemVisible) {
                hidden = false
            }

            if (hidden) customCardEditorBar()

            LazyColumn(state = lazyColumnState) {
                item {
                    DeckStats(
                        deck = uiState.deck,
                        modifier = Modifier.height(300.dp)
                    )
                }
                if (!hidden) item { customCardEditorBar() }

                val numCards = viewModel.getNumCardsInCurrentDeck()
                item {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                }
                items(numCards) { i ->
                    CardComponent(
                        card = uiState.deck.cards[i],
                        onCardSelected = {
                            viewModel.toggleCardSelection(i)
                            viewModel.openCardSelector()
                        },
                        onFavoriteCardButtonClicked = {
                            coroutineScope.launch {
                                viewModel.toggleCardFavorite(i)
                            }
                        },
                        onEditCardButtonClicked = { onEditCardButtonClicked(it) },
                        lastStudied = System.currentTimeMillis() - uiState.deck.deck.dateStudied,
                        lastUpdated = uiState.lastUpdated,
                        isQnAFlipped = uiState.deck.deck.flipQnA,
                    )
                }
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

    } else if (uiState.isEditDeckNameDialogOpen) {
        EditDeckNameDialog(
            onDismissRequest = { viewModel.toggleEditDeckNameDialog() },
            onConfirmClicked = {
                coroutineScope.launch {
                    viewModel.updateDeckName(it)
                    viewModel.toggleEditDeckNameDialog()
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
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

    } else if (uiState.isSessionOptionsOpen) {
        BackHandler { viewModel.toggleSessionOptions() }

    } else if (uiState.isCardSelectorOpen) {
        BackHandler { viewModel.closeCardSelector() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEditorBar(
    onAllCardsSelected: () -> Unit,
    onAllCardsDeselected: () -> Unit,
    onCardSelectorOpened: () -> Unit,
    onCardSelectorClosed: () -> Unit,
    onSortButtonClicked: () -> Unit,
    currentSortType: SortType,
    numCards: Int,
    numSelectedCards: Int,
    isCardSelectorOpen: Boolean,
    onCreateButtonClicked: () -> Unit,
    onTooManyCards: () -> Unit,
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
            IconButton(
                onClick = {
                    if (numCards < Constants.MAX_CARDS) onCreateButtonClicked()
                    else onTooManyCards()
                }
            ) {
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
            IconButton(
                onClick = onSortButtonClicked,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.width(24.dp)
                    )
                    Text(
                        text = when (currentSortType) {
                            SortType.ALPHANUMERICAL -> "A"
                            SortType.MASTERY -> "%"
                            SortType.FAVORITE -> "★"
                        },
                        fontSize = 12.sp,
                        modifier = Modifier.width(10.dp)
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
fun CardComponent(
    card: Card,
    onCardSelected: () -> Unit,
    onFavoriteCardButtonClicked: () -> Unit,
    onEditCardButtonClicked: (Long) -> Unit,
    isQnAFlipped: Boolean,
    lastStudied: Long,
    lastUpdated: Long,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    var isAnswerBlurred by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(bottom = smallPadding, start = smallPadding, end = smallPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = mediumPadding, end = smallPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.7f)
                    .padding(vertical = smallPadding)
            ) {
                Text(
                    text = if (isQnAFlipped) card.answerText else card.questionText,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = if (isQnAFlipped) card.questionText else card.answerText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .blur(if (isAnswerBlurred) 8.dp else 0.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { isAnswerBlurred = !isAnswerBlurred }
                            )
                        }
                )
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${Math.round(card.getMasteryLevel(millisSinceStudied = lastStudied)*100)}% (${card.numPerfect}/${Settings.getMasteryStandard()})",
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = onFavoriteCardButtonClicked,
                ) {
                    if (card.isFavorite) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            tint = Color.Yellow,
                            contentDescription = "Toggle favorite ($lastUpdated)"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Toggle favorite ($lastUpdated)"
                        )
                    }
                }
                IconButton(
                    onClick = { onEditCardButtonClicked(card.id) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit card"
                    )
                }
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
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val circleSize = 164.dp

    val masteryLevel = deck.deck.masteryLevel
    val timeSinceStudied = System.currentTimeMillis() - deck.deck.dateStudied
    val days = timeSinceStudied/86400000
    val hours = timeSinceStudied/3600000%24
    val minutes = (timeSinceStudied/60000).coerceAtLeast(1)%60

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
                        trackColor = Color.Gray,
                    )
                    Box(modifier = Modifier
                        .size(circleSize)
                        .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "${Math.round(masteryLevel*100)}%",
                            fontSize = 58.sp,
                        )
                    }
                }
                Text(
                    text = "mastered",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(bottom = smallPadding)
                        .weight(1f)
                )
            }
            if (deck.deck.isNeverStudied()) {
                Text(
                    text = "New deck!",
                    fontSize = 32.sp,
                    modifier = Modifier
                )
            } else if (days < 1) {
                Text(
                    text =
                        "${hours} hour" + (if(hours > 1) "s, " else ", ") +
                        "${minutes} minute" + (if(minutes > 1) "s" else ""),
                    fontSize = 32.sp,
                    modifier = Modifier
                )
                Text(
                    text = "since last studied",
                    fontSize = 16.sp,
                    modifier = Modifier
                )
            } else {
                Text(
                    text =
                        "${days} day" + (if(days > 1) "s, " else ", ") +
                        "${hours} hour" + (if(hours > 1) "s" else ""),
                    fontSize = 32.sp,
                    modifier = Modifier
                )
                Text(
                    text = "since last studied",
                    fontSize = 16.sp,
                    modifier = Modifier
                )
            }
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
    val largePadding = dimensionResource(R.dimen.padding_large)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()
            ) {
                Text(text = tip, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(largePadding))
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.size(120.dp, 40.dp)
                ) {
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
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127)))
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = smallPadding, bottom = largePadding)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text("Cancel") }
                    Button(
                        onClick = {
                            onDeleteButtonClicked()
                        },
                        modifier = Modifier.size(120.dp, 40.dp)
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
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127)))
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()

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
                    modifier = Modifier
                        .padding(top = smallPadding, bottom = largePadding)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text("Cancel") }
                    Button(
                        onClick = {
                            onDeleteButtonClicked()
                        },
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text("Delete") }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditDeckNameDialog(
    onDismissRequest: () -> Unit,
    onConfirmClicked: (String) -> Unit,
    setUserInput: (String) -> Unit,
    userInput: String?,
    focusManager: FocusManager,
    ) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {

        val smallPadding = dimensionResource(R.dimen.padding_small)
        val mediumPadding = dimensionResource(R.dimen.padding_medium)
        val largePadding = dimensionResource(R.dimen.padding_large)
        var isError by remember { mutableStateOf(false) }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Name for the deck:",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Column(
                    modifier = Modifier.padding(top = smallPadding, bottom = largePadding)
                ) {
                    if (isError) {
                        Text(
                            text = "This field is required.",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Start,
                        )
                    }
                    OutlinedTextField(
                        value = userInput ?: "",
                        onValueChange = { setUserInput(if (it.length <= StringLength.SHORT.maxLength) it else it.substring(0..StringLength.SHORT.maxLength)) },
                        label = { Text("Deck name") },
                        isError = isError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Exit) }),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (userInput.isNullOrBlank()) {
                                isError = true
                            } else {
                                onConfirmClicked(userInput)
                            }
                        },
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text("Save") }
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
        DeckScreen(viewModel(), {}, {a -> {a}}, {a -> {a}}, {}, {})
    }
}