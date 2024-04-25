package com.example.flashcards.ui.importCards

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.FlashcardApplication
import com.example.flashcards.R
import com.example.flashcards.data.Constants
import com.example.flashcards.data.StringLength
import com.example.flashcards.data.entities.Card
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.ui.AppViewModelProvider
import com.example.flashcards.ui.deck.ITT_ErrorState
import com.example.flashcards.ui.deck.ImportCardsViewModel
import com.example.flashcards.ui.deck.SubDeck
import com.example.flashcards.ui.deck.SubDeckType
import com.example.flashcards.ui.deck.UCF_ErrorState
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImportCardsScreen (
    viewModel: ImportCardsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackButtonClicked: () -> Unit,
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

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
                        else if (uiState.isUploadCsvFileScreenOpen) {
                            viewModel.toggleUploadCsvFileScreen()
                        }
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
            Column {
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

                            if (uiState.isBringFromDecksScreenOpen) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    TextButton(
                                        onClick = { viewModel.toggleBringFromDecksScreen() },
                                        modifier = Modifier.size(160.dp, 40.dp)
                                    ) { Text("Cancel") }
                                    Button(
                                        enabled = uiState.bFD_selectedDeck != null,
                                        onClick = {
                                            if (uiState.bFD_selectedDeck != null) {
                                                coroutineScope.launch {
                                                    val cards =
                                                        viewModel.getAllCardsFromDeck(uiState.bFD_selectedDeck!!)
                                                    viewModel.toggleBringFromDecksScreen()
                                                    viewModel.addSubDeck(
                                                        SubDeck(
                                                            name = ("${cards.size} " + if (cards.size == 1) "card" else "cards") + " - import from \"${uiState.bFD_selectedDeck!!.name}\"",
                                                            type = SubDeckType.DEFAULT,
                                                            cards = cards,
                                                        )
                                                    )
                                                }
                                            }
                                        },
                                        modifier = Modifier.size(160.dp, 40.dp)
                                    ) { Text("Import") }
                                }

                            } else if (uiState.isImportThroughTextScreenOpen) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    TextButton(
                                        onClick = { viewModel.toggleImportThroughTextScreen() },
                                        modifier = Modifier.size(160.dp, 40.dp)
                                    ) { Text("Cancel") }
                                    Button(
                                        enabled = uiState.iTT_inputText.isNotBlank() && uiState.iTT_questionLines.isNotBlank() && uiState.iTT_answerLines.isNotBlank(),
                                        onClick = {
                                            viewModel.setImportThroughScreenFocusRequest(false)
                                            val cards = viewModel.textToCards(checkForErrors = true)
                                            if (cards != null) {
                                                viewModel.toggleImportThroughTextScreen()
                                                viewModel.addSubDeck(
                                                    SubDeck(
                                                        name = ("${cards.size} " + if (cards.size == 1) "card" else "cards") + " - imported from text",
                                                        type = SubDeckType.TEXT,
                                                        cards = cards,
                                                    )
                                                )
                                            }
                                        },
                                        modifier = Modifier.size(160.dp, 40.dp)
                                    ) { Text("Import") }
                                }
                            } else if (uiState.isUploadCsvFileScreenOpen) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    TextButton(
                                        onClick = { viewModel.toggleUploadCsvFileScreen() },
                                        modifier = Modifier.size(160.dp, 40.dp)
                                    ) { Text("Cancel") }
                                    Button(
                                        enabled = uiState.uCF_csvFileData.isNotEmpty() && uiState.uCF_questionIndex != null && uiState.uCF_answerIndex != null,
                                        onClick = {
                                            viewModel.setUploadCsvFileScreenFocusRequest(false)
                                            val cards = viewModel.csvDataToCards(checkForErrors = true)
                                            if (cards != null) {
                                                viewModel.toggleUploadCsvFileScreen()
                                                viewModel.addSubDeck(
                                                    SubDeck(
                                                        name = ("${cards.size} " + if (cards.size == 1) "card" else "cards") + " - uploaded (${uiState.uCF_csvFileName})",
                                                        type = SubDeckType.CSV,
                                                        cards = cards,
                                                    )
                                                )
                                            }
                                        },
                                        modifier = Modifier.size(160.dp, 40.dp)
                                    ) { Text("Import") }
                                }
                            } else {
                                Button(
                                    onClick = {
                                        if (viewModel.getTotalNumCards() <= Constants.MAX_CARDS) {
                                            coroutineScope.launch {
                                                viewModel.importAll()
                                                viewModel.reset()
                                                onBackButtonClicked()
                                            }
                                        } else {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Card limit reached (maximum ${Constants.MAX_CARDS} cards).",
                                                    withDismissAction = true,
                                                )
                                            }
                                        }
                                    },
                                    enabled = uiState.subDecks.isNotEmpty(),
                                    modifier = Modifier
                                        .width(160.dp),
                                ) {
                                    Text(
                                        text = "Create cards",
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp,
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {

            if (uiState.isBringFromDecksScreenOpen) {
                BackHandler { viewModel.toggleBringFromDecksScreen() }

                BringFromDecksScreen(
                    bundles = uiState.bundles,
                    decks = uiState.decks,
                    excludeMastered = uiState.bFD_excludeMastered,
                    resetHistory = uiState.bFD_resetHistory,
                    setExcludeMastered = { viewModel.setExcludeMastered(it) },
                    setResetHistory = { viewModel.setResetHistory(it) },
                    selectedBundle = uiState.bFD_selectedBundle,
                    selectedDeck = uiState.bFD_selectedDeck,
                    selectBundle = { viewModel.selectBundle(it) },
                    selectDeck = { viewModel.selectDeck(it) },
                )

            } else if (uiState.isImportThroughTextScreenOpen) {
                BackHandler { viewModel.toggleImportThroughTextScreen() }

                ImportThroughTextScreen(
                    setInputText = { viewModel.setInputText(it) },
                    setQuestionLines = { viewModel.setQuestionLines(it) },
                    setAnswerLines = { viewModel.setAnswerLines(it) },
                    setHintLines = { viewModel.setHintLines(it) },
                    setExampleLines = { viewModel.setExampleLines(it) },
                    setIgnoredLines = { viewModel.setIgnoredLines(it) },
                    inputText = uiState.iTT_inputText,
                    questionLines = uiState.iTT_questionLines,
                    answerLines = uiState.iTT_answerLines,
                    hintLines = uiState.iTT_hintLines,
                    exampleLines = uiState.iTT_exampleLines,
                    ignoredLines = uiState.iTT_ignoredLines,
                    focusManager = focusManager,
                    isFocusRequested = uiState.iTT_focusRequested,
                    setFocusRequest = { viewModel.setImportThroughScreenFocusRequest(it) },
                    focusRequesterT = uiState.iTT_focusRequesterT,
                    focusRequesterQ = uiState.iTT_focusRequesterQ,
                    focusRequesterA = uiState.iTT_focusRequesterA,
                    focusRequesterH = uiState.iTT_focusRequesterH,
                    focusRequesterE = uiState.iTT_focusRequesterE,
                    focusRequesterI = uiState.iTT_focusRequesterI,
                    errorState = uiState.iTT_errorState,
                    errorState2 = uiState.iTT_errorState2,
                    previewCard1 = uiState.iTT_previewCard1,
                    previewCard2 = uiState.iTT_previewCard2,
                    updatePreviews = { viewModel.updateImportThroughTextScreenPreviewCards() },
                )

            } else if (uiState.isUploadCsvFileScreenOpen) {
                BackHandler { viewModel.toggleImportThroughTextScreen() }

                UploadCsvFileScreen(
                    parseCsvFile = { viewModel.csvToStrList(context, it) },
                    csvFileData = uiState.uCF_csvFileData,
                    csvFileName = uiState.uCF_csvFileName,
                    csvFileSize = uiState.uCF_csvFileSize,
                    setQuestionIndex = { viewModel.setQuestionIndex(it) },
                    setAnswerIndex = { viewModel.setAnswerIndex(it) },
                    setHintIndex = { viewModel.setHintIndex(it) },
                    setExampleIndex = { viewModel.setExampleIndex(it) },
                    questionIndex = uiState.uCF_questionIndex,
                    answerIndex = uiState.uCF_answerIndex,
                    hintIndex = uiState.uCF_hintIndex,
                    exampleIndex = uiState.uCF_exampleIndex,
                    focusManager = focusManager,
                    isFocusRequested = uiState.uCF_focusRequested,
                    setFocusRequest = { viewModel.setUploadCsvFileScreenFocusRequest(it) },
                    focusRequesterF = uiState.uCF_focusRequesterF,
                    errorState = uiState.uCF_errorState,
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
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = mediumPadding)
                                    )
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
fun UploadCsvFileScreen(
    parseCsvFile: (Uri) -> Unit,
    csvFileData: List<List<String>>,
    csvFileName: String,
    csvFileSize: Long,
    questionIndex: Int?,
    answerIndex: Int?,
    hintIndex: Int?,
    exampleIndex: Int?,
    setQuestionIndex: (String) -> Unit,
    setAnswerIndex: (String) -> Unit,
    setHintIndex: (String) -> Unit,
    setExampleIndex: (String) -> Unit,
    focusManager: FocusManager,
    isFocusRequested: Boolean,
    focusRequesterF: FocusRequester,
    setFocusRequest: (Boolean) -> Unit,
    errorState: UCF_ErrorState,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val mediumLargePadding = dimensionResource(R.dimen.padding_medium_large)

    val onSelectCsvClicked = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
    ) { fileUri ->
        if (fileUri != null) {
            parseCsvFile(fileUri)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(smallPadding)

        ) {
            Text(
                text = "Select .CSV file" + (
                    when (errorState) {
                        UCF_ErrorState.FILE_EMPTY -> " - file is empty."
                        UCF_ErrorState.FILE_INCOMPLETE -> " - file contains inconsistent data."
                        UCF_ErrorState.FILE_TOO_LONG -> " - too many cards (will only import the first ${Constants.MAX_CARDS} cards).."
                        UCF_ErrorState.FILE_TOO_LARGE -> " - file is too large (maximum: ${Constants.MAX_FILE_SIZE/1000000} MB)."
                        else -> ""
                    }
                ),
                color = if (errorState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Card(
                    shape = RoundedCornerShape(5),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = smallPadding)
                        .wrapContentHeight(align = Alignment.CenterVertically)

                ) {
                    Text(
                        text = csvFileName.ifBlank { "File not selected" },
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(smallPadding)
                    )
                }

                Button(
                    onClick = { onSelectCsvClicked.launch("*/*") },
                    modifier = Modifier
                        .size(120.dp, 40.dp)
                        .focusRequester(focusRequesterF)
                ) {
                    Text("Browse file")
                }
            }
            if (csvFileName.isNotEmpty()) {
                Text(
                    text =
                        "${"%,d".format(csvFileSize)} bytes, "
                        + (if (errorState == UCF_ErrorState.FILE_TOO_LARGE) "? " else "${csvFileData.size} ")
                        + (if (csvFileData.size == 1) "card" else "cards"),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (errorState == UCF_ErrorState.FILE_TOO_LARGE) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            CustomTextField(
                text = "Question text column",
                value = "${questionIndex ?: ""}",
                onValueChange = { setQuestionIndex(it) },
                label = "Column number",
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(top = mediumLargePadding, bottom = smallPadding)
            )
            CustomTextField(
                text = "Answer text column",
                value = "${answerIndex ?: ""}",
                onValueChange = { setAnswerIndex(it) },
                label = "Column number",
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(vertical = smallPadding)
            )
            CustomTextField(
                text = "Hint text column (optional)",
                value = "${hintIndex ?: ""}",
                onValueChange = { setHintIndex(it) },
                label = "Column number",
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(vertical = smallPadding)
            )
            CustomTextField(
                text = "Example text column (optional)",
                value = "${exampleIndex ?: ""}",
                onValueChange = { setExampleIndex(it) },
                label = "Column number",
                maxLines = 1,
                focusManager = focusManager,
                isLast = true,
                modifier = Modifier
                    .padding(vertical = smallPadding)
            )

            Column {
                Text(
                    text = "Preview (first 2 cards)",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )

                Row {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mediumPadding)
                        ) {

                            Text(text = "Question", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(0)?.getOrNull(questionIndex?.dec() ?: -1) ?: "",
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = smallPadding)
                                )
                            }
                            Spacer(modifier = Modifier.height(smallPadding))
                            Text(text = "Answer", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(0)?.getOrNull(answerIndex?.dec() ?: -1) ?: "",
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = smallPadding)
                                )
                            }
                            Spacer(modifier = Modifier.height(smallPadding))
                            Text(text = "Hint", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(0)?.getOrNull(hintIndex?.dec() ?: -1) ?: "",
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = smallPadding)
                                )
                            }
                            Spacer(modifier = Modifier.height(smallPadding))
                            Text(text = "Example", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(0)?.getOrNull(exampleIndex?.dec() ?: -1) ?: "",
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
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mediumPadding)
                        ) {

                            Text(text = "Question", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(1)?.getOrNull(questionIndex?.dec() ?: -1) ?: "",
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = smallPadding)
                                )
                            }
                            Spacer(modifier = Modifier.height(smallPadding))
                            Text(text = "Answer", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(1)?.getOrNull(answerIndex?.dec() ?: -1) ?: "",
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = smallPadding)
                                )
                            }
                            Spacer(modifier = Modifier.height(smallPadding))
                            Text(text = "Hint", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(1)?.getOrNull(hintIndex?.dec() ?: -1) ?: "",
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = smallPadding)
                                )
                            }
                            Spacer(modifier = Modifier.height(smallPadding))
                            Text(text = "Example", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = csvFileData.getOrNull(1)?.getOrNull(exampleIndex?.dec() ?: -1) ?: "",
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
        }

        if (!isFocusRequested && errorState.isError) {
            focusRequesterF.requestFocus()
            setFocusRequest(true)
        }
    }
}

@Composable
fun ImportThroughTextScreen(
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
    isFocusRequested: Boolean,
    focusRequesterT: FocusRequester,
    focusRequesterQ: FocusRequester,
    focusRequesterA: FocusRequester,
    focusRequesterH: FocusRequester,
    focusRequesterE: FocusRequester,
    focusRequesterI: FocusRequester,
    setFocusRequest: (Boolean) -> Unit,
    errorState: ITT_ErrorState,
    errorState2: ITT_ErrorState,
    previewCard1: Card?,
    previewCard2: Card?,
    updatePreviews: () -> Unit,
) {

    val inputTextUiNumLines = 8

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(smallPadding)

        ) {
            CustomTextField(
                text = "Copied text",
                value = inputText ?: "",
                onValueChange = { setInputText(it); updatePreviews() },
                label = "Copy and paste text directly from a table, list, etc.",
                isError = errorState.isTextError,
                errorMessage = when (errorState) {
                    ITT_ErrorState.TEXT_INCOMPLETE -> "number of questions and answers do not match."
                    ITT_ErrorState.TEXT_TOO_LONG -> "this text is too long."
                    else -> "what have you done??"
                },
                minLines = inputTextUiNumLines,
                maxLines = inputTextUiNumLines,
                focusManager = focusManager,
                stringLength = StringLength.VLONG,
                modifier = Modifier
                    .padding(vertical = smallPadding)
                    .focusRequester(focusRequesterT)
            )
            CustomTextField(
                text = "Question text lines",
                value = questionLines ?: "",
                onValueChange = { setQuestionLines(it); updatePreviews() },
                label = "Split with \',\' and define ranges with \'-\'",
                isError = errorState.isQuestionLineError || errorState2.isQuestionLineError,
                errorMessage =
                if (errorState == ITT_ErrorState.QUESTION_LINES_DUPLICATE || errorState2 == ITT_ErrorState.QUESTION_LINES_DUPLICATE)
                    "lines cannot overlap."
                else "what have you done??",
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(vertical = smallPadding)
                    .focusRequester(focusRequesterQ)
            )
            CustomTextField(
                text = "Answer text lines",
                value = answerLines ?: "",
                onValueChange = { setAnswerLines(it); updatePreviews() },
                label = "Split with \',\' and define ranges with \'-\'",
                isError = errorState.isAnswerLineError || errorState2.isAnswerLineError,
                errorMessage =
                if (errorState == ITT_ErrorState.ANSWER_LINES_DUPLICATE || errorState2 == ITT_ErrorState.ANSWER_LINES_DUPLICATE)
                    "lines cannot overlap."
                else "what have you done??",
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(vertical = smallPadding)
                    .focusRequester(focusRequesterA)
            )
            CustomTextField(
                text = "Hint text lines (optional)",
                value = hintLines ?: "",
                onValueChange = { setHintLines(it); updatePreviews() },
                label = "Split with \',\' and define ranges with \'-\'",
                isError = errorState.isHintLineError || errorState2.isHintLineError,
                errorMessage =
                if (errorState == ITT_ErrorState.HINT_LINES_DUPLICATE || errorState2 == ITT_ErrorState.HINT_LINES_DUPLICATE)
                    "lines cannot overlap."
                else "what have you done??",
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(vertical = smallPadding)
                    .focusRequester(focusRequesterH)
            )
            CustomTextField(
                text = "Example text lines (optional)",
                value = exampleLines ?: "",
                onValueChange = { setExampleLines(it); updatePreviews() },
                label = "Split with \',\' and define ranges with \'-\'",
                isError = errorState.isExampleLineError || errorState2.isExampleLineError,
                errorMessage =
                if (errorState == ITT_ErrorState.EXAMPLE_LINES_DUPLICATE || errorState2 == ITT_ErrorState.EXAMPLE_LINES_DUPLICATE)
                    "lines cannot overlap."
                else "what have you done??",
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(vertical = smallPadding)
                    .focusRequester(focusRequesterE)
            )
            CustomTextField(
                text = "Lines to ignore (optional)",
                value = ignoredLines ?: "",
                onValueChange = { setIgnoredLines(it); updatePreviews() },
                label = "Split with \',\' and define ranges with \'-\'",
                isError = errorState.isIgnoredLineError || errorState2.isIgnoredLineError,
                errorMessage =
                if (errorState == ITT_ErrorState.IGNORED_LINES_DUPLICATE || errorState2 == ITT_ErrorState.IGNORED_LINES_DUPLICATE)
                    "lines cannot overlap."
                else "what have you done??",
                maxLines = 1,
                focusManager = focusManager,
                isLast = true,
                modifier = Modifier
                    .padding(vertical = smallPadding)
                    .focusRequester(focusRequesterI)
            )

            Spacer(modifier = Modifier.height(smallPadding))

            Column {
                Text(
                    text = "Preview (first 2 cards)",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )

                Row {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mediumPadding)
                        ) {

                            Text(text = "Question", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
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
                            Text(text = "Answer", fontSize = 14.sp,)
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
                            Text(text = "Hint", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
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
                            Text(text = "Example", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
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
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mediumPadding)
                        ) {

                            Text(text = "Question", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
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
                            Text(text = "Answer", fontSize = 14.sp,)
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
                            Text(text = "Hint", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
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
                            Text(text = "Example", fontSize = 14.sp,)
                            Card(
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
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
        }

        if (!isFocusRequested) {
            if (errorState.isTextError) focusRequesterT.requestFocus()
            else if (errorState.isQuestionLineError) focusRequesterQ.requestFocus()
            else if (errorState.isAnswerLineError) focusRequesterA.requestFocus()
            else if (errorState.isHintLineError) focusRequesterH.requestFocus()
            else if (errorState.isExampleLineError) focusRequesterE.requestFocus()
            else if (errorState.isIgnoredLineError) focusRequesterI.requestFocus()
            setFocusRequest(true)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BringFromDecksScreen(
    bundles: List<BundleWithDecks>,
    decks: List<Deck>,
    excludeMastered: Boolean,
    resetHistory: Boolean,
    setExcludeMastered: (Boolean) -> Unit,
    setResetHistory: (Boolean) -> Unit,
    selectedBundle: BundleWithDecks?,
    selectedDeck: Deck?,
    selectBundle: (BundleWithDecks?) -> Unit,
    selectDeck: (Deck?) -> Unit,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    var dropdownMenuWidth by remember { mutableIntStateOf(0) }
    var dropdownMenuState by remember { mutableStateOf(0) }


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(smallPadding)

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
            ) {
                Text(
                    text = "Bundle (optional)",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = smallPadding)
                )
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
                Spacer(modifier = Modifier.height(smallPadding))
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
                            if (selectedBundle != null) selectDeck(null)
                            selectBundle(null)
                            dropdownMenuState = 0
                        },
                    )
                    for (bundle in bundles) {
                        DropdownMenuItem(
                            text = { Text(text = bundle.bundle.name) },
                            onClick = { selectBundle(bundle); dropdownMenuState = 0 },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(smallPadding))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
            ) {
                Text(
                    text = "Deck",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = smallPadding)
                )
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
                                onClick = { selectDeck(deck); dropdownMenuState = 0 },
                            )
                            n++
                        }
                    }
                    if (n == 0) {
                        DropdownMenuItem(
                            text = { Text(text = "None") },
                            onClick = {
                                selectDeck(null)
                                dropdownMenuState = 0
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(mediumPadding))
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
        }
    }
}

@Composable
fun TipDialog(
    onDismissRequest: () -> Unit,
    tip: String,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)

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
                Spacer(modifier = Modifier.height(largePadding))
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
    stringLength: StringLength = StringLength.SHORT,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text + if (isError) " - $errorMessage" else "",
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(if (it.length <= stringLength.maxLength) it else it.substring(0..stringLength.maxLength)) },
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