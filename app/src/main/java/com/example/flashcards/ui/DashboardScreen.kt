package com.example.flashcards.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val BOX_SIZE = 110
private const val BOX_SIZE_IN_BUNDLE = 100

private var isBundleOpen by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MenuViewModel,
    onDeckButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val smallPadding = dimensionResource(R.dimen.padding_small)

    var showAddOptions by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
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
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                if (showAddOptions) {
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
                                viewModel.createBundle("New Bundle")
                            },
                            modifier = Modifier.padding(smallPadding)
                        ) {
                            Text(text = "Create new bundle", modifier = Modifier.padding(smallPadding))
                        }
                    }
                }
                FloatingActionButton(
                    onClick = {
                        showAddOptions = !showAddOptions
                    },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
    ) { inderPadding ->

        Box(
            modifier = Modifier.padding(inderPadding)
        ) {
            DeckList(
                onDeckButtonClicked = onDeckButtonClicked,
                viewModel = viewModel,
                deckIconSize = BOX_SIZE,
                blur = isBundleOpen,
                cards = viewModel.uiState.collectAsState().value.cards,
            )

            if (isBundleOpen) {
                OpenBundle(
                    configuration = configuration,
                    viewModel = viewModel,
                    onDeckButtonClicked = onDeckButtonClicked,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableComposable(
    cards: Cards,
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
        if (cards.isBundle()) {
            BundleComponent(
                bundle = cards.toBundle(),
                viewModel = viewModel,
            )
        } else {
            DeckComponent(
                deck = cards.toDeck(),
                onDeckButtonClicked = onDeckButtonClicked,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
fun BundleComponent(
    bundle: Bundle,
    viewModel: MenuViewModel,
) {
    OutlinedButton(
        onClick = {
            viewModel.selectBundle(bundle)
            isBundleOpen = true
        },
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondary,
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
) {

    // overall constants
    val uiState by viewModel.uiState.collectAsState()
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val overlayHeight = configuration.screenWidthDp.dp-mediumPadding
    val decks = uiState.currentBundle!!.decks

    // deck list constants
    val heightLimit = overlayHeight.value.toInt()
    val numColumn = (configuration.screenWidthDp / (BOX_SIZE + smallPadding.value)).toInt()
    val numRows = (heightLimit / (BOX_SIZE + smallPadding.value)).toInt()
        .coerceAtMost((decks.size / numColumn).toInt())
    val decksPerPage = numColumn*numRows

    val pagerState = rememberPagerState(pageCount = {
        ceil((decks.size.toDouble()/decksPerPage)).toInt()
    })

    Button(
        shape = RoundedCornerShape(0),
        onClick = { isBundleOpen = false },
        modifier = Modifier
            .fillMaxSize()
            .alpha(0f)
    ) {}

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

            DeckList(
                cards = currentDeck,
                viewModel = viewModel,
                onDeckButtonClicked = onDeckButtonClicked,
                deckIconSize = BOX_SIZE_IN_BUNDLE,
            )
        }
    }
}

@Composable
fun DeckComponent(
    deck: Deck,
    onDeckButtonClicked: () -> Unit,
    viewModel: MenuViewModel,
) {

    OutlinedButton(
        onClick = {
            viewModel.selectDeck(deck)
            onDeckButtonClicked()
        },
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondary,
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
fun DeckList(
    cards: List<Cards>,
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

        for (i in 1..cards.size) {
            DraggableComposable(
                cards = cards[i-1],
                onDeckButtonClicked = onDeckButtonClicked,
                viewModel = viewModel,
                size = deckIconSize,
            )
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