package com.example.flashcards.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.R
import com.example.flashcards.ui.theme.FlashcardsTheme

@Composable
fun MainMenuScreen(
    onCardButtonClicked: () -> Unit,
    onTutorialButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
) {

    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding)
    ) {
        Spacer(Modifier.weight(0.7f))
        FilledTonalButton(
            onClick = { onCardButtonClicked() },
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .size(300.dp, 300.dp)
                .padding(mediumPadding)
            ) {
            Text(
                text = stringResource(R.string.main_menu_button_cards),
                fontSize = 48.sp,
                onTextLayout = {}
            )
        }
        Spacer(Modifier.weight(1.0f))
        Button(
            onClick = { onTutorialButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = stringResource(R.string.main_menu_button_tutorial),
                fontSize = 24.sp,
                onTextLayout = {}
            )
        }
        Button(
            onClick = { onSettingsButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            Text(
                text = stringResource(R.string.main_menu_button_settings),
                fontSize = 24.sp,
                onTextLayout = {}
            )
        }
        Spacer(Modifier.weight(0.4f))
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    FlashcardsTheme {
        MainMenuScreen({}, {}, {})
    }
}