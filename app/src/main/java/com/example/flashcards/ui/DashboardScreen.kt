package com.example.flashcards.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.Bundle
import com.example.flashcards.data.Cards
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import com.example.flashcards.ui.theme.FlashcardsTheme
import kotlinx.coroutines.launch
import kotlin.math.ceil

private const val BOX_SIZE = 110
private const val BOX_SIZE_IN_BUNDLE = 100

private var isBundleOpen by mutableStateOf(false)

@Composable
fun DashboardScreen(
    viewModel: MenuViewModel,
    onDeckButtonClicked: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val cards = DataSource.cards

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val numColumn = (configuration.screenWidthDp / (BOX_SIZE + smallPadding.value)).toInt()

    DeckList(
        cards = cards,
        onDeckButtonClicked = onDeckButtonClicked,
        viewModel = viewModel,
        deckIconSize = BOX_SIZE,
        blur = isBundleOpen,
        numColumn = numColumn,
        numFullRows = (cards.size / numColumn).toInt(),
        numInLastRow = cards.size % numColumn,
    )

    if (isBundleOpen) {
        OpenBundle(
            configuration = configuration,
            viewModel = viewModel,
            onDeckButtonClicked = onDeckButtonClicked,
        )
    }
}

@Composable
fun DraggableComposable(
    cards: Cards,
    onDeckButtonClicked: () -> Unit,
    viewModel: MenuViewModel,
    size: Int,
) {

    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(dimensionResource(R.dimen.padding_small))
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
    Button(
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
                numColumn = numColumn,
                numFullRows = (currentDeck.size / numColumn).toInt(),
                numInLastRow = currentDeck.size % numColumn,
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

    Button(
        onClick = {
            viewModel.selectDeck(deck)
            onDeckButtonClicked()
        },
        shape = RoundedCornerShape(10),
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

@Composable
fun DeckList(
    cards: List<Cards>,
    onDeckButtonClicked: () -> Unit,
    viewModel: MenuViewModel,
    deckIconSize: Int,
    padding: Dp = dimensionResource(R.dimen.padding_medium),
    blur: Boolean = false,
    numColumn: Int,
    numFullRows: Int,
    numInLastRow: Int,
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .blur(if (blur) 12.dp else 0.dp)
    ) {

        for (i in 0..<numFullRows) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (j in 0..<numColumn) {
                    DraggableComposable(
                        cards = cards[numColumn * i + j],
                        onDeckButtonClicked = onDeckButtonClicked,
                        viewModel = viewModel,
                        size = deckIconSize,
                    )
                }
            }
        }

        if (numInLastRow > 0) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (i in 0..<numInLastRow)
                    DraggableComposable(
                        cards = cards[numColumn * numFullRows+i],
                        onDeckButtonClicked = onDeckButtonClicked,
                        viewModel = viewModel,
                        size = deckIconSize,
                    )
                for (i in numInLastRow..<numColumn)
                    EmptyComponent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    FlashcardsTheme {
        DashboardScreen(viewModel()) {}
    }
}