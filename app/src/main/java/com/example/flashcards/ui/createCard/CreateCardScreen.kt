package com.example.flashcards.ui.createCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.Constants
import com.example.flashcards.data.StringLength
import com.example.flashcards.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCardScreen (
    viewModel: CreateCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackButtonClicked: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Create Card",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = mediumPadding)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = onBackButtonClicked,
                                modifier = Modifier.size(120.dp, 40.dp)
                            ) { Text("Cancel") }
                            Button(
                                enabled = uiState.questionTextInput.isNotBlank() && uiState.answerTextInput.isNotBlank(),
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.createCard()
                                        onBackButtonClicked()
                                    }
                                },
                                modifier = Modifier.size(120.dp, 40.dp)
                            ) { Text("Create") }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(smallPadding)
            ) {
                Spacer(modifier = Modifier.height(mediumPadding))
                CustomTextField(
                    text = "Question",
                    value = uiState.questionTextInput,
                    onValueChange = { viewModel.setQuestionTextInput(it) },
                    label = "Question text",
                    focusManager = focusManager,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
                CustomTextField(
                    text = "Answer",
                    value = uiState.answerTextInput,
                    onValueChange = { viewModel.setAnswerTextInput(it) },
                    label = "Answer text",
                    focusManager = focusManager,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
                CustomTextField(
                    text = "Hint (optional)",
                    value = uiState.hintTextInput,
                    onValueChange = { viewModel.setHintTextInput(it) },
                    label = "Hint text",
                    focusManager = focusManager,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
                CustomTextField(
                    text = "Example (optional)",
                    value = uiState.exampleTextInput,
                    onValueChange = { viewModel.setExampleTextInput(it) },
                    label = "Example text",
                    focusManager = focusManager,
                    isLast = true,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTextField(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 5,
    focusManager: FocusManager,
    isLast: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = " - this field is required.",
    ) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text + if (isError) errorMessage else "",
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(if (it.length <= StringLength.LONG.maxLength) it else it.substring(0..StringLength.LONG.maxLength)) },
            label = { Text(text = label) },
            isError = isError,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(imeAction = if (isLast) ImeAction.Done else ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(if (isLast) FocusDirection.Exit else FocusDirection.Down)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=393dp,height=808dp"
    //device = "spec:width=650dp,height=900dp"
    //device = "spec:orientation=landscape,width=393dp,height=808dp"
)
@Composable
fun CreateCardScreenPreview() {
    FlashcardsTheme() {
        CreateCardScreen(viewModel(), {})
    }
}