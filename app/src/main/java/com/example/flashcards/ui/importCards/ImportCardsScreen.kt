package com.example.flashcards.ui.importCards

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.ui.AppViewModelProvider
import com.example.flashcards.ui.deck.ImportCardsViewModel
import com.example.flashcards.ui.deck.SubDeck
import com.example.flashcards.ui.deck.SubDeckType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImportCardsScreen (
    viewModel: ImportCardsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackButtonClicked: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    val text =
                        if (uiState.isBringFromDecksScreenOpen) "Bring from decks"
                        else if (uiState.isImportThroughTextScreenOpen) "Import through copied text"
                        else if (uiState.isUploadCsvFileScreenOpen) "Upload .CSV file"
                        else "Import cards"
                    Text(
                        text = text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.isBringFromDecksScreenOpen)
                            viewModel.toggleBringFromDecksScreen()
                        else if (uiState.isImportThroughTextScreenOpen)
                            viewModel.toggleImportThroughTextScreen()
                        else if (uiState.isUploadCsvFileScreenOpen)
                            viewModel.toggleUploadCsvFileScreen()
                        else onBackButtonClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (uiState.isImportThroughTextScreenOpen || uiState.isUploadCsvFileScreenOpen) {
                        IconButton(onClick = { viewModel.toggleTip() }) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Tip",
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (!uiState.isBringFromDecksScreenOpen && !uiState.isImportThroughTextScreenOpen && !uiState.isUploadCsvFileScreenOpen) {
                Column() {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                        actions = {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModel.importAll()
                                            viewModel.reset()
                                            onBackButtonClicked()
                                        }
                                    },
                                    enabled = uiState.subDecks.isNotEmpty(),
                                    modifier = Modifier
                                        .width(160.dp),
                                ) {
                                    Text(
                                        text = "Import all",
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp,
                                    )
                                }
                            }
                        }
                    )
                }
            }
        },
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {

            val smallPadding = dimensionResource(R.dimen.padding_small)
            val mediumPadding = dimensionResource(R.dimen.padding_medium)

            if (uiState.isBringFromDecksScreenOpen) {
                BackHandler { viewModel.toggleBringFromDecksScreen() }

                BringFromDecksScreen(
                    onDismissRequest = { viewModel.toggleBringFromDecksScreen() },
                    bundles = uiState.bundles,
                    decks = uiState.decks,
                    excludeMastered = uiState.excludeMastered,
                    resetHistory = uiState.resetHistory,
                    onSelectClicked = {
                        coroutineScope.launch {
                            val cards = viewModel.getAllCardsFromDeck(it)
                            viewModel.toggleBringFromDecksScreen()
                            viewModel.addSubDeck(
                                SubDeck(
                                    name = "Import from \"${it.name}\" (${cards.size} cards)",
                                    type = SubDeckType.DEFAULT,
                                    cards = cards,
                                )
                            )
                        }
                    },
                    setExcludeMastered = { viewModel.setExcludeMastered(it) },
                    setResetHistory = { viewModel.setResetHistory(it) },
                )

            } else if (uiState.isImportThroughTextScreenOpen) {
                BackHandler { viewModel.toggleImportThroughTextScreen() }

                ImportThroughTextScreen(
                    onDismissRequest = { viewModel.toggleImportThroughTextScreen() },
                    onCreateClicked = {
                        viewModel.toggleImportThroughTextScreen()
                        val cards = viewModel.textToCards()
                        viewModel.addSubDeck(
                            SubDeck(
                                name = "Import from text (${cards.size} cards)",
                                type = SubDeckType.TEXT,
                                cards = cards,
                            )
                        )
                    },
                    getPreviewCards = { viewModel.textToCards(maxCards = 2) },
                    setInputText = { viewModel.setInputText(it) },
                    setQuestionLines = { viewModel.setQuestionLines(it) },
                    setAnswerLines = { viewModel.setAnswerLines(it) },
                    setHintLines = { viewModel.setHintLines(it) },
                    setExampleLines = { viewModel.setExampleLines(it) },
                    setIgnoredLines = { viewModel.setIgnoredLines(it) },
                    inputText = uiState.inputText,
                    questionLines = uiState.questionLines,
                    answerLines = uiState.answerLines,
                    hintLines = uiState.hintLines,
                    exampleLines = uiState.exampleLines,
                    ignoredLines = uiState.ignoredLines,
                    focusManager = focusManager,
                )

            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .padding(top = smallPadding, start = smallPadding, end = smallPadding)
                            .combinedClickable(
                                onClick = { viewModel.toggleBringFromDecksScreen() },
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mediumPadding)
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Bring from other decks",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(mediumPadding))
                            Text(
                                text = "Bring from other decks",
                                fontSize = 20.sp,
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .padding(top = smallPadding, start = smallPadding, end = smallPadding)
                            .combinedClickable(
                                onClick = { viewModel.toggleImportThroughTextScreen() },
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mediumPadding)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Import through text",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(mediumPadding))
                            Text(
                                text = "Import through copied text",
                                fontSize = 20.sp,
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .padding(top = smallPadding, start = smallPadding, end = smallPadding)
                            .combinedClickable(
                                onClick = { viewModel.toggleUploadCsvFileScreen() },
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mediumPadding)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Upload .CSV file",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(mediumPadding))
                            Text(
                                text = "Upload .CSV file",
                                fontSize = 20.sp,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(smallPadding))
                    Divider()

                    LazyColumn {
                        items(uiState.subDecks.size) { i ->
                            val subDeck = uiState.subDecks[i]
                            Card(
                                modifier = Modifier
                                    .height(64.dp)
                                    .fillMaxWidth()
                                    .padding(
                                        top = smallPadding,
                                        start = smallPadding,
                                        end = smallPadding
                                    )
                                    .combinedClickable(
                                        onClick = { },
                                    )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            top = mediumPadding,
                                            start = mediumPadding,
                                            bottom = mediumPadding
                                        )
                                ) {
                                    Text(
                                        text = subDeck.name,
                                        fontSize = 20.sp,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(
                                        onClick = { viewModel.removeSubDeck(subDeck) },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Delete",
                                        )
                                    }
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(smallPadding)) }
                    }
                }
            }
        }
    }

    if (uiState.isTipOpen) {
        val tip =
            if (uiState.isImportThroughTextScreenOpen)
                "Copy and paste the raw text from other decks."
            else if (uiState.isUploadCsvFileScreenOpen)
                "Upload .CSV spreadsheet file."
            else "How did you open this dialog??"
        TipDialog(
            onDismissRequest = { viewModel.toggleTip() },
            tip = tip,
        )
    }
}

@Composable
fun ImportThroughTextScreen(
    onDismissRequest: () -> Unit,
    onCreateClicked: () -> Unit,
    getPreviewCards: () -> List<Card>,
    setInputText: (String) -> Unit,
    setQuestionLines: (String) -> Unit,
    setAnswerLines: (String) -> Unit,
    setHintLines: (String) -> Unit,
    setExampleLines: (String) -> Unit,
    setIgnoredLines: (String) -> Unit,
    inputText: String?,
    questionLines: String?,
    answerLines: String?,
    hintLines: String?,
    exampleLines: String?,
    ignoredLines: String?,
    focusManager: FocusManager,
) {
    val inputTextUiNumLines = 8

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    /**
     * 0 - no error
     * 1 - input text is empty
     * 2 - found trailing values (incomplete text)
     * 3 - input text is too long
     * 10 - question lines are not set
     * 20 - answer lines are not set
     */
    var isError by remember { mutableStateOf(0) }
    var previewCard1 by remember { mutableStateOf<Card?>(null) }
    var previewCard2 by remember { mutableStateOf<Card?>(null) }

    val updatePreviews = {
        val previewCards = getPreviewCards()
        previewCard1 = previewCards.getOrNull(0)
        previewCard2 = previewCards.getOrNull(1)
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = smallPadding)
            .verticalScroll(rememberScrollState())

    ) {
        CustomTextField(
            text = "Paste text:",
            value = inputText ?: "",
            onValueChange = { setInputText(it); updatePreviews() },
            label = "Copy and paste text directly from a table, list, etc.",
            isError = isError != 0,
            errorMessage = when (isError) {
                1 -> "This field is required."
                2 -> "This text is incomplete."
                3 -> "This text is too long"
                else -> "An unknown error occurred."
            },
            minLines = inputTextUiNumLines,
            maxLines = inputTextUiNumLines,
            focusManager = focusManager,
            modifier = Modifier
                .padding(vertical = smallPadding, horizontal = mediumPadding)
        )
        CustomTextField(
            text = "Question text lines:",
            value = questionLines ?: "",
            onValueChange = { setQuestionLines(it); updatePreviews() },
            label = "Split with \',\' and define ranges with \'-\'",
            isError = isError == 10,
            errorMessage = "This field is required.",
            maxLines = 1,
            focusManager = focusManager,
            modifier = Modifier
                .padding(vertical = smallPadding, horizontal = mediumPadding)
        )
        CustomTextField(
            text = "Answer text lines:",
            value = answerLines ?: "",
            onValueChange = { setAnswerLines(it); updatePreviews() },
            label = "Split with \',\' and define ranges with \'-\'",
            isError = isError == 20,
            errorMessage = "This field is required.",
            maxLines = 1,
            focusManager = focusManager,
            modifier = Modifier
                .padding(vertical = smallPadding, horizontal = mediumPadding)
        )
        CustomTextField(
            text = "Hint text lines (optional):",
            value = hintLines ?: "",
            onValueChange = { setHintLines(it); updatePreviews() },
            label = "Split with \',\' and define ranges with \'-\'",
            maxLines = 1,
            focusManager = focusManager,
            modifier = Modifier
                .padding(vertical = smallPadding, horizontal = mediumPadding)
        )
        CustomTextField(
            text = "Example text lines (optional):",
            value = exampleLines ?: "",
            onValueChange = { setExampleLines(it); updatePreviews() },
            label = "Split with \',\' and define ranges with \'-\'",
            maxLines = 1,
            focusManager = focusManager,
            modifier = Modifier
                .padding(vertical = smallPadding, horizontal = mediumPadding)
        )
        CustomTextField(
            text = "Lines to ignore (optional):",
            value = ignoredLines ?: "",
            onValueChange = { setIgnoredLines(it); updatePreviews() },
            label = "Split with \',\' and define ranges with \'-\'",
            maxLines = 1,
            focusManager = focusManager,
            isLast = true,
            modifier = Modifier
                .padding(vertical = smallPadding, horizontal = mediumPadding)
        )

        Spacer(modifier = Modifier.height(smallPadding))

        Column {
            Text(
                text = "Preview (first 2 cards):",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = mediumPadding)
            )

            Row {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = mediumPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(mediumPadding)
                    ) {

                        Text(text = "Question:", fontSize = 14.sp,)
                        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard1?.questionText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                        Spacer(modifier = Modifier.height(smallPadding))
                        Text(text = "Answer:", fontSize = 14.sp,)
                        Card(
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard1?.answerText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                        Spacer(modifier = Modifier.height(smallPadding))
                        Text(text = "Hint:", fontSize = 14.sp,)
                        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard1?.hintText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                        Spacer(modifier = Modifier.height(smallPadding))
                        Text(text = "Example:", fontSize = 14.sp,)
                        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard1?.exampleText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(smallPadding))

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = mediumPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(mediumPadding)
                    ) {

                        Text(text = "Question:", fontSize = 14.sp,)
                        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard2?.questionText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                        Spacer(modifier = Modifier.height(smallPadding))
                        Text(text = "Answer:", fontSize = 14.sp,)
                        Card(
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard2?.answerText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                        Spacer(modifier = Modifier.height(smallPadding))
                        Text(text = "Hint:", fontSize = 14.sp,)
                        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard2?.hintText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                        Spacer(modifier = Modifier.height(smallPadding))
                        Text(text = "Example:", fontSize = 14.sp,)
                        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = previewCard2?.exampleText ?: "",
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = smallPadding)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(mediumPadding))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
        ) {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier.size(160.dp, 40.dp)
            ) { Text("Cancel") }
            Button(
                onClick = {
                    if (inputText.isNullOrBlank()) {
                        isError = 1
                    } else {
                        onCreateClicked()
                    }
                },
                modifier = Modifier.size(160.dp, 40.dp)
            ) { Text("Create") }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BringFromDecksScreen(
    onDismissRequest: () -> Unit,
    bundles: List<BundleWithDecks>,
    decks: List<Deck>,
    excludeMastered: Boolean,
    resetHistory: Boolean,
    onSelectClicked: (Deck) -> Unit,
    setExcludeMastered: (Boolean) -> Unit,
    setResetHistory: (Boolean) -> Unit,
    ) {

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    var dropdownMenuWidth by remember { mutableIntStateOf(0) }

    var dropdownMenuState by remember { mutableStateOf(0) }
    var selectedBundle by remember { mutableStateOf<BundleWithDecks?>(null) }
    var selectedDeck by remember { mutableStateOf<Deck?>(null) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    ) {
        Text(
            text = "Choose deck:",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(mediumPadding))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
        ) {
            Text(
                text = "Bundle (optional)",
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(smallPadding))
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        dropdownMenuWidth = coordinates.size.width
                    }
                    .combinedClickable(
                        onClick = { dropdownMenuState = 1 },
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = mediumPadding)
                ) {
                    Text(
                        text = selectedBundle?.bundle?.name ?: "None"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = if (dropdownMenuState == 1) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
            }
            DropdownMenu(
                expanded = dropdownMenuState == 1,
                onDismissRequest = { dropdownMenuState = 0 },
                modifier = Modifier
                    .width(with(LocalDensity.current) { dropdownMenuWidth.toDp() })
                    .heightIn(0.dp, 240.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(text = "None") },
                    onClick = {
                        if (selectedBundle != null) selectedDeck = null
                        selectedBundle = null
                        dropdownMenuState = 0
                    },
                )
                for (bundle in bundles) {
                    DropdownMenuItem(
                        text = { Text(text = bundle.bundle.name) },
                        onClick = { selectedBundle = bundle; dropdownMenuState = 0 },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(mediumPadding))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
        ) {
            Text(
                text = "Deck",
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(smallPadding))
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        onClick = { dropdownMenuState = 2 },
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = mediumPadding)
                ) {
                    Text(
                        text = selectedDeck?.name ?: "None"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = if (dropdownMenuState == 2) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
            }
            DropdownMenu(
                expanded = dropdownMenuState == 2,
                onDismissRequest = { dropdownMenuState = 0 },
                modifier = Modifier
                    .width(with(LocalDensity.current) { dropdownMenuWidth.toDp() })
                    .heightIn(0.dp, 240.dp)
            ) {
                var n = 0
                for (deck in decks) {
                    if (deck.bundleId == (selectedBundle?.bundle?.id ?: -1)) {
                        DropdownMenuItem(
                            text = { Text(text = deck.name) },
                            onClick = { selectedDeck = deck; dropdownMenuState = 0 },
                        )
                        n++
                    }
                }
                if (n == 0) {
                    DropdownMenuItem(
                        text = { Text(text = "None") },
                        onClick = {
                            selectedDeck = null
                            dropdownMenuState = 0
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Switch(
                checked = excludeMastered,
                onCheckedChange = { setExcludeMastered(it) },
            )
            Spacer(modifier = Modifier.width(mediumPadding))
            Text(
                text = "Exclude mastered cards",
                fontSize = 16.sp,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Switch(
                checked = resetHistory,
                onCheckedChange = { setResetHistory(it) },
            )
            Spacer(modifier = Modifier.width(mediumPadding))
            Text(
                text = "Clear history",
                fontSize = 16.sp,
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = onDismissRequest
            ) { Text("Cancel") }
            Button(
                enabled = selectedDeck != null,
                onClick = {
                    onSelectClicked(selectedDeck!!)
                }
            ) { Text("Select") }
        }
    }
}

@Composable
fun TipDialog(
    onDismissRequest: () -> Unit,
    tip: String,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()
            ) {
                Text(text = tip, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(mediumPadding*2))
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.size(120.dp, 40.dp)
                ) {
                    Text(text = "Close")
                }
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
fun ImportCardsScreen() {
    FlashcardsTheme() {
        ImportCardsScreen(viewModel(), {})
    }
}