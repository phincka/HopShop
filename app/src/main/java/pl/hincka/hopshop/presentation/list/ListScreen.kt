package pl.hincka.hopshop.presentation.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.hincka.hopshop.R
import pl.hincka.hopshop.components.bottomSheet.BottomSheet
import pl.hincka.hopshop.components.bottomSheet.CreateItemBottomSheet
import pl.hincka.hopshop.components.bottomSheet.CreateListBottomSheet
import pl.hincka.hopshop.components.dropdownMenu.DropdownMenu
import pl.hincka.hopshop.components.infoContainer.InfoContainer
import pl.hincka.hopshop.components.modalDialog.FormModalDialog
import pl.hincka.hopshop.components.modalDialog.ModalDialog
import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ItemModel
import pl.hincka.hopshop.data.model.ItemsCountModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.data.model.UserModel
import pl.hincka.hopshop.data.util.AccountUserState
import pl.hincka.hopshop.data.util.DropdownMenuItemData
import pl.hincka.hopshop.presentation.auth.signIn.BackgroundShapes
import pl.hincka.hopshop.presentation.components.LoadingDialog
import pl.hincka.hopshop.presentation.dashboard.CreateListState
import pl.hincka.hopshop.presentation.dashboard.ShareListState
import pl.hincka.hopshop.presentation.main.SnackbarHandler
import pl.hincka.hopshop.presentation.main.bottomBarPadding
import pl.hincka.hopshop.ui.theme.AppTheme
import pl.hincka.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pl.hincka.hopshop.nav.destinations.DashboardScreenDestination
import pl.hincka.hopshop.presentation.list.ClearListItemsState
import pl.hincka.hopshop.presentation.list.CreateItemState
import pl.hincka.hopshop.presentation.list.GenerateShareCodeState
import pl.hincka.hopshop.presentation.list.ItemsCountState
import pl.hincka.hopshop.presentation.list.ItemsState
import pl.hincka.hopshop.presentation.list.ListState
import pl.hincka.hopshop.presentation.list.ListViewModel
import pl.hincka.hopshop.presentation.list.RemoveItemState
import pl.hincka.hopshop.presentation.list.RemoveListState
import pl.hincka.hopshop.presentation.list.RemoveSharedListState

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
    val accountUserState = viewModel.accountUserState.collectAsState().value
    val listState = viewModel.listState.collectAsState().value
    val itemsState = viewModel.itemsState.collectAsState().value
    val itemsCountState = viewModel.itemsCountState.collectAsState().value
    val removeListState = viewModel.removeListState.collectAsState().value
    val removeSharedListState = viewModel.removeSharedListState.collectAsState().value
    val createItemState = viewModel.createItemState.collectAsState().value
    val removeItemState = viewModel.removeItemState.collectAsState().value
    val shareListState = viewModel.shareListState.collectAsState().value
    val clearListItemsState = viewModel.clearListItemsState.collectAsState().value
    val createListState = viewModel.createListState.collectAsState().value
    val generateShareCodeState = viewModel.generateShareCodeState.collectAsState().value

    if (removeListState is RemoveListState.Success) {
        navigator.navigate(DashboardScreenDestination(message = "Pomyślnie usunięto listę"))
    }

    if (removeSharedListState is RemoveSharedListState.Success) {
        navigator.navigate(DashboardScreenDestination(message = "Pomyślnie usunięto udostępnioną listę"))
    }

    LaunchedEffect(shareListState, clearListItemsState, removeItemState, createListState) {
        launch {
            if (shareListState is ShareListState.Error) snackbarHandler.showErrorSnackbar(message = shareListState.message)
            if (clearListItemsState is ClearListItemsState.Error) snackbarHandler.showErrorSnackbar(
                message = clearListItemsState.message
            )

            if (shareListState is ShareListState.Success) snackbarHandler.showSuccessSnackbar(
                message = "Pomyślnie udostępniono listę"
            )

            if (clearListItemsState is ClearListItemsState.Success) snackbarHandler.showSuccessSnackbar(
                message = "Pomyślnie wyczyszczono listę"
            )

            if (removeListState is RemoveListState.Error) snackbarHandler.showErrorSnackbar(
                message = removeListState.message
            )

            if (removeSharedListState is RemoveSharedListState.Error) snackbarHandler.showErrorSnackbar(
                message = removeSharedListState.message
            )

            if (removeItemState is RemoveItemState.Success) snackbarHandler.showSuccessSnackbar(
                message = "Pomyślnie usunięto"
            )

            if (removeItemState is RemoveItemState.Error) snackbarHandler.showErrorSnackbar(
                message = "Wystąpił błąd podczas usuwania"
            )

            if (createListState is CreateListState.Success) snackbarHandler.showSuccessSnackbar(
                message = "Pomyślnie zmieniono listę"
            )

            if (createListState is CreateListState.Error) snackbarHandler.showErrorSnackbar(
                message = "Wystąpił błąd podczas edycji listy"
            )
        }
    }

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
            .background(Color.White)
    ) {
        BackgroundShapes()

        if (accountUserState is AccountUserState.SignedInState) {

            when (listState) {
                is ListState.Error -> Unit
                is ListState.Loading -> LoadingDialog()
                is ListState.Success -> {
                    ListLayout(
                        navigator = navigator,
                        list = listState.list,
                        itemsState = itemsState,
                        itemsCountState = itemsCountState,
                        setItemSelected = viewModel::setItemSelected,
                        removeItem = viewModel::removeItem,
                        removeList = viewModel::removeList,
                        removeSharedList = viewModel::removeSharedList,
                        createItemState = createItemState,
                        createListState = createListState,
                        createItem = viewModel::createItem,
                        clearListItems = viewModel::clearListItems,
                        editList = viewModel::editList,
                        user = accountUserState.user,
                        generateShareListQrCode = viewModel::generateShareListQrCode,
                        generateShareCodeState = generateShareCodeState,
                    )
                }
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
    itemsCountState: ItemsCountState,
    setItemSelected: (String, Boolean) -> Unit,
    removeItem: (String?) -> Unit,
    removeList: (String) -> Unit,
    removeSharedList: (String) -> Unit,
    createItemState: CreateItemState,
    createListState: CreateListState,
    createItem: (String, String) -> Unit,
    editList: (FormListModel) -> Unit,
    clearListItems: () -> Unit,
    user: UserModel,
    generateShareListQrCode: (String) -> Unit,
    generateShareCodeState: GenerateShareCodeState,
) {
    var isCreateItemDialogVisible by remember { mutableStateOf(false) }
    var isEditListDialogVisible by remember { mutableStateOf(false) }
    var isDropdownMenuVisible by remember { mutableStateOf(false) }
    var isModalActive by remember { mutableStateOf(false) }
    var isRemoveItemModalActive by remember { mutableStateOf(false) }
    var isShareListDialogVisible by remember { mutableStateOf(false) }

    var isShoppingCompleteModalActive by remember { mutableStateOf(true) }

    var removeItemId by remember { mutableStateOf<String?>(null) }


    val menuItems = listOf(
        DropdownMenuItemData(
            icon = Icons.Outlined.Edit,
            text = stringResource(R.string.edit_list),
            onClick = {
                isEditListDialogVisible = true
            },
            disabled = list.sharedIds.contains(user.uid),
        ),
        DropdownMenuItemData(
            icon = Icons.Outlined.Share,
            text = stringResource(R.string.share),
            onClick = {
                isShareListDialogVisible = true
                isDropdownMenuVisible = false
                generateShareListQrCode(list.id)
            },
            disabled = list.sharedIds.contains(user.uid),
        ),
        DropdownMenuItemData(
            icon = Icons.Outlined.Clear,
            text = stringResource(R.string.remove_modal_title),
            onClick = {
                isModalActive = true
            },
            disabled = false,
        ),
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = Color.Transparent,
                        titleContentColor = Color.Transparent,
                        actionIconContentColor = Color.Transparent,
                    ),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = list.name,
                                style = Typography.h5,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.black,
                            )

                            if (list.sharedIds.contains(user.uid)) {
                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(
                                    imageVector = Icons.Filled.FolderShared,
                                    contentDescription = "Localized description",
                                    tint = AppTheme.colors.purple,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
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
                                                AppTheme.colors.purpleWhite,
                                                shape = CircleShape
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Localized description",
                                            tint = AppTheme.colors.purple,
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
                                                AppTheme.colors.purpleWhite,
                                                shape = CircleShape
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.MoreVert,
                                            contentDescription = "Localized description",
                                            tint = AppTheme.colors.purple,
                                        )
                                    }
                                }
                            }
                        }
                    }
                )

                itemsCountState.takeIfSuccess()?.itemsCount?.let { itemsCountModel ->
                    ItemsCount(
                        itemsCountModel = itemsCountModel,
                        isShoppingCompleteModalActive = isShoppingCompleteModalActive,
                        setShoppingCompleteModalActive = { isShoppingCompleteModalActive = it },
                        clearListItems = clearListItems,
                    )
                }

                if (list.tag.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Label,
                            contentDescription = "Localized description",
                            tint = AppTheme.colors.grey,
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(-45f)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = list.tag,
                            style = Typography.small,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.colors.grey,
                        )
                    }
                }
            }

            DropdownMenu(
                isDropdownMenuVisible = isDropdownMenuVisible,
                setDropdownMenuVisible = { isDropdownMenuVisible = it },
                menuItems = menuItems
            )

            if (list.sharedIds.contains(user.uid)) {
                ModalDialog(
                    dialogTitle = "Usuń udostępnioną listę",
                    dialogText = "Czy na pewno chcesz usunąć udostępnioną listę?",
                    confirmButtonText = stringResource(R.string.remove_modal_remove),
                    dismissButtonText = stringResource(R.string.remove_modal_cancel),
                    icon = Icons.Filled.Warning,
                    isModalActive = isModalActive,
                    onDismissRequest = { isModalActive = false },
                    onConfirmation = { removeSharedList(list.id) },
                )
            } else {
                ModalDialog(
                    dialogTitle = stringResource(R.string.remove_modal_title),
                    dialogText = stringResource(R.string.remove_modal_text),
                    confirmButtonText = stringResource(R.string.remove_modal_remove),
                    dismissButtonText = stringResource(R.string.remove_modal_cancel),
                    icon = Icons.Filled.Warning,
                    isModalActive = isModalActive,
                    onDismissRequest = { isModalActive = false },
                    onConfirmation = { removeList(list.id) },
                )
            }

            ModalDialog(
                dialogTitle = "Usuń element listy",
                dialogText = "Czy na pewno chcesz usunąć ten element?",
                confirmButtonText = stringResource(R.string.remove_modal_remove),
                dismissButtonText = stringResource(R.string.remove_modal_cancel),
                icon = Icons.Filled.Warning,
                isModalActive = isRemoveItemModalActive,
                onDismissRequest = { isRemoveItemModalActive = false },
                onConfirmation = {
                    removeItem(removeItemId)
                    removeItemId = null
                    isRemoveItemModalActive = false
                },
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
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            when (itemsState) {
                is ItemsState.Error -> Unit

                is ItemsState.Loading -> LoadingDialog()

                is ItemsState.Success -> {
                    if (itemsState.items.isNotEmpty()) {
                        SwipeToDismiss(
                            items = itemsState.items,
                            removeItem = { itemId ->
                                isRemoveItemModalActive = true
                                removeItemId = itemId
                            },
                            setItemSelected = setItemSelected,
                        )
                    } else {
                        InfoContainer(
                            title = stringResource(R.string.empty_list_title),
                            text = stringResource(R.string.empty_list_text),
                        )
                    }
                }
            }
        }

        if (user.isModalAlternativeEnable) {
            FormModalDialog(
                isVisible = isCreateItemDialogVisible,
                setVisible = { isCreateItemDialogVisible = it },
            ) {
                CreateItemBottomSheet(
                    setVisible = { isCreateItemDialogVisible = it },
                    listId = list.id,
                    createItemState = createItemState,
                    createItem = createItem,
                )
            }
            FormModalDialog(
                isVisible = isEditListDialogVisible,
                setVisible = { isEditListDialogVisible = it },
            ) {
                CreateListBottomSheet(
                    setVisible = { isEditListDialogVisible = it },
                    executeFunction = editList,
                    createListState = createListState,
                    listModel = list,
                )
            }
        } else {
            BottomSheet(
                isVisible = isCreateItemDialogVisible,
                setVisible = { isCreateItemDialogVisible = it },
            ) {
                CreateItemBottomSheet(
                    setVisible = { isCreateItemDialogVisible = it },
                    listId = list.id,
                    createItemState = createItemState,
                    createItem = createItem,
                )
            }
            BottomSheet(
                isVisible = isEditListDialogVisible,
                setVisible = { isEditListDialogVisible = it },
            ) {
                CreateListBottomSheet(
                    setVisible = { isEditListDialogVisible = it },
                    executeFunction = editList,
                    createListState = createListState,
                    listModel = list,
                )
            }
        }

        FormModalDialog(
            isVisible = isShareListDialogVisible,
            setVisible = { isShareListDialogVisible = it },
        ) {
            when (generateShareCodeState) {
                is GenerateShareCodeState.None -> Unit

                is GenerateShareCodeState.Error -> {
                    Column(
                        modifier = Modifier.height(284.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Wystąpił błąd podczas generowania kodu udostępniania",
                            style = Typography.p,
                            color = AppTheme.colors.black,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                is GenerateShareCodeState.Loading -> LoadingDialog(
                    height = Modifier.height(284.dp)
                )

                is GenerateShareCodeState.Success -> {
                    generateShareCodeState.bitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Generated QR Code",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(256.dp),
                            contentScale = ContentScale.Fit
                        )

                        Text(
                            text = buildAnnotatedString {
                                append("Kod udostępniania: ")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                ) {
                                    append(generateShareCodeState.shareCode)
                                }
                            },
                            style = Typography.h5,
                            color = AppTheme.colors.black,
                        )
                    }
                }
            }
        }
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
    val swipeToDismissState = rememberSwipeToDismissBoxState()

    val backgroundColor = when (swipeToDismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> AppTheme.colors.red
        else -> Color.Transparent
    }

    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            DeleteBackground(backgroundColor)
        },
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ) {
        ChangeableButton(
            item = item,
            setItemSelected = setItemSelected
        )
    }

    LaunchedEffect(swipeToDismissState.currentValue) {
        if (swipeToDismissState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
            coroutineScope.launch {
                delay(300)
                onRemove()
                swipeToDismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }
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
                    tint = AppTheme.colors.purple,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = "Localized description",
                    tint = AppTheme.colors.gray30,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        textStyle = Typography.label.copy(
            fontWeight = FontWeight.SemiBold
        ),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = if (isSelected && !isEditing) AppTheme.colors.purpleWhite else AppTheme.colors.grey20,
            disabledIndicatorColor = if (isSelected && !isEditing) AppTheme.colors.purple50 else AppTheme.colors.grey50,
            disabledTextColor = if (isSelected && !isEditing) AppTheme.colors.purple else AppTheme.colors.black,
            focusedContainerColor = AppTheme.colors.grey20,
            focusedIndicatorColor = AppTheme.colors.grey50,
            focusedTextColor = AppTheme.colors.black,
        )
    )
}


@Composable
fun ItemsCount(
    itemsCountModel: ItemsCountModel,
    isShoppingCompleteModalActive: Boolean,
    setShoppingCompleteModalActive: (Boolean) -> Unit,
    clearListItems: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Localized description",
                tint = AppTheme.colors.grey,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "${stringResource(R.string.selected_items)} ${itemsCountModel.selected} z ${itemsCountModel.items}",
                style = Typography.small,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.grey,
            )
        }

        if (itemsCountModel.items == itemsCountModel.selected && itemsCountModel.items > 0) {
            ModalDialog(
                dialogTitle = stringResource(R.string.clear_list_modal_title),
                dialogText = stringResource(R.string.clear_list_modal_text),
                confirmButtonText = stringResource(R.string.clear_list_modal_remove),
                dismissButtonText = stringResource(R.string.clear_list_modal_cancel),
                icon = Icons.Filled.Warning,
                isModalActive = isShoppingCompleteModalActive,
                onDismissRequest = {
                    setShoppingCompleteModalActive(false)
                },
                onConfirmation = {
                    clearListItems()
                    setShoppingCompleteModalActive(false)
                },
            )
        }
    }
}

private fun ItemsCountState.takeIfSuccess() = (this as? ItemsCountState.Success)