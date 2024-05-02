package com.example.flashcards.ui.mainMenu

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.ui.AppViewModelProvider
import com.example.flashcards.ui.theme.FlashcardsTheme

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAllCardsButtonClicked: () -> Unit,
    onPriorityDecksButtonClicked: () -> Unit,
    onLanguageButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val allCardsBtnAnim = animateFloatAsState(
        targetValue = if (uiState.allCardsBtnAnimRequested) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            if (uiState.allCardsBtnAnimRequested) {
                onAllCardsButtonClicked()
            }
        }
    )

    val priorityDecksBtnAnim = animateFloatAsState(
        targetValue = if (uiState.priorityDecksBtnAnimRequested) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            if (uiState.priorityDecksBtnAnimRequested) {
                onPriorityDecksButtonClicked()
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadSettings(context, configuration)
        viewModel.softReset()
    }

    BackHandler {
        viewModel.openCloseDialog()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.weight(0.7f))
        FilledTonalButton(
            onClick = { viewModel.requestAllCardsBtnAnim() },
            shape = RoundedCornerShape(percent = (10 * (1 - allCardsBtnAnim.value).toInt())),
            modifier = Modifier
                .size(
                    width = 300.dp + ((configuration.screenWidthDp - 300) * allCardsBtnAnim.value).dp,
                    height = (300 * (1-priorityDecksBtnAnim.value)).dp + ((configuration.screenHeightDp - 300) * allCardsBtnAnim.value).dp
                )
                .padding(mediumPadding * (1f - allCardsBtnAnim.value - priorityDecksBtnAnim.value))
            ) {
            Text(
                text = stringResource(id = R.string.mms_cards),
                fontSize = 48.sp,
                lineHeight = 52.sp,
                textAlign = TextAlign.Center,
            )
        }

        FilledTonalButton(
            onClick = { viewModel.requestPriorityDecksBtnAnim() },
            shape = RoundedCornerShape(percent = (20 * (1 - priorityDecksBtnAnim.value).toInt())),
            modifier = Modifier
                .size(
                    width = 300.dp + ((configuration.screenWidthDp - 300) * priorityDecksBtnAnim.value).dp,
                    height = 150.dp + ((configuration.screenHeightDp - 150) * priorityDecksBtnAnim.value).dp
                )
                .padding(mediumPadding * (1f - priorityDecksBtnAnim.value))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.mms_priority),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(smallPadding))
                Text(
                    text = "${uiState.numPriorityDecks} " + stringResource(id = R.string.mms_priority_remaining),
                    fontSize = 32.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(Modifier.weight(1.0f))
        Button(
            onClick = { onSettingsButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = stringResource(id = R.string.mms_settings),
                fontSize = 24.sp,
                onTextLayout = {}
            )
        }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = stringResource(id = R.string.mms_credits),
                fontSize = 24.sp,
                onTextLayout = {}
            )
        }
        Spacer(Modifier.weight(0.4f))
    }

    if (uiState.isCloseDialogOpen) {
        CloseDialog(
            onDismissRequest = { viewModel.closeCloseDialog() },
            onCloseButtonClicked = {
                val activity = (context as? Activity)
                activity?.finish()
            }
        )
    }
}

@Composable
fun CloseDialog(
    onDismissRequest: () -> Unit,
    onCloseButtonClicked: () -> Unit,
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
                    text = stringResource(id = R.string.mms_d_close),
                    fontSize = 24.sp,
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
                    ) { Text(stringResource(id = R.string.cancel)) }
                    Button(
                        onClick = onCloseButtonClicked,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text(stringResource(id = R.string.close)) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    FlashcardsTheme {
        MainMenuScreen(viewModel(), {}, {}, {}, {})
    }
}