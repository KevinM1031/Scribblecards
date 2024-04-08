package com.example.flashcards.ui.menu

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.data.entities.Bundle
import com.example.flashcards.data.entities.Deck
import com.example.flashcards.ui.AppViewModelProvider
import com.example.flashcards.ui.theme.FlashcardsTheme
import kotlinx.coroutines.launch

private const val BOX_SIZE_DP = 110
private const val BOX_SIZE_IN_BUNDLE_DP = 100

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onDeckButtonClicked: (Long) -> Unit,
    onBackButtonClicked: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val isBundleOpen = viewModel.isBundleOpen()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            val currentBundleIndex = uiState.currentBundleIndex

            if (uiState.isBundleCreatorOpen) {
                BundleCreatorTopAppBar(
                    numSelected = uiState.numSelectedDecks + uiState.numSelectedBundles,
                    closeBundleCreator = { viewModel.closeBundleCreator() },
                    onCreateClicked = { viewModel.openBundleCreatorDialog() },
                )

            } else if (isBundleOpen) {
                BundleTopAppBar(
                    onBackButtonClicked = { viewModel.closeBundle() },
                    onEditBundleNameButtonClicked = { viewModel.openEditBundleNameDialog() },
                    title = viewModel.getBundle(currentBundleIndex!!).name,
                )

            } else {
                DashboardTopAppBar(
                    onBackButtonClicked = onBackButtonClicked,
                )
            }
        },
        floatingActionButton = {
            if (!uiState.isBundleCreatorOpen) {
                CreateOptionButton(
                    isCreateOptionsOpen = uiState.isCreateOptionsOpen,
                    openBundleCreator = { viewModel.openBundleCreator() },
                    openDeckCreator = { viewModel.openDeckCreatorDialog() },
                    toggleCreateOptions = { viewModel.toggleCreateOptions() },
                )
            }
        },
    ) { innerPadding ->

        var containerSize by remember { mutableStateOf(IntSize.Zero) }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size
                }
        ) {
            CardsList(
                onDeckOpened = { onDeckButtonClicked(it) },
                onDeckSelected = { viewModel.toggleDeckSelection(it) },
                getDeck = { viewModel.getDeck(it) },
                numDecks = viewModel.getNumDecks(),

                onBundleOpened = { viewModel.openBundle(it) },
                onBundleSelected = { viewModel.toggleBundleSelection(it) },
                getBundle = { viewModel.getBundle(it) },
                numBundles = viewModel.getNumBundles(),

                isBundleCreatorOpen = uiState.isBundleCreatorOpen,
                containerSize = containerSize,
                cardIconSize = BOX_SIZE_DP,
                padding = dimensionResource(R.dimen.padding_medium),
                blur = isBundleOpen,
            )

            if (isBundleOpen) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) { detectTapGestures { viewModel.closeBundle() } }
                )

                OpenBundle(
                    configuration = LocalConfiguration.current,
                    onDeckOpened = { onDeckButtonClicked(it) },
                    onDeckSelected = { viewModel.toggleDeckSelection(it) } ,
                    getDeck = { viewModel.getDeckFromCurrentBundle(it) },
                    numDecks = viewModel.getNumDecksInCurrentBundle(),
                    getBundle = { viewModel.getBundle(it) },
                    isBundleCreatorOpen = uiState.isBundleCreatorOpen,
                    cardIconSize = BOX_SIZE_IN_BUNDLE_DP,
                )
            }
        }
    }

    if (uiState.isBundleCreatorDialogOpen) {
        CreateBundleDialog(
            onDismissRequest = { viewModel.closeBundleCreatorDialog() },
            onCreateClicked = {
                coroutineScope.launch {
                    viewModel.createBundle(it)
                }
                viewModel.closeBundleCreator()
                viewModel.closeBundle()
                viewModel.closeBundleCreatorDialog()
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isDeckCreatorDialogOpen) {
        CreateDeckDialog(
            onDismissRequest = { viewModel.closeDeckCreatorDialog() },
            onCreateClicked = {
                coroutineScope.launch {
                    viewModel.createDeck(it)
                }
                viewModel.closeDeckCreatorDialog()
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isEditBundleNameDialogOpen) {
        CreateBundleDialog(
            editMode = true,
            onDismissRequest = { viewModel.closeEditBundleNameDialog() },
            onCreateClicked = {
                coroutineScope.launch {
                    viewModel.updateCurrentBundleName(it)
                }
                viewModel.closeEditBundleNameDialog()
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardsList(
    onDeckOpened: (Long) -> Unit,
    onDeckSelected: (Int) -> Unit,
    getDeck: (Int) -> Deck,
    numDecks: Int,

    onBundleOpened: (Int) -> Unit,
    onBundleSelected: (Int) -> Unit,
    getBundle: (Int) -> Bundle,
    numBundles: Int,

    isBundleCreatorOpen: Boolean,
    containerSize: IntSize,
    cardIconSize: Int,
    padding: Dp = dimensionResource(R.dimen.padding_medium),
    blur: Boolean = false,
) {

    val bundlePositions = remember { mutableStateMapOf<Int, Offset>() }
    val deckPositions = remember { mutableStateMapOf<Int, Offset>() }
    var droppedBundle by remember { mutableStateOf<Int?>(null) }
    var droppedDeck by remember { mutableStateOf<Int?>(null) }
    val density = LocalDensity.current
    val sizePx = with(density) { BOX_SIZE_DP.dp.toPx()/2 }.toInt()

    if (droppedBundle != null) {

        droppedBundle = null
    } else if (droppedDeck != null) {
        bundlePositions.forEach {
            if (iconOverlaps(deckPositions[droppedDeck]!!, it.value, sizePx)) {
                Log.d("debug", "dropped on ${it.key}")
            }
        }
        droppedDeck = null
    }

    FlowRow(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .blur(if (blur) 12.dp else 0.dp)
            .verticalScroll(rememberScrollState())
            .wrapContentSize(Alignment.TopCenter)
    ) {

        for (i in 0..<numBundles) {
            DraggableComposable(
                index = i,
                onBundleOpened = onBundleOpened,
                onBundleSelected = onBundleSelected,
                getBundle = getBundle,
                isBundleCreatorOpen = isBundleCreatorOpen,
                isBundle = true,
                size = cardIconSize,
                setPosition = { bundlePositions[i] = it },
                onDrop = { droppedBundle = it },
            )
        }

        for (i in 0..<numDecks) {
            DraggableComposable(
                index = i,
                onDeckOpened = onDeckOpened,
                onDeckSelected = onDeckSelected,
                getDeck = getDeck,
                getBundle = getBundle,
                isBundleCreatorOpen = isBundleCreatorOpen,
                isBundle = false,
                size = cardIconSize,
                setPosition = { deckPositions[i] = it },
                onDrop = { droppedDeck = it },

                )
        }

        val width = with(LocalDensity.current) { containerSize.width.toDp() }
        val widthLeft = width - padding*2 - cardIconSize.dp*(numBundles+numDecks)

        for (i in 0..<((widthLeft/cardIconSize.dp).toInt())) {
            EmptyComponent(size = BOX_SIZE_DP)
        }
    }
}

fun iconOverlaps(p1: Offset, p2: Offset, size: Int): Boolean {
    return p1.x+size > p2.x && p1.x < p2.x+size && p1.y+size > p2.y && p1.y < p2.y+size
}

@Composable
fun DraggableComposable(
    index: Int,
    onDeckOpened: ((Long) -> Unit)? = null,
    onDeckSelected: ((Int) -> Unit)? = null,
    getDeck: ((Int) -> Deck)? = null,
    onBundleOpened: ((Int) -> Unit)? = null,
    onBundleSelected: ((Int) -> Unit)? = null,
    getBundle: ((Int) -> Bundle)? = null,
    isBundleCreatorOpen: Boolean,
    isBundle: Boolean,
    size: Int,
    setPosition: (Offset) -> Unit,
    onDrop: (Int?) -> Unit,
) {

    var isDragging by remember { mutableStateOf(false) }
    var xOff by remember { mutableFloatStateOf(0f) }
    var yOff by remember { mutableFloatStateOf(0f) }
    val d = LocalDensity.current

    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(dimensionResource(R.dimen.padding_small))
            .offset(x = (xOff / d.density).dp, y = (yOff / d.density).dp)
            .alpha(if (isDragging) 0.5f else 1f)
            .zIndex(if (isDragging) 1f else 0f)
            .onGloballyPositioned { coordinates ->
                setPosition(coordinates.positionInRoot())
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        isDragging = true
                        onDrop(null)
                    },
                    onDragEnd = {
                        isDragging = false
                        xOff = 0f
                        yOff = 0f
                        onDrop(index)
                    },
                    onDragCancel = {
                        isDragging = false
                        xOff = 0f
                        yOff = 0f
                        onDrop(null)
                    },
                ) { change, dragAmount ->
                    change.consume()
                    xOff += dragAmount.x
                    yOff += dragAmount.y
                }
            }
    ) {

        if (isBundle) {
            if (onBundleOpened != null && onBundleSelected != null && getBundle != null) {
                BundleComponent(
                    index = index,
                    onBundleOpened = onBundleOpened,
                    onBundleSelected = onBundleSelected,
                    getBundle = getBundle,
                )
            }
        } else {
            if (onDeckOpened != null && onDeckSelected != null && getDeck != null) {
                DeckComponent(
                    onDeckOpened = { onDeckOpened(it) },
                    onDeckSelected = { onDeckSelected(index) },
                    getDeck = { getDeck(index) },
                    isBundleCreatorOpen = isBundleCreatorOpen,
                )
            }
        }
    }
}

@Composable
fun BundleComponent(
    index: Int,
    onBundleOpened: (Int) -> Unit,
    onBundleSelected: (Int) -> Unit,
    getBundle: (Int) -> Bundle,
) {

    val bundle = getBundle(index)

    OutlinedButton(
        onClick = {
            onBundleOpened(index)
        },
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (bundle.isSelected) MaterialTheme.colorScheme.tertiary
            else MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = if (bundle.isSelected) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.tertiary,
        ),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = bundle.name,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun DeckComponent(
    onDeckOpened: (Long) -> Unit,
    onDeckSelected: () -> Unit,
    getDeck: () -> Deck,
    isBundleCreatorOpen: Boolean,
) {

    val deck = getDeck()

    OutlinedButton(
        onClick = {
            if (isBundleCreatorOpen) {
                onDeckSelected()
            } else {
                onDeckOpened(deck.id)
            }
        },
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (deck.isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.primary,
            contentColor = if (deck.isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.primaryContainer,
        ),
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text(
            text = deck.name,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OpenBundle(
    numDecks: Int,
    configuration: Configuration,
    cardIconSize: Int,
    onDeckOpened: ((Long) -> Unit)? = null,
    onDeckSelected: ((Int) -> Unit)? = null,
    getDeck: ((Int) -> Deck)? = null,
    getBundle: ((Int) -> Bundle)? = null,
    isBundleCreatorOpen: Boolean,
) {

    // overall constants
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val padding = mediumPadding

    val overlayWidth =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            (configuration.screenWidthDp - mediumPadding.value*2)*0.6f
        else
            configuration.screenWidthDp - mediumPadding.value*2
    val overlayHeight =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            configuration.screenHeightDp - mediumPadding.value*2
        else
            configuration.screenWidthDp - mediumPadding.value*2

    Card(
        modifier = Modifier
            .padding(mediumPadding)
            .size(overlayWidth.dp, overlayHeight.dp)

    ) {

        FlowRow(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(smallPadding)
                .wrapContentSize(Alignment.TopCenter)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size
                }
        ) {

            for (i in 0..<numDecks) {
                DraggableComposable(
                    index = i,
                    onDeckOpened = onDeckOpened,
                    onDeckSelected = onDeckSelected,
                    getDeck = getDeck,
                    getBundle = getBundle,
                    isBundleCreatorOpen = isBundleCreatorOpen,
                    isBundle = false,
                    size = cardIconSize,
                    setPosition = {},
                    onDrop = {},
                    )
            }
            val widthLeft = overlayWidth.dp - padding*2 - cardIconSize.dp*numDecks

            for (i in 0..<((widthLeft/cardIconSize.dp).toInt())) {
                EmptyComponent(size = BOX_SIZE_IN_BUNDLE_DP)
            }
        }
    }
}

@Composable
fun EmptyComponent(size: Int) {
    Spacer(
        modifier = Modifier
            .size(size.dp)
            .padding(dimensionResource(R.dimen.padding_small))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopAppBar(
    onBackButtonClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Dashboard", overflow = TextOverflow.Ellipsis,) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleTopAppBar(
    onBackButtonClicked: () -> Unit,
    onEditBundleNameButtonClicked: () -> Unit,
    title: String,
) {
    TopAppBar(
        title = { Text(text = title, overflow = TextOverflow.Ellipsis,) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onEditBundleNameButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit bundle name"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleCreatorTopAppBar(
    numSelected: Int,
    closeBundleCreator: () -> Unit,
    onCreateClicked: () -> Unit,
    ) {

    val smallPadding = dimensionResource(R.dimen.padding_small)

    TopAppBar(
        title = { Text(text = "$numSelected Selected") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(
                onClick = { closeBundleCreator() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            Button(
                onClick = {
                    onCreateClicked()
                },
                enabled = numSelected > 0,
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Row() {
                    Text(
                        text = "Create",
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.size(smallPadding))
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done",
                    )
                }
            }
        }
    )
}

@Composable
fun CreateOptionButton(
    isCreateOptionsOpen: Boolean,
    openBundleCreator: () -> Unit,
    openDeckCreator: () -> Unit,
    toggleCreateOptions: () -> Unit,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (isCreateOptionsOpen) {
            Column(
                horizontalAlignment = Alignment.End

            ) {
                FloatingActionButton(
                    onClick = {
                        openDeckCreator()
                    },
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = "Create new deck", modifier = Modifier.padding(smallPadding))
                }
                FloatingActionButton(
                    onClick = {
                        openBundleCreator()
                    },
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = "Create new bundle", modifier = Modifier.padding(smallPadding))
                }
            }
        }
        FloatingActionButton(
            onClick = {
                toggleCreateOptions()
            },
            modifier = Modifier.padding(smallPadding)
        ) {
            if (isCreateOptionsOpen)
                Icon(Icons.Default.Close, contentDescription = "Close")
            else
                Icon(Icons.Default.Add, contentDescription = "Open")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateBundleDialog(
    editMode: Boolean = false,
    onDismissRequest: () -> Unit,
    onCreateClicked: (String) -> Unit,
    setUserInput: (String) -> Unit,
    userInput: String?,
    focusManager: FocusManager,
    ) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {

        val smallPadding = dimensionResource(R.dimen.padding_small)
        val mediumPadding = dimensionResource(R.dimen.padding_medium)
        var isError by remember { mutableStateOf(false) }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isError) 280.dp else 240.dp)
                .padding(mediumPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxSize()


            ) {
                Text(
                    text = "Name for the bundle:",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Column(

                ) {
                    if (isError) {
                        Text(
                            text = "This field is required.",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    OutlinedTextField(
                        value = userInput ?: "",
                        onValueChange = { setUserInput(it) },
                        label = { Text("Bundle name") },
                        isError = isError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Exit) }),
                        modifier = Modifier
                            .padding(bottom = smallPadding)
                    )
                }
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
                        onClick = {
                            if (userInput.isNullOrBlank()) {
                                isError = true
                            } else {
                                onCreateClicked(userInput)
                            }
                        }
                    ) { Text(if (editMode) "Confirm" else "Create") }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateDeckDialog(
    onDismissRequest: () -> Unit,
    onCreateClicked: (String) -> Unit,
    setUserInput: (String) -> Unit,
    userInput: String?,
    focusManager: FocusManager,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {

        val smallPadding = dimensionResource(R.dimen.padding_small)
        val mediumPadding = dimensionResource(R.dimen.padding_medium)
        var isError by remember { mutableStateOf(false) }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isError) 280.dp else 240.dp)
                .padding(mediumPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxSize()


            ) {
                Text(
                    text = "Name for the deck:",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Column() {
                    if (isError) {
                        Text(
                            text = "This field is required.",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Start,
                        )
                    }
                    OutlinedTextField(
                        value = userInput ?: "",
                        onValueChange = { setUserInput(it) },
                        label = { Text("Deck name") },
                        isError = isError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Exit) }),
                        modifier = Modifier
                            .padding(bottom = smallPadding)
                    )
                }
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
                        onClick = {
                            if (userInput.isNullOrBlank()) {
                                isError = true
                            } else {
                                onCreateClicked(userInput)
                            }
                        }
                    ) { Text("Create") }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    //device = "spec:orientation=landscape,width=333dp,height=808dp"
)
@Composable
fun DashboardScreenPreview() {
    FlashcardsTheme {
        DashboardScreen(viewModel(), {}, {})
    }
}