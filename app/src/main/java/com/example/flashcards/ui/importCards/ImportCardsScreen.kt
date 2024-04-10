package com.example.flashcards.ui.importCards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.data.relations.BundleWithDecks
import com.example.flashcards.ui.AppViewModelProvider
import com.example.flashcards.ui.deck.ImportCardsViewModel
import com.example.flashcards.ui.deck.SubDeck
import com.example.flashcards.ui.deck.SubDeckType
import com.example.flashcards.ui.editCard.EditCardViewModel
import com.example.flashcards.ui.menu.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
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
                    Text(
                        text = "Import Cards",
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
                actions = {},
            )
        },
        bottomBar = {
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
                                    coroutineScope.launch{
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
        },
    ) { innerPadding ->

        val smallPadding = dimensionResource(R.dimen.padding_small)
        val mediumPadding = dimensionResource(R.dimen.padding_medium)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Card(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(top = smallPadding, start = smallPadding, end = smallPadding)
                    .combinedClickable(
                        onClick = { viewModel.toggleBringFromDecksDialog() },
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
                        onClick = { viewModel.toggleImportFromQuizletDialog() },
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
                        contentDescription = "Import from quizlet",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(mediumPadding))
                    Text(
                        text = "Import from Quizlet",
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
                        onClick = { viewModel.toggleUploadCsvFileDialog() },
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

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                for (subDeck in uiState.subDecks) {
                    Card(
                        modifier = Modifier
                            .height(64.dp)
                            .fillMaxWidth()
                            .padding(top = smallPadding, start = smallPadding, end = smallPadding)
                            .combinedClickable(
                                onClick = { viewModel.toggleUploadCsvFileDialog() },
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
                                text = subDeck.type.name.substring(0..0),
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.width(mediumPadding))
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
                Spacer(modifier = Modifier.height(smallPadding))
            }
        }
    }

    if (uiState.isBringFromDecksDialogOpen) {
        BringFromDecksDialog(
            onDismissRequest = { viewModel.toggleBringFromDecksDialog() },
            bundles = uiState.bundles,
            decks = uiState.decks,
            excludeMastered = uiState.excludeMastered,
            resetHistory = uiState.resetHistory,
            onSelectClicked = {
                coroutineScope.launch {
                    viewModel.toggleBringFromDecksDialog()
                    viewModel.addSubDeck(
                        SubDeck(
                            name = it.name,
                            type = SubDeckType.DEFAULT,
                            cards = viewModel.getAllCardsFromDeck(it),
                        )
                    )
                }
            },
            setExcludeMastered = { viewModel.setExcludeMastered(it) },
            setResetHistory = { viewModel.setResetHistory(it) },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BringFromDecksDialog(
    onDismissRequest: () -> Unit,
    bundles: List<BundleWithDecks>,
    decks: List<Deck>,
    excludeMastered: Boolean,
    resetHistory: Boolean,
    onSelectClicked: (Deck) -> Unit,
    setExcludeMastered: (Boolean) -> Unit,
    setResetHistory: (Boolean) -> Unit,
    ) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {

        val smallPadding = dimensionResource(R.dimen.padding_small)
        val mediumPadding = dimensionResource(R.dimen.padding_medium)

        var dropdownMenuWidth by remember { mutableIntStateOf(0) }

        var dropdownMenuState by remember { mutableStateOf(0) }
        var selectedBundle by remember { mutableStateOf<BundleWithDecks?>(null) }
        var selectedDeck by remember { mutableStateOf<Deck?>(null) }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .padding(mediumPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxSize()


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