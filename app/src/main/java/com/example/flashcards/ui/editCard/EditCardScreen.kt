package com.example.flashcards.ui.editCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardScreen (
    viewModel: EditCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
                        text = "Edit Card",
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
    ) { innerPadding ->

        var clearCardHistory by remember { mutableStateOf(false) }

        var isQuestionError by remember { mutableStateOf(false) }
        var isAnswerError by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(mediumPadding))
            CustomTextField(
                text = "Question",
                value = uiState.questionTextInput,
                onValueChange = { viewModel.setQuestionTextInput(it) },
                label = "Question text",
                focusManager = focusManager,
                isError = isQuestionError,
                modifier = Modifier
                    .padding(vertical = smallPadding, horizontal = mediumPadding)
            )
            CustomTextField(
                text = "Answer",
                value = uiState.answerTextInput,
                onValueChange = { viewModel.setAnswerTextInput(it) },
                label = "Answer text",
                focusManager = focusManager,
                isError = isAnswerError,
                modifier = Modifier
                    .padding(vertical = smallPadding, horizontal = mediumPadding)
            )
            CustomTextField(
                text = "Hint (optional)",
                value = uiState.hintTextInput,
                onValueChange = { viewModel.setHintTextInput(it) },
                label = "Hint text",
                focusManager = focusManager,
                modifier = Modifier
                    .padding(vertical = smallPadding, horizontal = mediumPadding)
            )
            CustomTextField(
                text = "Example (optional)",
                value = uiState.exampleTextInput,
                onValueChange = { viewModel.setExampleTextInput(it) },
                label = "Example text",
                focusManager = focusManager,
                isLast = true,
                modifier = Modifier
                    .padding(vertical = smallPadding, horizontal = mediumPadding)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Clear card history",
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                Checkbox(
                    checked = clearCardHistory,
                    onCheckedChange = { clearCardHistory = it },
                )
            }
            Button(
                onClick = {
                    var isError = false
                    if (uiState.questionTextInput.isBlank()) {
                        isQuestionError = true
                        isError = true
                    }
                    if (uiState.answerTextInput.isBlank()) {
                        isAnswerError = true
                        isError = true
                    }
                    if (!isError) {
                        coroutineScope.launch {
                            if (clearCardHistory) {
                                viewModel.clearCardHistory()
                            }
                            viewModel.updateCard()
                            onBackButtonClicked()
                        }
                    }
                },
                modifier = Modifier
                    .size(160.dp, 80.dp)
                    .padding(vertical = mediumPadding)
            ) {
                Text(
                    text = "Confirm"
                )
            }
            Spacer(modifier = Modifier.height(mediumPadding))
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
            modifier = modifier
                .fillMaxWidth()
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
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
            modifier = modifier
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
        EditCardScreen(viewModel(), {})
    }
}