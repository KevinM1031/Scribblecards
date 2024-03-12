package com.example.flashcards.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Cards
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import com.example.flashcards.ui.theme.FlashcardsTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val BOX_SIZE = 110
private const val BOX_SIZE_IN_BUNDLE = 100

@Composable
fun DashboardScreen(
    viewModel: MenuViewModel,
    onDeckButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val cardsFlow = remember { MutableStateFlow(viewModel.uiState.value.cards) }

    Scaffold(
        topBar = {
            if (viewModel.isBundleCreatorOpen) {
                BundleCreatorTopAppBar(viewModel = viewModel)
            } else {
                DashboardTopAppBar(onBackButtonClicked = onBackButtonClicked)
            }
        },
        floatingActionButton = {
            if (!viewModel.isBundleCreatorOpen) {
                CreateOptionButton(viewModel = viewModel)
            }
        },
    ) { inderPadding ->

        Box(
            modifier = Modifier
                .padding(inderPadding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { if (viewModel.isBundleOpen) viewModel.closeBundle() }
                    }
            )

            CardsList(
                onDeckButtonClicked = onDeckButtonClicked,
                viewModel = viewModel,
                deckIconSize = BOX_SIZE,
                blur = viewModel.isBundleOpen,
                cardsFlow = cardsFlow,
                onCardsChange = { modifiedCard, index ->
                    cardsFlow.value = cardsFlow.value.toMutableList().apply {
                        this[index] = modifiedCard
                    }
                }
            )

            if (viewModel.isBundleOpen) {
                OpenBundle(
                    configuration = configuration,
                    viewModel = viewModel,
                    onDeckButtonClicked = onDeckButtonClicked,
                    cardsFlow = cardsFlow,
                    onCardsChange = { modifiedCard, index ->
                        cardsFlow.value = cardsFlow.value.toMutableList().apply {
                            this[index] = modifiedCard
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DraggableComposable(
    cardsFlow: StateFlow<List<Cards>>,
    cardsIndex: Int,
    onCardsChange: (Cards, Int) -> Unit,
    onDeckButtonClicked: () -> Unit,
    viewModel: MenuViewModel,
    size: Int,
) {

    var isDragging by remember { mutableStateOf(false) }
    var xOff by remember { mutableStateOf(0f) }
    var yOff by remember { mutableStateOf(0f) }
    val d = LocalDensity.current

    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(dimensionResource(R.dimen.padding_small))
            .offset(x = (xOff / d.density).dp, y = (yOff / d.density).dp)
            .alpha(if (isDragging) 0.5f else 1f)
            .zIndex(if (isDragging) 1f else 0f)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                        xOff = 0f
                        yOff = 0f
                    },
                    onDragCancel = {
                        isDragging = false
                        xOff = 0f
                        yOff = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    xOff += dragAmount.x
                    yOff += dragAmount.y
                }
            }
    ) {

        val cards = cardsFlow.collectAsState().value[cardsIndex]

        if (cards.isBundle()) {
            BundleComponent(
                cardsFlow = cardsFlow,
                bundleIndex = cardsIndex,
                onBundleChange = onCardsChange,
                viewModel = viewModel,
            )
        } else {
            DeckComponent(
                cardsFlow = cardsFlow,
                deckIndex = cardsIndex,
                onDeckChange = onCardsChange,
                onDeckButtonClicked = onDeckButtonClicked,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
fun BundleComponent(
    cardsFlow: StateFlow<List<Cards>>,
    bundleIndex: Int,
    onBundleChange: (Cards, Int) -> Unit,
    viewModel: MenuViewModel,
) {

    val bundle = cardsFlow.collectAsState().value[bundleIndex]
    var modifiedBundle by remember { mutableStateOf(bundle) }

    OutlinedButton(
        onClick = {
            viewModel.openBundle(bundleIndex)
        },
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (bundle.isSelected()) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = if (bundle.isSelected()) MaterialTheme.colorScheme.tertiaryContainer
                else MaterialTheme.colorScheme.tertiary,
        ),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = bundle.name,
            textAlign = TextAlign.Center,
            onTextLayout = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OpenBundle(
    configuration: Configuration,
    onDeckButtonClicked: () -> Unit,
    viewModel: MenuViewModel,
    cardsFlow: StateFlow<List<Cards>>,
    onCardsChange: (Cards, Int) -> Unit,
) {
/*
    val cards = cardsFlow.collectAsState().value

    // overall constants
    val uiState by viewModel.uiState.collectAsState()
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val overlayHeight = configuration.screenWidthDp.dp-mediumPadding

    // deck list constants
    val heightLimit = overlayHeight.value.toInt()
    val numColumn = (configuration.screenWidthDp / (BOX_SIZE + smallPadding.value)).toInt()
    val numRows = (heightLimit / (BOX_SIZE + smallPadding.value)).toInt()
        .coerceAtMost((cards[i].size / numColumn).toInt())
    val decksPerPage = numColumn*numRows

    val pagerState = rememberPagerState(pageCount = {
        ceil((decks.size.toDouble()/decksPerPage)).toInt()
    })

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(overlayHeight + mediumPadding * 2)
            .padding(mediumPadding)

    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(overlayHeight + mediumPadding * 2)
        ) { page ->

            val currentDeck = decks.slice(
                page * decksPerPage..<(
                        if (page + 1 < pagerState.pageCount) (page + 1) * decksPerPage
                        else decks.size
                        )
            )

            CardsList(
                cards = currentDeck,
                viewModel = viewModel,
                onDeckButtonClicked = onDeckButtonClicked,
                deckIconSize = BOX_SIZE_IN_BUNDLE,
            )
        }
    }

 */
}

@Composable
fun DeckComponent(
    cardsFlow: StateFlow<List<Cards>>,
    deckIndex: Int,
    onDeckChange: (Cards, Int) -> Unit,
    onDeckButtonClicked: () -> Unit,
    viewModel: MenuViewModel,
) {

    val deck = cardsFlow.collectAsState().value[deckIndex]
    val modifiedDeck by remember { mutableStateOf(deck) }
    var isPressed by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = {
            isPressed = modifiedDeck.isSelected()
            if (viewModel.isBundleCreatorOpen) {
                modifiedDeck.toggleSelection()
                onDeckChange(modifiedDeck, deckIndex)
                val x = modifiedDeck.isSelected()
                Log.d("Test", "$x")
            } else {
                viewModel.openDeck(deckIndex)
                onDeckButtonClicked()
            }
        },
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.primary,
            contentColor = if (isPressed) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primaryContainer,
        ),
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text(
            text = deck.name,
            textAlign = TextAlign.Center,
            onTextLayout = {}
        )
    }
}

@Composable
fun EmptyComponent() {
    Spacer(
        modifier = Modifier
            .size(BOX_SIZE.dp)
            .padding(dimensionResource(R.dimen.padding_small))
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardsList(
    cardsFlow: StateFlow<List<Cards>>,
    onCardsChange: (Cards, Int) -> Unit,
    onDeckButtonClicked: () -> Unit,
    viewModel: MenuViewModel,
    deckIconSize: Int,
    padding: Dp = dimensionResource(R.dimen.padding_medium),
    blur: Boolean = false,
) {

    FlowRow(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .blur(if (blur) 12.dp else 0.dp)
    ) {

        val cards = cardsFlow.collectAsState().value

        for (i in 1..cards.size) {
            DraggableComposable(
                cardsFlow = cardsFlow,
                cardsIndex = i-1,
                onCardsChange = onCardsChange,
                onDeckButtonClicked = onDeckButtonClicked,
                viewModel = viewModel,
                size = deckIconSize,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopAppBar(
    onBackButtonClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Dashboard") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleCreatorTopAppBar(
    viewModel: MenuViewModel
) {
    val numSelected = viewModel.numSelectedDecks + viewModel.numSelectedBundles

    TopAppBar(
        title = { Text(text = "$numSelected Selected") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = {
                viewModel.closeBundleCreator()
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = ""
                )
            }
        }
    )
}

@Composable
fun CreateOptionButton(
    viewModel: MenuViewModel
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (viewModel.isCreateOptionsOpen) {
            Column(
                horizontalAlignment = Alignment.End

            ) {
                FloatingActionButton(
                    onClick = {

                    },
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = "Create new deck", modifier = Modifier.padding(smallPadding))
                }
                FloatingActionButton(
                    onClick = {
                        viewModel.openBundleCreator()
                    },
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = "Create new bundle", modifier = Modifier.padding(smallPadding))
                }
            }
        }
        FloatingActionButton(
            onClick = {
                viewModel.toggleCreateOptions()
            },
            modifier = Modifier.padding(smallPadding)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    FlashcardsTheme {
        DashboardScreen(viewModel(), {}, {})
    }
}