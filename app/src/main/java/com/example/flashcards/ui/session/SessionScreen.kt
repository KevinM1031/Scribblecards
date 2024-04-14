package com.example.flashcards.ui.session

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.relations.DeckWithCards
import com.example.flashcards.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.roundToInt

private const val SWIPER_SIZE = 48
private const val SWIPER_HEIGHT = 64
private const val SMOOTHING_FACTOR = 0.4f

@Composable
fun SessionScreen (
    viewModel: SessionViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onQuit: (Long) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val deck = viewModel.getCurrentDeck()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    if (uiState.isSessionCompleted) {
        SummaryScreen(
            viewModel = viewModel,
            uiState = uiState,
            coroutineScope = coroutineScope,
            onExit = onQuit
        )

    } else {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    SessionMenu(
                        deck = deck,
                        currentCardIndex = uiState.currentCardIndex,
                        activeCardIndices = uiState.activeCards,
                        usedCardIndices = uiState.usedCards,
                        completedCardIndices = uiState.completedCards,
                        cardHistory = uiState.cardHistory,
                        onQuitButtonClicked = { viewModel.toggleQuitDialog() },
                        onRestartButtonClicked = { viewModel.toggleRestartDialog() },
                    )
                }
            },
        ) {
            Column {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Flashcard(
                            card = viewModel.getCurrentCard(),
                            isSlideAnimRequested = uiState.isSlideAnimRequested,
                            isFlipped = uiState.isFlipped,
                            isHintShown = uiState.isHintShown,
                            isExampleShown = uiState.isExampleShown,
                            isHistoryShown = uiState.isHistoryShown,
                            flipQnA = deck.deck.flipQnA,
                            flipContent = uiState.flipContent,
                            completeSlideAnimRequest = { viewModel.completeSlideAnimRequest() },
                            onHintButtonClicked = { viewModel.showHint() },
                            onExampleButtonClicked = { viewModel.showExample() },
                            onInfoButtonClicked = { viewModel.toggleInfo() },
                            onSkipButtonClicked = { viewModel.skipCard() },
                            onFlipButtonClicked = { viewModel.flipCard() },
                            setContentFlip = { viewModel.setContentFlip(it) },
                            nextCard = {
                                viewModel.nextCard()
                                viewModel.clearStrokes()
                            },
                            onMenuButtonClicked = {
                                coroutineScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            currentCardHistory = uiState.cardHistory[uiState.currentCardIndex]!!,
                            modifier = Modifier.weight(0.4f)
                        )
                        if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                            FlipBar(
                                setIsCorrect = { viewModel.setIsCorrect(it) },
                                enabled = uiState.isAnswerSeen,
                                requestSlideAnim = { viewModel.requestSlideAnim() }
                            )
                            Notepad(
                                strokes = uiState.strokes,
                                onStroke = { viewModel.addStroke(it) },
                                onUndo = {
                                    viewModel.undoStroke()
                                    viewModel.update()
                                },
                                onClear = {
                                    viewModel.clearStrokes()
                                    viewModel.update()
                                },
                                modifier = Modifier.weight(0.5f),
                            )
                        }
                    }
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Notepad(
                            strokes = uiState.strokes,
                            onStroke = { viewModel.addStroke(it) },
                            onUndo = {
                                viewModel.undoStroke()
                                viewModel.update()
                            },
                            onClear = {
                                viewModel.clearStrokes()
                                viewModel.update()
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Box(
                        modifier = Modifier.height(SWIPER_HEIGHT.dp)
                    ) {
                        FlipBar(
                            setIsCorrect = { viewModel.setIsCorrect(it) },
                            enabled = uiState.isAnswerSeen,
                            requestSlideAnim = { viewModel.requestSlideAnim() }
                        )
                    }
                }
            }
        }

        if (uiState.isQuitDialogOpen) {
            QuitDialog(
                onDismissRequest = { viewModel.toggleQuitDialog() },
                onQuitButtonClicked = {
                    viewModel.toggleQuitDialog()
                    onQuit(uiState.param)
                },
            )
        }

        if (uiState.isRestartDialogOpen) {
            RestartDialog(
                onDismissRequest = { viewModel.toggleRestartDialog() },
                onRestartButtonClicked = {
                    viewModel.toggleRestartDialog()
                    viewModel.reset()
                    coroutineScope.launch {
                        drawerState.close()
                        viewModel.startSession(uiState.param)
                    }
                },
            )
        }
    }

    if (uiState.isTipDialogOpen) {
        TipDialog(
            tip = uiState.tipText,
            onDismissRequest = { viewModel.toggleTipDialog() }
        )
    }
}

@Composable
fun QuitDialog(
    onDismissRequest: () -> Unit,
    onQuitButtonClicked: () -> Unit,
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
                    text = "Quit Session?",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Current session record will be lost.",
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
                            onQuitButtonClicked()
                        }
                    ) { Text("Quit") }
                }
            }
        }
    }
}

@Composable
fun RestartDialog(
    onDismissRequest: () -> Unit,
    onRestartButtonClicked: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {

        val mediumPadding = dimensionResource(R.dimen.padding_medium)

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
                    text = "Restart Session?",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Current session record will be lost.",
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
                        onClick = onRestartButtonClicked
                    ) { Text("Restart") }
                }
            }
        }
    }
}

@Composable
fun SessionMenu(
    deck: DeckWithCards,
    currentCardIndex : Int,
    activeCardIndices: List<Int>,
    usedCardIndices: List<Int>,
    completedCardIndices: List<Int>,
    cardHistory: Map<Int, CardHistory>,
    onRestartButtonClicked: () -> Unit,
    onQuitButtonClicked: () -> Unit,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val progress = completedCardIndices.size.toFloat() / deck.cards.size

    Column(
        modifier = Modifier
            .width(300.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(smallPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(smallPadding)
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onRestartButtonClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restart"
                        )
                    }
                    IconButton(
                        onClick = onQuitButtonClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Quit"
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = deck.deck.name,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(62.dp)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${(progress*100).roundToInt()}% Completed ",
                    fontSize = 14.sp
                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(smallPadding)
                        .height(8.dp),
                )
            }
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            CardComponent(
                card = deck.cards[currentCardIndex],
                cardHistory = cardHistory[currentCardIndex]!!,
                flipQnA = deck.deck.flipQnA,
            )
            for (i in activeCardIndices) {
                CardComponent(
                    card = deck.cards[i],
                    cardHistory = cardHistory[i]!!,
                    flipQnA = deck.deck.flipQnA,
                )
            }
            for (i in usedCardIndices) {
                CardComponent(
                    card = deck.cards[i],
                    cardHistory = cardHistory[i]!!,
                    flipQnA = deck.deck.flipQnA,
                )
            }
            for (i in completedCardIndices) {
                CardComponent(
                    card = deck.cards[i],
                    cardHistory = cardHistory[i]!!,
                    flipQnA = deck.deck.flipQnA,
                )
            }
        }
    }
}

@Composable
fun CardComponent(
    card: Card,
    cardHistory: CardHistory,
    flipQnA: Boolean,
    modifier: Modifier = Modifier,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val cardHistoryList = cardHistory.getHistory()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = smallPadding, end = smallPadding, bottom = smallPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = mediumPadding)
        ) {
            Text(
                text = if (flipQnA) card.answerText else card.questionText,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = mediumPadding)
            )
            if (cardHistoryList.isEmpty()) {
                Text("New")
            } else {
                for (wasCorrect in cardHistoryList) {
                    if (wasCorrect) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Correct",
                            tint = Color.Green,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Wrong",
                            tint = Color.Red,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Flashcard(
    card: Card,
    isSlideAnimRequested: Boolean,
    isFlipped: Boolean,
    isHintShown: Boolean,
    isExampleShown: Boolean,
    isHistoryShown: Boolean,
    flipQnA: Boolean,
    flipContent: Boolean,
    completeSlideAnimRequest: () -> Unit,
    onHintButtonClicked: () -> Unit,
    onExampleButtonClicked: () -> Unit,
    onInfoButtonClicked: () -> Unit,
    onSkipButtonClicked: () -> Unit,
    onFlipButtonClicked: () -> Unit,
    onMenuButtonClicked: () -> Unit,
    setContentFlip: (Boolean) -> Unit,
    nextCard: () -> Unit,
    currentCardHistory: CardHistory,
    modifier: Modifier = Modifier,
    ) {

    var verticalSlide by remember { mutableStateOf(false) }

    val cardSkip = animateFloatAsState(
        targetValue = if (verticalSlide || isSlideAnimRequested) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            if (isFlipped) onFlipButtonClicked()
            if (isSlideAnimRequested) nextCard()
            if (verticalSlide) onSkipButtonClicked()
            completeSlideAnimRequest()
            verticalSlide = false
        }
    )

    val contentRotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = if (cardSkip.value > 0) 0 else 90,
            easing = { fraction -> floor(fraction) }
        ),
        finishedListener = { setContentFlip(isFlipped) }
    )

    val cardRotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = if (cardSkip.value > 0) 0 else 250,
            easing = FastOutSlowInEasing,
        ),
    )

    val cardText = if (flipContent || flipQnA) card.answerText else card.questionText
    val historyList = currentCardHistory.getHistory()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
        ),
        onClick = {
            onFlipButtonClicked()
        },
        enabled = cardSkip.value == 0f,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(dimensionResource(R.dimen.padding_small))
            .zIndex(if (cardSkip.value > 0) -100f else 100f)
            .offset(y = cardSkip.value.dp * 100f)
            .alpha(1f - cardSkip.value)
            .graphicsLayer {
                rotationY = cardRotation.value
                cameraDistance = 20f * density
            },
    ) {
        Box(
            modifier = Modifier.graphicsLayer {
                rotationY = contentRotation.value
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                ) {
                    IconButton(
                        onClick = onMenuButtonClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Show cards"
                        )
                    }
                    IconButton(
                        onClick = { verticalSlide = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Skip"
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isHistoryShown && cardSkip.value == 0f)
                        if (historyList.isEmpty()) {
                            Text("This is a new card!")
                        } else {
                            for (wasCorrect in currentCardHistory.getHistory()) {
                                if (wasCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Correct",
                                        tint = Color.Green,
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Wrong",
                                        tint = Color.Red,
                                    )
                                }
                            }
                        }
                    IconButton(
                        onClick = onInfoButtonClicked,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Show card info"
                        )
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Spacer(modifier = Modifier.size(52.dp))
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
                    if (flipContent || flipQnA) {
                        if (isExampleShown) {
                            Text(
                                text = card.exampleText ?: "",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight(Alignment.CenterVertically),
                            )
                        } else {
                            TextButton(
                                onClick = onExampleButtonClicked,
                                enabled = card.exampleText != null,
                            ) {
                                Text(
                                    text = "Example",
                                    textDecoration = TextDecoration.Underline,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        if (isHintShown) {
                            Text(
                                text = card.hintText ?: "",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight(Alignment.CenterVertically),
                            )
                        } else {
                            TextButton(
                                onClick = onHintButtonClicked,
                                enabled = card.hintText != null,
                            ) {
                                Text(
                                    text = "Hint",
                                    textDecoration = TextDecoration.Underline,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlipBar(
    setIsCorrect: (Boolean) -> Unit,
    enabled: Boolean,
    requestSlideAnim: () -> Unit,
    ) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    if (enabled) {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current
        val swipeRange =
            with(density) { (configuration.screenWidthDp.dp - smallPadding * 2 - SWIPER_SIZE.dp).toPx() / 2 }
        var isOnSwipeCooldown by remember { mutableStateOf(false) }

        val anchors = DraggableAnchors {
            -1 at -swipeRange
            0 at 0f
            1 at swipeRange
        }
        val middleAnchor = DraggableAnchors {
            0 at 0f
        }
        val swipeableState = remember {
            AnchoredDraggableState(
                initialValue = 0,
                positionalThreshold = { distance -> distance * 0.5f },
                velocityThreshold = { with(density) { 100.dp.toPx() } },
                animationSpec = tween()
            )
        }
        SideEffect {
            if (!isOnSwipeCooldown && swipeableState.currentValue != 0) {
                setIsCorrect(swipeableState.currentValue == 1)
                requestSlideAnim()
                isOnSwipeCooldown = true
            } else if (isOnSwipeCooldown && swipeableState.currentValue == 0) {
                isOnSwipeCooldown = false
            }

            if (isOnSwipeCooldown) {
                swipeableState.updateAnchors(middleAnchor)
            } else {
                swipeableState.updateAnchors(anchors)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .fillMaxWidth()
                .height(SWIPER_HEIGHT.dp)
                .padding(smallPadding)
                .anchoredDraggable(state = swipeableState, orientation = Orientation.Horizontal)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                tint = Color.Red,
                contentDescription = "Wrong",
                modifier = Modifier
                    .size(SWIPER_SIZE.dp)
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                tint = Color.Black,
                contentDescription = "Correct",
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = swipeableState
                                .requireOffset()
                                .toInt(), y = 0
                        )
                    }
                    .size(SWIPER_SIZE.dp)
                    .zIndex(10f)
            )
            Icon(
                imageVector = Icons.Default.Check,
                tint = Color.Green,
                contentDescription = "Correct",
                modifier = Modifier
                    .size(SWIPER_SIZE.dp)
            )
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .fillMaxWidth()
                .height(64.dp)
                .padding(smallPadding)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                tint = Color.Gray,
                contentDescription = "Wrong",
                modifier = Modifier
                    .size(SWIPER_SIZE.dp)
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                tint = Color.Gray,
                contentDescription = "Correct",
                modifier = Modifier
                    .size(SWIPER_SIZE.dp)
            )
            Icon(
                imageVector = Icons.Default.Check,
                tint = Color.Gray,
                contentDescription = "Correct",
                modifier = Modifier
                    .size(SWIPER_SIZE.dp)
            )
        }
    }
}

@Composable
fun Notepad(
    strokes: List<List<Line>>,
    onStroke: (List<Line>) -> Unit,
    onUndo: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val newStroke = remember { mutableStateListOf<Line>() }

    BoxWithConstraints(
        modifier = modifier
            .zIndex(-1000f)
    ) {
        val maxWidth = constraints.maxWidth.toFloat()
        val maxHeight = constraints.maxHeight.toFloat()

        var isDrawing by remember { mutableStateOf(false) }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = {
                            newStroke.clear()
                            isDrawing = true
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val start = change.position - dragAmount
                            var end = change.position
                            if (isDrawing) {
                                if (end.x < 0 || end.x > maxWidth || end.y < 0 || end.y > maxHeight) {
                                    end = end.copy(
                                        x = end.x.coerceAtLeast(0f).coerceAtMost(maxWidth),
                                        y = end.y.coerceAtLeast(0f).coerceAtMost(maxHeight)
                                    )
                                    newStroke.add(Line(start, end))
                                    onStroke(newStroke.toList())
                                    isDrawing = false
                                } else {
                                    newStroke.add(Line(start, end))
                                }
                            }
                        },
                        onDragEnd = {
                            onStroke(newStroke.toList())
                            isDrawing = false
                        }
                    )
                }
        ) {
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 10f)
            drawLine(color = Color.LightGray, start = Offset(size.width/2, size.height/2), end = Offset(size.width/2, 0f), pathEffect = pathEffect, strokeWidth = 2.dp.toPx(),)
            drawLine(color = Color.LightGray, start = Offset(size.width/2, size.height/2), end = Offset(size.width/2, size.height), pathEffect = pathEffect, strokeWidth = 2.dp.toPx(),)
            drawLine(color = Color.LightGray, start = Offset(size.width/2, size.height/2), end = Offset(0f, size.height/2), pathEffect = pathEffect, strokeWidth = 2.dp.toPx(),)
            drawLine(color = Color.LightGray, start = Offset(size.width/2, size.height/2), end = Offset(size.width, size.height/2), pathEffect = pathEffect, strokeWidth = 2.dp.toPx(),)

            val currStrokes = strokes.toMutableList()
            if (newStroke.isNotEmpty()) {
                currStrokes.add(newStroke)
            }
            for (stroke in currStrokes) {
                if (stroke.isEmpty()) continue

                val path = Path()
                path.moveTo(stroke[0].start.x, stroke[0].start.y)

                if (stroke.size == 1) {
                    path.lineTo(stroke[0].end.x, stroke[0].end.y)

                } else {

                    val k = SMOOTHING_FACTOR
                    var cp = getControlPoint(stroke[0].start, stroke[0].end, stroke[1].end, -k)
                    path.quadraticBezierTo(cp.x, cp.y, stroke[0].end.x, stroke[0].end.y)

                    for (i in 1..<stroke.size - 1) {
                        val cp1 = getControlPoint(stroke[i].end, stroke[i].start, stroke[i-1].start, -k)
                        val cp2 = getControlPoint(stroke[i].start, stroke[i].end, stroke[i + 1].end, -k)

//                        drawLine(start = stroke[i].start, end = cp1, color = Color.Red, strokeWidth = 5f)
//                        drawLine(start = stroke[i].end, end = cp2, color = Color.Blue, strokeWidth = 5f)
//                        drawCircle(color = Color.Green, radius = 10f, center = stroke[i].start)
//                        drawCircle(color = Color.Red, radius = 10f, center = cp1)
//                        drawCircle(color = Color.Blue, radius = 10f, center = cp2)

                        path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, stroke[i].end.x, stroke[i].end.y,)
                    }

                    val i = stroke.size-1
                    cp = getControlPoint(stroke[i].end, stroke[i].start, stroke[i].start, -k)
                    path.quadraticBezierTo(cp.x, cp.y, stroke[i].end.x, stroke[i].end.y)
                }
                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(4.dp.toPx())
                )
            }
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {
            IconButton(
                onClick = {
                    newStroke.clear()
                    onClear()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear"
                )
            }
            IconButton(
                onClick = {
                    newStroke.clear()
                    onUndo()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Undo"
                )
            }
        }
    }
}

fun getControlPoint(p1: Offset, p2: Offset, p3: Offset, k: Float): Offset {
    val l = (p3.x-p1.x)*(p3.x-p1.x) + (p3.y-p1.y)*(p3.y-p1.y)
    val v1 = Offset(p2.x-p1.x, p2.y-p1.y)
    val v2 = Offset(p3.x-p1.x, p3.y-p1.y)
    val c = k * (v1.x*v2.x + v1.y*v2.y) / l
    return Offset(p2.x + v2.x*c, p2.y + v2.y*c)
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
)
@Composable
fun SessionScreenPreview() {
    FlashcardsTheme() {
        SessionScreen(viewModel(), {})
    }
}