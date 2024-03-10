package com.example.flashcards.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.flashcards.R

@Composable
fun Loading() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.dice_1),
            contentDescription = null
        )
        Spacer(Modifier.weight(0.3f))
        Text(
            text = stringResource(R.string.loading_title),
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(2f))
    }
}