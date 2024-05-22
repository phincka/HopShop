package com.example.hopshop.presentation.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.hopshop.R
import com.example.hopshop.presentation.components.LoadingDialog
import com.example.hopshop.presentation.main.bottomBarPadding
import com.example.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.window.Dialog
import com.example.hopshop.data.model.ItemModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.presentation.components.TextError
import org.koin.core.parameter.parametersOf


@SuppressLint("StateFlowValueCalledInComposition")
@Destination
@Composable
fun ListScreen(
    listId: String,
    navigator: DestinationsNavigator,
    viewModel: ListViewModel = koinViewModel(parameters = { parametersOf(listId) }),
    navController: NavController
) {
    val listState = viewModel.listState.collectAsState().value
    val itemsState = viewModel.itemsState.collectAsState().value
    val itemsCountState = viewModel.itemsCountState.collectAsState().value

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
            .background(Color.White)
    ) {

        when (listState) {
            is ListState.Error -> Unit

            is ListState.Loading -> LoadingDialog(stringResource(R.string.home_loading))

            is ListState.Success -> {
                ListLayout(
                    navigator = navigator,
                    list = listState.list,
                    itemsState = itemsState,
                    itemsCount = itemsCountState,
                    viewModel = viewModel
                )
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListLayout(
    navigator: DestinationsNavigator,
    list: ListModel,
    itemsState: ItemsState,
    itemsCount: ItemsCountState,
    viewModel: ListViewModel
) {
    var isCreateItemDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = list.name,
                                style = Typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color("#344054".toColorInt())
                            )

                            if (list.isShared) {
                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(
                                    imageVector = Icons.Filled.FolderShared,
                                    contentDescription = "Localized description",
                                    tint = Color("#7F56D9".toColorInt()),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    navigationIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { navigator.popBackStack() },
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color("#F9F5FF".toColorInt()),
                                                shape = CircleShape
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Localized description",
                                            tint = Color("#7F56D9".toColorInt()),
                                            modifier = Modifier
                                        )
                                    }
                                }
                            }
                        }
                    },
                    actions = {
                        Row(
                            horizontalArrangement = Arrangement.End,
                        ) {
                            IconButton(
                                onClick = { },
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.End
                                ) {
//                                    Box(
//                                        modifier = Modifier
//                                            .background(
//                                                Color("#F9F5FF".toColorInt()),
//                                                shape = CircleShape
//                                            )
//                                            .padding(8.dp)
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Filled.MoreVert,
//                                            contentDescription = "Localized description",
//                                            tint = Color("#7F56D9".toColorInt()),
//                                        )
//                                    }
                                }
                            }
                        }
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    if (itemsCount is ItemsCountState.Success) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Localized description",
                                tint = Color("#667085".toColorInt()),
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "Kupiono ${itemsCount.itemsCount.selected} z ${itemsCount.itemsCount.items}",
                                style = Typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color("#667085".toColorInt()),
                            )
                        }
                    }

                    if (list.tag.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Label,
                                contentDescription = "Localized description",
                                tint = Color("#667085".toColorInt()),
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(-45f)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = list.tag,
                                style = Typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color("#667085".toColorInt()),
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreateItemDialogVisible = true },
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Filled.Add, "Large floating action button")
            }
        },
        containerColor = Color.White,

        modifier = Modifier.padding(horizontal = 16.dp),

        ) {

        Column(
            modifier = Modifier.padding(it)
        ) {
            when (itemsState) {

                is ItemsState.Error -> Unit

                is ItemsState.Loading -> {
                    Log.w("LOG_HIN", itemsState.toString())
                    LoadingDialog(stringResource(R.string.home_loading))
                }

                is ItemsState.Success -> {
                    Log.w("LOG_HIN", itemsState.toString())
                    if (itemsState.items.isNotEmpty()) {
                        SwipeToDismissExample(
                            items = itemsState.items,
                            viewModel = viewModel
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(top = 105.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Label,
                                contentDescription = "Localized description",
                                tint = Color("#7F56D9".toColorInt()),
                                modifier = Modifier.size(56.dp)
                            )

                            Text(
                                text = "Add items to your list",
                                style = Typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color("#101828".toColorInt()),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(264.dp)
                            )

                            Text(
                                text = "Your smart shopping list will shown here. start by creating a new list",
                                style = Typography.bodyMedium,
                                color = Color("#667085".toColorInt()),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(264.dp)
                            )

                        }
                    }
                }
            }
        }
    }


    CreateItemDialog(
        isVisible = isCreateItemDialogVisible,
        setVisible = { isCreateItemDialogVisible = it },
        listId = list.id,
        viewModel = viewModel
    )
}

@Composable
fun SwipeToDismissExample(items: List<ItemModel>, viewModel: ListViewModel) {
    val list = remember { items }

    LazyColumn {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            SwipeToDismissItem(
                item = item,
                onRemove = {
                    viewModel.removeItem(itemId = item.id)
                },
                onEdit = { Log.d("LOG_HINCKA", "EDIT!") },
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    item: ItemModel,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    viewModel: ListViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            when (state) {
                SwipeToDismissBoxValue.EndToStart -> {
                    coroutineScope.launch {
                        delay(300)
                        onRemove()
                    }
                    true
                }

                SwipeToDismissBoxValue.StartToEnd -> false
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )
    val color: Color = when (swipeToDismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            Color.Red
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            Color.Green.copy(alpha = 0.3f)
        }

        SwipeToDismissBoxValue.Settled -> {
            Color.Transparent
        }
    }

    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            DeleteBackground(color)
        },
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ) {
        ChangeableButton(item, viewModel)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun ChangeableButton(
    item: ItemModel,
    viewModel: ListViewModel
) {
    val buttonText by remember { mutableStateOf(item.name) }
    val isEditing by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var isSelected = item.selected

    OutlinedTextField(
        value = buttonText,
        onValueChange = {
//            newText ->
//            if (isEditing) {
//                buttonText = newText
//            }
        },
        enabled = isEditing,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        Log.w("LOG_HIN", item.selected.toString())
                        isSelected = !isSelected

                        viewModel.setItemSelected(itemId = item.id, isSelected = isSelected)
                    }
                )
            }
            .focusRequester(focusRequester),
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckBox,
                    contentDescription = "Localized description",
                    tint = Color("#7F56D9".toColorInt()),
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = "Localized description",
                    tint = Color("#D0D5DD".toColorInt()),
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        ),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = if (isSelected && !isEditing) Color("#F9F5FF".toColorInt()) else Color(
                "#FCFCFD".toColorInt()
            ),
            disabledIndicatorColor = if (isSelected && !isEditing) Color("#D6BBFB".toColorInt()) else Color(
                "#EAECF0".toColorInt()
            ),
            disabledTextColor = if (isSelected && !isEditing) Color("#7F56D9".toColorInt()) else Color(
                "#344054".toColorInt()
            ),

            focusedContainerColor = Color("#FCFCFD".toColorInt()),
            focusedIndicatorColor = Color("#EAECF0".toColorInt()),
            focusedTextColor = Color("#344054".toColorInt()),
        )
    )
}


@Composable
fun VerticalSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))

@Composable
fun AccessLargeLogIcons(logUrls: List<String>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        logUrls.take(4).forEachIndexed { index, url ->
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = url)
                    .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                        transformations(CircleCropTransformation())
                    }).build()
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color("#EAECF0".toColorInt()), CircleShape),
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
        if (logUrls.size > 2) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color("#F9F5FF".toColorInt()))
                    .border(1.dp, Color("#EAECF0".toColorInt()), CircleShape),
            ) {
                Text(
                    text = "+${logUrls.size - 1}",
                    style = Typography.labelMedium,
                    color = Color("#7F56D9".toColorInt()),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemDialog(
    isVisible: Boolean,
    setVisible: (Boolean) -> Unit,
    listId: String,
    viewModel: ListViewModel
) {
    if (!isVisible) return

    var itemName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { setVisible(false) }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp),
            shape = RoundedCornerShape(32.dp),
        ) {
            if (viewModel.createItemState.value is CreateItemState.Loading) LoadingDialog(
                stringResource(R.string.home_loading)
            )
            if (viewModel.createItemState.value is CreateItemState.Success) setVisible(false)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Dodaj item",
                        style = Typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color("#344054".toColorInt())
                    )

                    IconButton(
                        onClick = { },
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color("#F9F5FF".toColorInt()), shape = CircleShape)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Localized description",
                                    tint = Color("#7F56D9".toColorInt()),
                                    modifier = Modifier
                                        .size(16.dp)
                                )
                            }
                        }
                    }
                }

                VerticalSpacer(16.dp)

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = {
                        Text("Nazwa listy")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color("#7F56D9".toColorInt()),
                        unfocusedBorderColor = Color("#D0D5DD".toColorInt()),
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color("#667085".toColorInt())
                    ),
                    maxLines = 1,
                    keyboardActions = KeyboardActions(
                        onDone = {}
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                if (viewModel.createItemState.value is CreateItemState.Error) {
                    VerticalSpacer(8.dp)
                    TextError(text = "Nie udało się dodać elementu.")
                }

                VerticalSpacer(16.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.createItem(
                                name = itemName,
                                listId = listId
                            )
                        },
                        contentPadding = PaddingValues(16.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description",
                            tint = Color("#ffffff".toColorInt()),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Add new item",
                            style = Typography.bodyMedium,
                        )
                    }
                }

            }


        }
    }
}