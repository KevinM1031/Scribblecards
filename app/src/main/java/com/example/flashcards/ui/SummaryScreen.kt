package com.example.flashcards.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.flashcards.data.Card
import com.example.flashcards.data.DataSource
import com.example.flashcards.data.Deck
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun SummaryScreen (
    viewModel: SessionViewModel,
    onBackButtonClicked: () -> Unit,
) {

    //TODO delete below
    var isSetupDone by remember { mutableStateOf(false) }
    if (!isSetupDone) {
        viewModel.setup("0")
        isSetupDone = true
    }

    val uiState by viewModel.uiState.collectAsState()
    val deck = viewModel.getCurrentDeck()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        val mediumPadding = dimensionResource(R.dimen.padding_medium)
        val circleSize = 240.dp

        val numCards = deck.cards.size
        val numPerfect = viewModel.getNumPerfect()
        val oldMasteryLevel = numPerfect.toFloat()/numCards + 0.8f
        val newMasteryLevel = numPerfect.toFloat()/numCards + 0.94f

        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = deck.data.name,
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
                    progress = newMasteryLevel,
                    modifier = Modifier.size(circleSize),
                    strokeWidth = 12.dp,
                )
                CircularProgressIndicator(
                    progress = oldMasteryLevel,
                    modifier = Modifier.size(circleSize),
                    strokeWidth = 12.dp,
                    trackColor = Color.Gray
                )
                Box(modifier = Modifier
                    .size(circleSize)
                    .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = "${Math.round(newMasteryLevel*100)}%",
                        fontSize = 80.sp,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Total Cards: $numCards",
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = "Perfect: $numPerfect",
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
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
        SummaryScreen(viewModel(), {})
    }
}