package com.example.flashcards.ui.session

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.DeckWithCards
import com.example.flashcards.ui.AppViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SummaryScreen (
    viewModel: SessionViewModel,
    uiState: SessionUiState,
    coroutineScope: CoroutineScope,
    onExit: (Long) -> Unit,
) {

    val deck = viewModel.getCurrentDeck()

    var saveSessionData by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        val mediumPadding = dimensionResource(R.dimen.padding_medium)
        val circleSize = 220.dp

        Spacer(modifier = Modifier.weight(2f))
        Text(
            text = deck.deck.name,
            fontSize = 32.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            lineHeight = 34.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = "Session Summary",
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(bottom = mediumPadding)
        ) {
            Box(modifier = Modifier
                .height(circleSize)
                .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    progress = uiState.newMasteryLevel,
                    modifier = Modifier.size(circleSize),
                    strokeWidth = 12.dp,
                    trackColor = Color.LightGray,
                    color = Color.Green,
                    )
                CircularProgressIndicator(
                    progress = uiState.oldMasteryLevel,
                    modifier = Modifier.size(circleSize),
                    strokeWidth = 12.dp,
                    trackColor = Color(0,0,0,0),
                    color = Color.Blue,
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .size(circleSize)
                ) {
                    Text(
                        text = "${Math.round(uiState.newMasteryLevel*100)}%",
                        fontSize = 56.sp,
                    )
                    Text(
                        text = "+${Math.round((uiState.newMasteryLevel-uiState.oldMasteryLevel)*100)}%",
                        fontSize = 16.sp,
                        color = Color.Green,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = "Cards studied: ${deck.cards.size}",
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = "Perfect: ${uiState.numPerfect}",
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = {
                if (saveSessionData) {
                    coroutineScope.launch {
                        viewModel.applySessionData()
                        onExit(uiState.param)
                    }
                } else {
                    onExit(uiState.param)
                }
            }
        ) {
            Text(
                text = "Exit"
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Save session data",
                fontSize = 16.sp,
            )
            IconButton(onClick = { viewModel.toggleTipDialog() }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
            Checkbox(
                checked = saveSessionData,
                onCheckedChange = { saveSessionData = it },
            )
        }
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=393dp,height=808dp"
    //device = "spec:width=650dp,height=900dp"
    //device = "spec:orientation=landscape,width=393dp,height=808dp"
)
@Composable
fun SummaryScreenPreview() {
    FlashcardsTheme() {
        SummaryScreen(viewModel(factory = AppViewModelProvider.Factory), SessionUiState(), rememberCoroutineScope(), {})
    }
}