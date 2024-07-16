package com.example.hopshop.presentation.list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.rotate
import com.example.hopshop.data.model.ItemModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.data.util.DropdownMenuItemData
import com.example.hopshop.presentation.components.BottomSheet
import com.example.hopshop.presentation.components.Modal
import com.example.hopshop.presentation.dashboard.ShareListState
import com.example.hopshop.presentation.destinations.DashboardScreenDestination
import com.example.hopshop.presentation.main.SnackbarHandler
import com.example.hopshop.ui.theme.HopShopAppTheme
import org.koin.core.parameter.parametersOf

@SuppressLint("StateFlowValueCalledInComposition")
@Destination
@Composable
fun ListScreen(
    listId: String,
    snackbarHandler: SnackbarHandler,
    navigator: DestinationsNavigator,
    viewModel: ListViewModel = koinViewModel(parameters = { parametersOf(listId) }),
    navController: NavController
) {
    val listState = viewModel.listState.collectAsState().value
    val itemsState = viewModel.itemsState.collectAsState().value
    val itemsCountState = viewModel.itemsCountState.collectAsState().value
    val removeListState = viewModel.removeListState.collectAsState().value
    val createItemState = viewModel.createItemState.collectAsState().value
    val shareListState = viewModel.shareListState.collectAsState().value

    if (removeListState is RemoveListState.Success) navigator.navigate(DashboardScreenDestination)

    LaunchedEffect(shareListState) {
        launch {
            if (shareListState is ShareListState.Error) snackbarHandler.showErrorSnackbar(message = shareListState.message)
            if (shareListState is ShareListState.Success) snackbarHandler.showSuccessSnackbar(
                message = "Pomyślnie udostępniono listę"
            )
        }
    }

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (listState) {
            is ListState.Error -> Unit
            is ListState.Loading -> LoadingDialog(stringResource(R.string.loading))
            is ListState.Success -> {
                ListLayout(
                    navigator = navigator,
                    list = listState.list,
                    itemsState = itemsState,
                    itemsCount = itemsCountState,
                    setItemSelected = viewModel::setItemSelected,
                    removeItem = viewModel::removeItem,
                    removeList = viewModel::removeList,
                    shareList = viewModel::shareList,
                    removeSharedList = viewModel::removeSharedList,
                    createItemState = createItemState,
                    shareListState = shareListState,
                    createItem = viewModel::createItem,
                )
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun ListLayout(
    navigator: DestinationsNavigator,
    list: ListModel,
    itemsState: ItemsState,
    itemsCount: ItemsCountState,
    setItemSelected: (String, Boolean) -> Unit,
    removeItem: (String) -> Unit,
    removeList: (String) -> Unit,
    shareList: (String, String) -> Unit,
    removeSharedList: (String, String) -> Unit,
    createItemState: CreateItemState,
    shareListState: ShareListState,
    createItem: (String, String) -> Unit,
) {
    var isCreateItemDialogVisible by remember { mutableStateOf(false) }
    var isDropdownMenuVisible by remember { mutableStateOf(false) }
    var isModalActive by remember { mutableStateOf(false) }
    var isShareListDialogVisible by remember { mutableStateOf(false) }

    val menuItems = listOf(
        DropdownMenuItemData(
            icon = Icons.Outlined.Edit,
            text = stringResource(R.string.edit_list),
            onClick = {
//                navigator.navigate(CreateListScreenDestination(listId = list.id))
            }
        ),
        DropdownMenuItemData(
            icon = Icons.Outlined.Share,
            text = stringResource(R.string.share),
            onClick = {
                isShareListDialogVisible = true
                isDropdownMenuVisible = false
            }
        ),
        DropdownMenuItemData(
            icon = Icons.Outlined.Clear,
            text = stringResource(R.string.loading), //usuń,
            onClick = {
                isModalActive = true
            }
        ),
    )

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
                                color = HopShopAppTheme.colors.black,
                            )

                            if (list.isShared) {
                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(
                                    imageVector = Icons.Filled.FolderShared,
                                    contentDescription = "Localized description",
                                    tint = HopShopAppTheme.colors.purple,
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
                                onClick = {
                                    navigator.navigateUp()
                                },
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                HopShopAppTheme.colors.purpleWhite,
                                                shape = CircleShape
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Localized description",
                                            tint = HopShopAppTheme.colors.purple,
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
                                onClick = { isDropdownMenuVisible = !isDropdownMenuVisible },
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                HopShopAppTheme.colors.purpleWhite,
                                                shape = CircleShape
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.MoreVert,
                                            contentDescription = "Localized description",
                                            tint = HopShopAppTheme.colors.purple,
                                        )
                                    }
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
                                tint = HopShopAppTheme.colors.grey,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "${stringResource(R.string.selected_items)} ${itemsCount.itemsCount.selected} z ${itemsCount.itemsCount.items}",
                                style = Typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = HopShopAppTheme.colors.grey,
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
                                tint = HopShopAppTheme.colors.grey,
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(-45f)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = list.tag,
                                style = Typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = HopShopAppTheme.colors.grey,
                            )
                        }
                    }
                }
            }

            Dropdown(
                isDropdownMenuVisible = isDropdownMenuVisible,
                setDropdownMenuVisible = { isDropdownMenuVisible = it },
                menuItems = menuItems
            )

            Modal(
                dialogTitle = stringResource(R.string.remove_modal_title),
                dialogText = stringResource(R.string.remove_modal_text),
                icon = Icons.Filled.Warning,
                isModalActive = isModalActive,
                onDismissRequest = { isModalActive = false },
                onConfirmation = { removeList(list.id) },
            )
        },
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

                is ItemsState.Loading -> LoadingDialog(stringResource(R.string.loading))

                is ItemsState.Success -> {
                    if (itemsState.items.isNotEmpty()) {
                        SwipeToDismiss(
                            items = itemsState.items,
                            removeItem = removeItem,
                            setItemSelected = setItemSelected,
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
                                tint = HopShopAppTheme.colors.purple,
                                modifier = Modifier.size(56.dp)
                            )

                            Text(
                                text = stringResource(R.string.empty_list_title),
                                style = Typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = HopShopAppTheme.colors.black90,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(264.dp)
                            )

                            Text(
                                text = stringResource(R.string.empty_list_text),
                                style = Typography.bodyMedium,
                                color = HopShopAppTheme.colors.grey,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(264.dp)
                            )

                        }
                    }
                }
            }
        }

        BottomSheet(
            isVisible = isCreateItemDialogVisible,
            setVisible = { isCreateItemDialogVisible = it },
        ) {
            CreateItemDialog(
                setVisible = { isCreateItemDialogVisible = it },
                listId = list.id,
                createItemState = createItemState,
                createItem = createItem,
            )
        }


//        BottomSheet(
//            isVisible = isShareListDialogVisible,
//            setVisible = { isShareListDialogVisible = it },
//            sheetState = sheetState,
//        ) {
//            ShareListBottomSheet(
//                setVisible = { isShareListDialogVisible = it },
//                listId = list.id,
//                shareList = shareList,
//                shareListState = shareListState,
//            )
//        }
    }
}

@Composable
fun SwipeToDismiss(
    items: List<ItemModel>,
    setItemSelected: (String, Boolean) -> Unit,
    removeItem: (String) -> Unit,
) {
    LazyColumn {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            SwipeToDismissItem(
                item = item,
                onRemove = { removeItem(item.id) },
                setItemSelected = setItemSelected
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
    setItemSelected: (String, Boolean) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            when (state) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    coroutineScope.launch {
                        delay(300)
                        onRemove()
                    }
                    true
                }

                SwipeToDismissBoxValue.EndToStart -> false
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )
    val color: Color = when (swipeToDismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> {
            HopShopAppTheme.colors.red
        }

        SwipeToDismissBoxValue.EndToStart -> {
            Color.Transparent
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
        ChangeableButton(
            item = item,
            setItemSelected = setItemSelected
        )
    }

}

@Composable
fun DeleteBackground(
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
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
    setItemSelected: (String, Boolean) -> Unit,
) {
    val buttonText by remember { mutableStateOf(item.name) }
    val isEditing by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var isSelected = item.selected

    OutlinedTextField(
        value = buttonText,
        onValueChange = {},
        enabled = isEditing,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isSelected = !isSelected
                        setItemSelected(item.id, isSelected)
                    }
                )
            }
            .focusRequester(focusRequester),
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckBox,
                    contentDescription = "Localized description",
                    tint = HopShopAppTheme.colors.purple,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = "Localized description",
                    tint = HopShopAppTheme.colors.gray30,
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
            disabledContainerColor = if (isSelected && !isEditing) HopShopAppTheme.colors.purpleWhite else HopShopAppTheme.colors.grey20,
            disabledIndicatorColor = if (isSelected && !isEditing) HopShopAppTheme.colors.purple50 else HopShopAppTheme.colors.grey50,
            disabledTextColor = if (isSelected && !isEditing) HopShopAppTheme.colors.purple else HopShopAppTheme.colors.black,

            focusedContainerColor = HopShopAppTheme.colors.grey20,
            focusedIndicatorColor = HopShopAppTheme.colors.grey50,
            focusedTextColor = HopShopAppTheme.colors.black,
        )
    )
}


@Composable
fun VerticalSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemDialog(
    setVisible: (Boolean) -> Unit,
    listId: String,
    createItemState: CreateItemState,
    createItem: (String, String) -> Unit,
) {
    var itemName by remember { mutableStateOf("") }

    if (createItemState is CreateItemState.Loading) LoadingDialog(stringResource(R.string.loading))

    Text(
        text = stringResource(R.string.modal_create_item_title),
        style = Typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = HopShopAppTheme.colors.black,
    )
    VerticalSpacer(16.dp)

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemName,
        onValueChange = { itemName = it },
        label = {
            Text(stringResource(R.string.form_create_item_label))
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = HopShopAppTheme.colors.purple,
            unfocusedBorderColor = HopShopAppTheme.colors.gray30,
        ),
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = HopShopAppTheme.colors.grey,
        ),
        maxLines = 1,
        keyboardActions = KeyboardActions(
            onDone = {}
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        )
    )

    VerticalSpacer(24.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                createItem(itemName, listId)
                setVisible(false)
            },
            contentPadding = PaddingValues(16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Localized description",
                tint = HopShopAppTheme.colors.white,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Dodaj",
                style = Typography.bodyMedium,
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareListBottomSheet(
    setVisible: (Boolean) -> Unit,
    listId: String,
    shareList: (String, String) -> Unit,
    shareListState: ShareListState,
) {
    var email by remember { mutableStateOf("") }

    if (shareListState is ShareListState.Loading) LoadingDialog(
        stringResource(R.string.loading)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Udostępnij listę",
            style = Typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = HopShopAppTheme.colors.black,
        )

        IconButton(
            onClick = { setVisible(false) },
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        HopShopAppTheme.colors.purpleWhite,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Localized description",
                    tint = HopShopAppTheme.colors.purple,
                )
            }
        }
    }

    VerticalSpacer(16.dp)

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = { email = it },
        label = {
            Text("Adres e-mail")
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = HopShopAppTheme.colors.purple,
            unfocusedBorderColor = HopShopAppTheme.colors.gray30,
        ),
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = HopShopAppTheme.colors.grey,
        ),
        maxLines = 1,
        keyboardActions = KeyboardActions(
            onDone = {}
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        )
    )

    VerticalSpacer(24.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                shareList(listId, email)
                setVisible(false)
            },
            contentPadding = PaddingValues(16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Localized description",
                tint = HopShopAppTheme.colors.white,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Udostępnij",
                style = Typography.bodyMedium,
            )
        }
    }
}

@Composable
fun Dropdown(
    isDropdownMenuVisible: Boolean,
    setDropdownMenuVisible: (Boolean) -> Unit,
    menuItems: List<DropdownMenuItemData>
) {
    if (!isDropdownMenuVisible) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .offset(y = 64.dp)
            .background(Color.LightGray)
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { setDropdownMenuVisible(false) },
            modifier = Modifier.width(240.dp)
        ) {
            menuItems.forEachIndexed { index, item ->
                if (index == menuItems.size - 1 && menuItems.size > 1) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(6.dp))
                }

                DropdownMenuItem(
                    text = {
                        Text(item.text)
                    },
                    onClick = {
                        item.onClick()
                    },
                    leadingIcon = {
                        Icon(
                            item.icon,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}