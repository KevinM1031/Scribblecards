package com.example.flashcards.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import androidx.core.util.TypedValueCompat.dpToPx
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.SwipeToDismissBoxDefaults.AnimationSpec
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.flashcards.R
import com.example.flashcards.data.Card
import com.example.flashcards.data.CardHistory
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.Date
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen (
    viewModel: SessionViewModel,
    param: String,
    onBackButtonClicked: () -> Unit,
) {

    viewModel.setup(param)

    val uiState by viewModel.uiState.collectAsState()
    val deck = viewModel.getCurrentDeck()

    if (uiState.isSessionCompleted) {
        Text(text = "FINISHED", fontSize = 128.sp)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { /* Drawer content */ }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Flashcard(
                card = viewModel.getCurrentCard(),
                isFlipped = uiState.isFlipped,
                isHintShown = uiState.isHintShown,
                isExampleShown = uiState.isExampleShown,
                isHistoryShown = uiState.isHistoryShown,
                flipQnA = deck.data.flipQnA,
                onHintButtonClicked = { viewModel.showHint() },
                onExampleButtonClicked = { viewModel.showExample() },
                onInfoButtonClicked = { viewModel.toggleInfo() },
                onSkipButtonClicked = { viewModel.skipCard() },
                onFlipButtonClicked = { viewModel.flipCard() },
                onMenuButtonClicked = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                currentCardHistory = uiState.cardHistory[uiState.currentCardIndex]!!,
            )
            FlipBar(
                nextCard = { viewModel.nextCard(it) },
                enabled = uiState.isAnswerSeen,
            )
            Notepad()
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Flashcard(
    card: Card,
    isFlipped: Boolean,
    isHintShown: Boolean,
    isExampleShown: Boolean,
    isHistoryShown: Boolean,
    flipQnA: Boolean,
    onHintButtonClicked: () -> Unit,
    onExampleButtonClicked: () -> Unit,
    onInfoButtonClicked: () -> Unit,
    onSkipButtonClicked: () -> Unit,
    onFlipButtonClicked: () -> Unit,
    onMenuButtonClicked: () -> Unit,
    currentCardHistory: CardHistory,
    ) {

    var flipContent by remember { mutableStateOf(false) }

    val contentRotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 90,
            easing = { fraction -> floor(fraction) }
        ),
        finishedListener = { flipContent = isFlipped }
    )

    val rotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing,
        ),
    )

    val cardText = if (flipContent || flipQnA) card.answerText else card.questionText
    val historyList = currentCardHistory.getHistory()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            onFlipButtonClicked()
        },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(dimensionResource(R.dimen.padding_small))
            .zIndex(100f)
            .graphicsLayer {
                rotationY = rotation.value
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
                Column(
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
                        onClick = onSkipButtonClicked
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
                    if (isHistoryShown)
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
@OptIn(ExperimentalWearMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun FlipBar(
    nextCard: (Boolean) -> Unit,
    enabled: Boolean,
) {
    val swiperSize = 48.dp
    val smallPadding = dimensionResource(R.dimen.padding_small)

    if (enabled) {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current
        val swipeRange =
            with(density) { (configuration.screenWidthDp.dp - smallPadding * 2 - swiperSize).toPx() / 2 }
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
                nextCard(swipeableState.currentValue == 1)
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
                .height(64.dp)
                .padding(smallPadding)
                .anchoredDraggable(state = swipeableState, orientation = Orientation.Horizontal)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                tint = Color.Red,
                contentDescription = "Wrong",
                modifier = Modifier
                    .size(swiperSize)
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
                    .size(swiperSize)
                    .zIndex(10f)
            )
            Icon(
                imageVector = Icons.Default.Check,
                tint = Color.Green,
                contentDescription = "Correct",
                modifier = Modifier
                    .size(swiperSize)
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
                    .size(swiperSize)
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                tint = Color.Gray,
                contentDescription = "Correct",
                modifier = Modifier
                    .size(swiperSize)
            )
            Icon(
                imageVector = Icons.Default.Check,
                tint = Color.Gray,
                contentDescription = "Correct",
                modifier = Modifier
                    .size(swiperSize)
            )
        }
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