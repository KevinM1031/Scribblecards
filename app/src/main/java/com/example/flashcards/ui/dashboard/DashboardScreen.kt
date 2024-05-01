package com.example.flashcards.ui.dashboard

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.Constants
import com.example.flashcards.data.StringLength
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.ui.AppViewModelProvider
import com.example.flashcards.ui.theme.FlashcardsTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val BOX_SIZE_DP = 100
private const val BOX_SIZE_IN_BUNDLE_DP = 90

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onDeckButtonClicked: (Long) -> Unit,
    onBackButtonClicked: () -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.softReset()
    }
    val uiState by viewModel.uiState.collectAsState()
    val isBundleOpen = viewModel.isBundleOpen()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            if (uiState.isBundleCreatorOpen) {
                BundleCreatorTopAppBar(
                    numSelected = uiState.numSelectedDecks + uiState.numSelectedBundles,
                    closeBundleCreator = { viewModel.closeBundleCreator() },
                    onCreateClicked = { viewModel.openBundleCreatorDialog() },
                )

            } else if (isBundleOpen) {
                if (uiState.isRemoveDeckFromBundleUiOpen) {
                    RemoveDeckFromBundleTopBar(
                        numSelected = uiState.numSelectedDecks,
                        closeRemoveDeckFromBundleUi = { viewModel.closeRemoveDeckFromBundleUi() },
                        onRemoveClicked = {
                            coroutineScope.launch {
                                viewModel.moveSelectedDecksOutOfBundle()
                                viewModel.requestCloseBundleAnim()
                            }
                        },
                    )
                } else {
                    BundleTopAppBar(
                        onBackButtonClicked = { viewModel.requestCloseBundleAnim(); },
                        onEditBundleNameButtonClicked = { viewModel.openEditBundleNameDialog() },
                        title = viewModel.getBundle(uiState.currentBundleIndex!!)?.bundle?.name ?: "",
                    )
                }

            } else {
                DashboardTopAppBar(
                    onBackButtonClicked = onBackButtonClicked,
                )
            }
        },
        floatingActionButton = {
            if (!uiState.isBundleCreatorOpen) {
                CreateOptionButton(
                    isCreateOptionsOpen = uiState.isCreateOptionsOpen,
                    isCreateOptionsAnimRequested = uiState.isCreateOptionsCloseAnimRequested,
                    openBundleCreator = { viewModel.openBundleCreator() },
                    openDeckCreator = { viewModel.openDeckCreatorDialog() },
                    onTooManyDecks = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Deck limit reached (maximum ${Constants.MAX_DECKS} decks).",
                                withDismissAction = true,
                            )
                        }
                    },
                    getNumDecks = {
                        var n = uiState.decks.size
                        for (bundle in uiState.bundles) {
                            n += bundle.decks.size
                        }
                        n
                    },
                    toggleCreateOptions = { viewModel.toggleCreateOptions() },
                    requestCloseCreateOptions = { viewModel.requestCloseCreateOptionsAnim() },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->

        var containerSize by remember { mutableStateOf(IntSize.Zero) }
        val bundleCloseAnim = animateFloatAsState(
            targetValue = if (uiState.isBundleCloseAnimRequested) 0f else if (isBundleOpen) 1f else 0f,
            animationSpec = tween(
                durationMillis = 250,
                easing = FastOutSlowInEasing,
            ),
            finishedListener = {
                if (uiState.isBundleCloseAnimRequested) viewModel.closeBundle()
            }
        )

        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        if (uiState.isCreateOptionsOpen) viewModel.toggleCreateOptions()
                    }
                }
                .padding(innerPadding)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size
                }
        ) {
            CardsList(
                onDeckOpened = { onDeckButtonClicked(it) },
                onDeckSelected = { viewModel.toggleDeckSelection(it) },
                getDeck = { viewModel.getDeck(it) },
                numDecks = viewModel.getNumDecks(),

                onBundleOpened = { viewModel.openBundle(it) },
                onBundleSelected = { viewModel.toggleBundleSelection(it) },
                getBundle = { viewModel.getBundle(it) ?: BundleWithDecks(Bundle(), listOf()) },
                numBundles = viewModel.getNumBundles(),

                moveDeckToBundle = { d, b ->
                    coroutineScope.launch {
                        viewModel.moveDeckToBundle(d, b)
                    }
                },
                mergeDecksIntoBundle = { d, b ->
                    coroutineScope.launch {
                        viewModel.mergeDecksIntoBundle(d, b)
                    }
                },
                mergeBundleWithBundle = { s, t ->
                    coroutineScope.launch {
                        viewModel.mergeBundleWithBundle(s, t)
                    }
                },

                isBundleCreatorOpen = uiState.isBundleCreatorOpen,
                isRemoveDeckFromBundleUiOpen = uiState.isRemoveDeckFromBundleUiOpen,
                cardIconSize = BOX_SIZE_DP,
                padding = dimensionResource(R.dimen.padding_medium),
                blur = bundleCloseAnim.value,
            )

            if (isBundleOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) { detectTapGestures { viewModel.requestCloseBundleAnim() } }
                )

                OpenBundle(
                    configuration = LocalConfiguration.current,
                    onDeckOpened = { onDeckButtonClicked(it) },
                    onDeckSelected = {
                        if (!uiState.isRemoveDeckFromBundleUiOpen && !uiState.isBundleCreatorOpen) {
                            viewModel.openRemoveDeckFromBundleUi()
                        }
                        viewModel.toggleDeckSelection(it)
                    },
                    getDeck = { viewModel.getDeckFromCurrentBundle(it) },
                    numDecks = viewModel.getNumDecksInCurrentBundle(),
                    getBundle = { viewModel.getBundle(it) ?: BundleWithDecks(Bundle(), listOf()) },
                    isBundleCreatorOpen = uiState.isBundleCreatorOpen,
                    isRemoveDeckFromBundleUiOpen = uiState.isRemoveDeckFromBundleUiOpen,
                    cardIconSize = BOX_SIZE_IN_BUNDLE_DP,
                    modifier = Modifier.alpha(bundleCloseAnim.value)
                )

                if (uiState.isBundleCloseAnimRequested) {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    if (uiState.isBundleCreatorDialogOpen) {
        CreateBundleDialog(
            onDismissRequest = { viewModel.closeBundleCreatorDialog() },
            onCreateClicked = {
                viewModel.closeBundleCreatorDialog()
                coroutineScope.launch {
                    viewModel.createBundle(it)
                    viewModel.closeBundleCreator()
                    viewModel.requestCloseBundleAnim()
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isDeckCreatorDialogOpen) {
        CreateDeckDialog(
            onDismissRequest = { viewModel.closeDeckCreatorDialog() },
            onCreateClicked = {
                viewModel.closeDeckCreatorDialog()
                viewModel.closeCreateOptions()
                coroutineScope.launch {
                    viewModel.createDeck(it)
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isEditBundleNameDialogOpen) {
        CreateBundleDialog(
            editMode = true,
            onDismissRequest = { viewModel.closeEditBundleNameDialog() },
            onCreateClicked = {
                viewModel.closeEditBundleNameDialog()
                coroutineScope.launch {
                    viewModel.updateCurrentBundleName(it)
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isCreateOptionsOpen) {
        BackHandler { viewModel.requestCloseCreateOptionsAnim() }

    } else if (uiState.isRemoveDeckFromBundleUiOpen && !uiState.isBundleCreatorOpen) {
        BackHandler { viewModel.closeRemoveDeckFromBundleUi()}

    } else if (isBundleOpen) {
        BackHandler { viewModel.requestCloseBundleAnim() }

    } else if (uiState.isBundleCreatorOpen) {
        BackHandler { viewModel.closeBundleCreator() }

    }
}

@Composable
fun CardsList(
    onDeckOpened: (Long) -> Unit,
    onDeckSelected: (Int) -> Unit,
    getDeck: (Int) -> Deck,
    numDecks: Int,

    onBundleOpened: (Int) -> Unit,
    onBundleSelected: (Int) -> Unit,
    getBundle: (Int) -> BundleWithDecks,
    numBundles: Int,

    moveDeckToBundle: (Int, Int) -> Unit,
    mergeDecksIntoBundle: (Int, Int) -> Unit,
    mergeBundleWithBundle: (Int, Int) -> Unit,

    isBundleCreatorOpen: Boolean,
    isRemoveDeckFromBundleUiOpen: Boolean,
    cardIconSize: Int,
    padding: Dp = dimensionResource(R.dimen.padding_medium),
    blur: Float = 0f,
) {

    val bundlePositions = remember { mutableStateMapOf<Int, Offset>() }
    val deckPositions = remember { mutableStateMapOf<Int, Offset>() }
    var draggingBundleIndex by remember { mutableStateOf<Int?>(null) }
    var draggingDeckIndex by remember { mutableStateOf<Int?>(null) }
    var highlightedBundleIndex by remember { mutableStateOf<Int?>(null) }
    var highlightedDeckIndex by remember { mutableStateOf<Int?>(null) }
    var isDropped by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val sizePx = with(density) { BOX_SIZE_DP.dp.toPx()/2 }.toInt()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur((12 * blur).dp)
    ) {

        val lazyGridState = rememberLazyGridState()
        val adjustedCardIconSize by remember { derivedStateOf {
            with(density) {
                lazyGridState.layoutInfo.visibleItemsInfo.getOrNull(0)?.size?.width?.toDp()?.value?.toInt()
            } ?: cardIconSize
        } }

        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Adaptive(minSize = (cardIconSize).dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            items(numBundles) { i ->
                DraggableComposable(
                    index = i,
                    onBundleOpened = onBundleOpened,
                    onBundleSelected = onBundleSelected,
                    getBundle = getBundle,
                    isBundleCreatorOpen = isBundleCreatorOpen,
                    isBundle = true,
                    size = adjustedCardIconSize,
                    setPosition = { bundlePositions[i] = it },
                    onDrag = { draggingBundleIndex = it },
                    onDrop = { isDropped = true },
                    isHighlighted = highlightedBundleIndex == i,
                    isClickEnabled = draggingBundleIndex == null,
                )
            }

            items(numDecks) { i ->
                DraggableComposable(
                    index = i,
                    onDeckOpened = onDeckOpened,
                    onDeckSelected = onDeckSelected,
                    getDeck = getDeck,
                    getBundle = getBundle,
                    isBundleCreatorOpen = isBundleCreatorOpen,
                    isRemoveDeckFromBundleUiOpen = isRemoveDeckFromBundleUiOpen,
                    isBundle = false,
                    size = adjustedCardIconSize,
                    setPosition = { deckPositions[i] = it },
                    onDrag = { draggingDeckIndex = it },
                    onDrop = { isDropped = true },
                    isHighlighted = highlightedDeckIndex == i,
                    isClickEnabled = draggingDeckIndex == null,
                )
            }

            if (!isBundleCreatorOpen) {

                highlightedBundleIndex = null
                highlightedDeckIndex = null

                // check for dragging bundles
                if (draggingBundleIndex != null) {
                    for (pos in bundlePositions) {
                        if (pos.key != draggingBundleIndex && iconOverlaps(
                                bundlePositions[draggingBundleIndex]!!,
                                pos.value,
                                sizePx
                            )
                        ) {

                            // bundle dropped on bundle - merge bundles
                            if (isDropped) {
                                mergeBundleWithBundle(draggingBundleIndex!!, pos.key)
                                bundlePositions.remove(draggingBundleIndex)
                                isDropped = false
                                draggingBundleIndex = null

                            } else highlightedBundleIndex = pos.key
                            break
                        }
                    }

                    if (isDropped) {
                        isDropped = false
                        draggingBundleIndex = null
                    }

                    // check for dragging decks
                } else if (draggingDeckIndex != null) {
                    var pass = false
                    for (pos in bundlePositions) {
                        if (iconOverlaps(deckPositions[draggingDeckIndex]!!, pos.value, sizePx)) {
                            pass = true

                            // deck dropped on bundle - move deck to bundle
                            if (isDropped) {
                                moveDeckToBundle(draggingDeckIndex!!, pos.key)
                                deckPositions.remove(draggingDeckIndex)
                                isDropped = false
                                draggingDeckIndex = null

                            } else highlightedBundleIndex = pos.key
                            break
                        }
                    }

                    if (!pass) {
                        for (pos in deckPositions) {
                            if (pos.key != draggingDeckIndex && iconOverlaps(
                                    deckPositions[draggingDeckIndex]!!,
                                    pos.value,
                                    sizePx
                                )
                            ) {

                                // deck dropped on deck - merge decks into bundle
                                if (isDropped) {
                                    mergeDecksIntoBundle(draggingDeckIndex!!, pos.key)
                                    deckPositions.remove(draggingDeckIndex)
                                    deckPositions.remove(pos.key)
                                    isDropped = false
                                    draggingDeckIndex = null

                                } else highlightedDeckIndex = pos.key
                                break
                            }
                        }
                    }

                    if (isDropped) {
                        isDropped = false
                        draggingDeckIndex = null
                    }
                }
            }
        }
    }
}

fun iconOverlaps(p1: Offset, p2: Offset, size: Int): Boolean {
    return p1.x+size > p2.x && p1.x < p2.x+size && p1.y+size > p2.y && p1.y < p2.y+size
}

@Composable
fun DraggableComposable(
    index: Int,
    onDeckOpened: ((Long) -> Unit)? = null,
    onDeckSelected: ((Int) -> Unit)? = null,
    getDeck: ((Int) -> Deck)? = null,
    onBundleOpened: ((Int) -> Unit)? = null,
    onBundleSelected: ((Int) -> Unit)? = null,
    getBundle: ((Int) -> BundleWithDecks)? = null,
    isBundleCreatorOpen: Boolean,
    isRemoveDeckFromBundleUiOpen: Boolean = false,
    isBundle: Boolean,
    size: Int,
    setPosition: (Offset) -> Unit = {},
    onDrag: (Int?) -> Unit = {},
    onDrop: () -> Unit = {},
    isHighlighted: Boolean = false,
    isClickEnabled: Boolean = true,
    isDeckInsideBundle: Boolean = false,
    alpha: Float = 1f,
) {

    var isDragging by remember { mutableStateOf(false) }
    var xOff by remember { mutableFloatStateOf(0f) }
    var yOff by remember { mutableFloatStateOf(0f) }
    val d = LocalDensity.current

    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(
                if (isBundle || isDeckInsideBundle) dimensionResource(R.dimen.padding_small)
                else dimensionResource(R.dimen.padding_medium_small)
            )
            .offset(x = (xOff / d.density).dp, y = (yOff / d.density).dp)
            .alpha(alpha * (if (isDragging) 0.5f else 1f))
            .zIndex(if (isDragging) 1f else 0f)
            .onGloballyPositioned { coordinates ->
                setPosition(coordinates.positionInRoot())
            }
            .pointerInput(Unit) {
                if (!isDeckInsideBundle && !isBundleCreatorOpen) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            isDragging = true
                            onDrag(index)
                        },
                        onDragEnd = {
                            isDragging = false
                            xOff = 0f
                            yOff = 0f
                            onDrop()
                        },
                        onDragCancel = {
                            isDragging = false
                            xOff = 0f
                            yOff = 0f
                            onDrag(null)
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        xOff += dragAmount.x
                        yOff += dragAmount.y
                    }
                }
            }
    ) {

        if (isBundle) {
            if (onBundleOpened != null && onBundleSelected != null && getBundle != null) {
                BundleComponent(
                    index = index,
                    onBundleOpened = onBundleOpened,
                    onBundleSelected = onBundleSelected,
                    getBundle = getBundle,
                    isHighlighted = isHighlighted,
                    isClickEnabled = isClickEnabled,
                )
            }
        } else {
            if (onDeckOpened != null && onDeckSelected != null && getDeck != null) {
                DeckComponent(
                    onDeckOpened = { onDeckOpened(it) },
                    onDeckSelected = { onDeckSelected(index) },
                    getDeck = { getDeck(index) },
                    isBundleCreatorOpen = isBundleCreatorOpen,
                    isHighlighted = isHighlighted,
                    isClickEnabled = isClickEnabled,
                    isRemoveDeckFromBundleUiOpen = isRemoveDeckFromBundleUiOpen,
                    isInBundle = isDeckInsideBundle,
                )
            }
        }
    }
}

@Composable
fun BundleComponent(
    index: Int,
    onBundleOpened: (Int) -> Unit,
    onBundleSelected: (Int) -> Unit,
    getBundle: (Int) -> BundleWithDecks,
    isHighlighted: Boolean,
    isClickEnabled: Boolean,
    ) {

    val bundleWithDeck = getBundle(index)
    val bundle = bundleWithDeck.bundle

    OutlinedButton(
        onClick = {
            onBundleOpened(index)
        },
        enabled = isClickEnabled,
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (bundle.isSelected) MaterialTheme.colorScheme.tertiary
                else if (isHighlighted) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = if (bundle.isSelected) MaterialTheme.colorScheme.tertiaryContainer
                else if (isHighlighted) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = if (bundle.isSelected) MaterialTheme.colorScheme.tertiary
                else if (isHighlighted) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = if (bundle.isSelected) MaterialTheme.colorScheme.tertiaryContainer
                else if (isHighlighted) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.tertiary,
        ),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            var masteryLevel = 0f
            if (bundleWithDeck.decks.isNotEmpty()) {
                for (deck in bundleWithDeck.decks) {
                    masteryLevel += deck.masteryLevel
                }
                masteryLevel /= bundleWithDeck.decks.size
            }

            Text(
                text = bundle.name,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                maxLines = 3,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${(masteryLevel*100).roundToInt()}%",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeckComponent(
    onDeckOpened: (Long) -> Unit,
    onDeckSelected: () -> Unit,
    getDeck: () -> Deck,
    isBundleCreatorOpen: Boolean = false,
    isHighlighted: Boolean,
    isClickEnabled: Boolean = true,
    isInBundle: Boolean = false,
    isRemoveDeckFromBundleUiOpen: Boolean = false,
    ) {

    val deck = getDeck()

    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = if (deck.isSelected) MaterialTheme.colorScheme.primaryContainer
                else if (isHighlighted) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.primary,
            contentColor = if (deck.isSelected) MaterialTheme.colorScheme.primary
                else if (isHighlighted) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = if (deck.isSelected) MaterialTheme.colorScheme.primaryContainer
                else if (isHighlighted) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.primary,
            disabledContentColor = if (deck.isSelected) MaterialTheme.colorScheme.primary
                else if (isHighlighted) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                enabled = isClickEnabled,
                onClick = {
                    if (isBundleCreatorOpen || (isInBundle && isRemoveDeckFromBundleUiOpen)) {
                        onDeckSelected()
                    } else {
                        onDeckOpened(deck.id)
                    }
                },
                onLongClick = {
                    if (isInBundle) {
                        onDeckSelected()
                    }
                },
            )

    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = deck.name,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                maxLines = 3,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${(deck.masteryLevel*100).roundToInt()}%",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun OpenBundle(
    modifier: Modifier = Modifier,
    numDecks: Int,
    configuration: Configuration,
    cardIconSize: Int,
    onDeckOpened: ((Long) -> Unit)? = null,
    onDeckSelected: ((Int) -> Unit)? = null,
    getDeck: ((Int) -> Deck)? = null,
    getBundle: ((Int) -> BundleWithDecks)? = null,
    isBundleCreatorOpen: Boolean,
    isRemoveDeckFromBundleUiOpen: Boolean,
) {

    val density = LocalDensity.current
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var containerPosition by remember { mutableStateOf(Offset.Zero) }

    val overlayWidth =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            (configuration.screenWidthDp - mediumPadding.value*2)*0.6f
        else
            configuration.screenWidthDp - mediumPadding.value*2
    val overlayHeight =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            configuration.screenHeightDp - mediumPadding.value*2
        else
            configuration.screenWidthDp - mediumPadding.value*2

    Card(
        modifier = modifier
            .padding(mediumPadding)
            .size(overlayWidth.dp, overlayHeight.dp)

    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(smallPadding)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size
                    containerPosition = coordinates.positionInRoot()
                }
        ) {

            val lazyGridState = rememberLazyGridState()
            val adjustedCardIconSize by remember { derivedStateOf {
                with(density) {
                    lazyGridState.layoutInfo.visibleItemsInfo.getOrNull(0)?.size?.width?.toDp()?.value?.toInt()
                } ?: cardIconSize
            } }

            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Adaptive(minSize = (cardIconSize).dp)
            ) {

                items(numDecks) { i ->
                    DraggableComposable(
                        index = i,
                        onDeckOpened = onDeckOpened,
                        onDeckSelected = onDeckSelected,
                        getDeck = getDeck,
                        getBundle = getBundle,
                        isBundleCreatorOpen = isBundleCreatorOpen,
                        isRemoveDeckFromBundleUiOpen = isRemoveDeckFromBundleUiOpen,
                        isBundle = false,
                        size = adjustedCardIconSize,
                        isHighlighted = false,
                        isClickEnabled = true,
                        isDeckInsideBundle = true,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopAppBar(
    onBackButtonClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Dashboard", overflow = TextOverflow.Ellipsis,) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleTopAppBar(
    onBackButtonClicked: () -> Unit,
    onEditBundleNameButtonClicked: () -> Unit,
    title: String,
) {
    TopAppBar(
        title = { Text(text = title, overflow = TextOverflow.Ellipsis,) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onEditBundleNameButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit bundle name"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleCreatorTopAppBar(
    numSelected: Int,
    closeBundleCreator: () -> Unit,
    onCreateClicked: () -> Unit,
    ) {

    val smallPadding = dimensionResource(R.dimen.padding_small)

    TopAppBar(
        title = { Text(text = "$numSelected Selected") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(
                onClick = { closeBundleCreator() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            Button(
                onClick = {
                    onCreateClicked()
                },
                enabled = numSelected > 0,
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Row() {
                    Text(
                        text = "Create",
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.size(smallPadding))
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done",
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveDeckFromBundleTopBar(
    numSelected: Int,
    closeRemoveDeckFromBundleUi: () -> Unit,
    onRemoveClicked: () -> Unit,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)

    TopAppBar(
        title = { Text(text = "$numSelected Selected") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(
                onClick = { closeRemoveDeckFromBundleUi() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            Button(
                onClick = {
                    onRemoveClicked()
                },
                enabled = numSelected > 0,
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Row() {
                    Text(
                        text = "Remove",
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.size(smallPadding))
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Remove",
                    )
                }
            }
        }
    )
}

@Composable
fun CreateOptionButton(
    isCreateOptionsOpen: Boolean,
    isCreateOptionsAnimRequested: Boolean,
    openBundleCreator: () -> Unit,
    openDeckCreator: () -> Unit,
    onTooManyDecks: () -> Unit,
    getNumDecks: () -> Int,
    toggleCreateOptions: () -> Unit,
    requestCloseCreateOptions: () -> Unit,
    ) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val optionOpenAnim = animateFloatAsState(
        targetValue = if (isCreateOptionsAnimRequested) 0f else if (isCreateOptionsOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            if (isCreateOptionsAnimRequested) toggleCreateOptions()
        }
    )

    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (isCreateOptionsOpen) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .offset(x = ((1f - optionOpenAnim.value) * 100).dp)
                    .alpha(optionOpenAnim.value)

            ) {
                FloatingActionButton(
                    onClick = {
                        if (getNumDecks() < Constants.MAX_DECKS) openDeckCreator()
                        else onTooManyDecks()
                    },
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = "Create new deck", modifier = Modifier.padding(smallPadding))
                }
                FloatingActionButton(
                    onClick = openBundleCreator,
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = "Create new bundle", modifier = Modifier.padding(smallPadding))
                }
            }
        }
        FloatingActionButton(
            onClick = {
                if (isCreateOptionsOpen) {
                    requestCloseCreateOptions()
                } else {
                    toggleCreateOptions()
                }
            },
            modifier = Modifier.padding(smallPadding)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Open",
                modifier = Modifier
                    .rotate(optionOpenAnim.value * -45f)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateBundleDialog(
    editMode: Boolean = false,
    onDismissRequest: () -> Unit,
    onCreateClicked: (String) -> Unit,
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
                    text = "Name for the bundle:",
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
                        )
                    }
                    OutlinedTextField(
                        value = userInput ?: "",
                        onValueChange = { setUserInput(if (it.length <= StringLength.SHORT.maxLength) it else it.substring(0..StringLength.SHORT.maxLength)) },
                        label = { Text("Bundle name") },
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
                                onCreateClicked(userInput)
                            }
                        },
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text(if (editMode) "Save" else "Create") }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateDeckDialog(
    onDismissRequest: () -> Unit,
    onCreateClicked: (String) -> Unit,
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
                    .fillMaxWidth()
                    .padding(mediumPadding)

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
                                onCreateClicked(userInput)
                            }
                        },
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text("Create") }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    //device = "spec:orientation=landscape,width=333dp,height=808dp"
)
@Composable
fun DashboardScreenPreview() {
    FlashcardsTheme {
        DashboardScreen(viewModel(), {}, {})
    }
}