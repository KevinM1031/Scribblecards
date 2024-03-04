package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ArtSpaceApp(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ArtSpaceApp(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.dice_1),
            contentDescription = null,
            modifier = Modifier.padding(0.dp, 8.dp)
        )

        Spacer(modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Artwork Title",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text (
                text = "Artwork Artist (Year)",
                fontSize = 10.sp
            )
        }

        Spacer(modifier.weight(0.5f))

        Row(
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            Button(
                onClick = {  },
                modifier = Modifier.size(80.dp, 20.dp)

            ) {
                Text (
                    text = "Previous",
                    fontSize = 8.sp
                )
            }
            Spacer(modifier.weight(0.5f))
            Button (
                onClick = {  },
                modifier = Modifier.size(160.dp, 40.dp)

            ) {
                Text (
                    text = "Next",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpaceAppPreview() {
    MyApplicationTheme {
        ArtSpaceApp()
    }
}