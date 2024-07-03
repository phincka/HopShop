package com.example.hopshop.presentation.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.hopshop.R
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.data.model.UserModel
import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.presentation.components.BottomSheet
import com.example.hopshop.presentation.components.GroceryListItem
import com.example.hopshop.presentation.components.LoadingDialog
import com.example.hopshop.presentation.components.TextError
import com.example.hopshop.presentation.components.TopBar
import com.example.hopshop.presentation.destinations.BaseAuthScreenDestination
import com.example.hopshop.presentation.list.VerticalSpacer
import com.example.hopshop.presentation.main.bottomBarPadding
import com.example.hopshop.ui.theme.HopShopAppTheme
import com.example.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = koinViewModel(),
    navController: NavController
) {
    var isShareListDialogVisible by remember { mutableStateOf(false) }

    val createListState = viewModel.createListState.collectAsState().value
    val listsState = viewModel.listsState.collectAsState().value

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
            .background(Color.Red)
    ) {
        when (val accountUserState = viewModel.accountUserState.collectAsState().value) {
            is AccountUserState.Loading -> LoadingDialog(stringResource(R.string.loading))
            is AccountUserState.SignedInState -> DashboardLayout(
                navigator = navigator,
                listsState = listsState,
                user = accountUserState.user,
                isShareListDialogVisible = isShareListDialogVisible,
                setVisible = { isShareListDialogVisible = it },
                createList = viewModel::createList,
                createListState = createListState,
            )
            is AccountUserState.GuestState -> navigator.navigate(BaseAuthScreenDestination)
            is AccountUserState.Error -> TextError(accountUserState.message)
            is AccountUserState.None -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun DashboardLayout(
    navigator: DestinationsNavigator,
    listsState: ListsState,
    user: UserModel,
    isShareListDialogVisible: Boolean,
    setVisible: (Boolean) -> Unit,
    createList: (String, String, String, String) -> Unit,
    createListState: CreateListState,
) {
    var titlesState by remember { mutableIntStateOf(0) }
    val titles = listOf(
        stringResource(R.string.dashboard_tile_lists),
        stringResource(R.string.dashboard_tile_items)
    )

    Scaffold(
        topBar = {},
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { setVisible(true) },
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Filled.Add, "Large floating action button")
            }
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TopBar(
                title = stringResource(R.string.dashboard_topbar_title),
                navigator = navigator,
                user = user
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PrimaryTabRow(
                    selectedTabIndex = titlesState,
                    indicator = {},
                    divider = {}
                ) {
                    titles.forEachIndexed { index, title ->
                        var roundedCornerShape = RoundedCornerShape(0.dp)
                        if (index == 0) roundedCornerShape =
                            RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                        if (index == titles.size - 1) roundedCornerShape =
                            RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)

                        Tab(
                            selected = titlesState == index,
                            onClick = { titlesState = index },
                            text = {
                                Text(
                                    text = title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = Typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = HopShopAppTheme.colors.black,
                                )
                            },
                            modifier = if (titlesState == index) {
                                Modifier
                                    .height(40.dp)
                                    .clip(roundedCornerShape)
                                    .background(Color.White)
                                    .border(1.dp, HopShopAppTheme.colors.grey50, roundedCornerShape)
                            } else {
                                Modifier
                                    .height(40.dp)
                                    .clip(roundedCornerShape)
                                    .background(HopShopAppTheme.colors.grey10)
                                    .border(1.dp, HopShopAppTheme.colors.grey50, roundedCornerShape)
                            }
                        )
                    }
                }

                when (titlesState) {
                    0 -> when (listsState) {
                        is ListsState.None -> Unit

                        is ListsState.Loading -> LoadingDialog(stringResource(R.string.loading))

                        is ListsState.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                items(items = listsState.lists) { list ->
                                    GroceryListItem(
                                        navigator = navigator,
                                        list = list
                                    )
                                }
                            }
                        }

                        is ListsState.Error -> Unit
                    }
                    // TODO Migrate to custom component
                    1 -> Column(
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
                            text = stringResource(R.string.in_progress_title),
                            style = Typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = HopShopAppTheme.colors.black90,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(264.dp)
                        )

                        Text(
                            text = stringResource(R.string.in_progress_text),
                            style = Typography.bodyMedium,
                            color = HopShopAppTheme.colors.grey,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(264.dp)
                        )
                    }
                }
            }
        }

        BottomSheet(
            isVisible = isShareListDialogVisible,
            setVisible = setVisible,
        ) {
            CreateListBottomSheet(
                setVisible = setVisible,
                createList = createList,
                createListState = createListState,
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListBottomSheet(
    setVisible: (Boolean) -> Unit,
    createList: (String, String, String, String) -> Unit,
    createListState: CreateListState,
    list: ListModel? = null,
) {
    var listName by remember { mutableStateOf(list?.name ?: "") }
    var tag by remember { mutableStateOf(list?.tag ?: "") }
    var sharedMail by remember { mutableStateOf("") }
    var description by remember { mutableStateOf(list?.description ?: "") }

    if (createListState is CreateListState.Loading) LoadingDialog(
        stringResource(R.string.loading)
    )

    Text(
        text = stringResource(R.string.create_list),
        style = Typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = HopShopAppTheme.colors.black,
    )

    VerticalSpacer(16.dp)

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = listName,
        onValueChange = { listName = it },
        label = {
            Text(stringResource(R.string.list_name))
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

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = tag,
        onValueChange = { tag = it },
        label = {
            Text(stringResource(R.string.category))
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

    if (list == null) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sharedMail,
            onValueChange = { sharedMail = it },
            label = {
                Text(stringResource(R.string.share))
            },
            placeholder = { Text(stringResource(R.string.form_email_label)) },
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
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = description,
        onValueChange = { description = it },
        label = {
            Text(stringResource(R.string.form_description_label))
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
        minLines = 3,
        keyboardActions = KeyboardActions(
            onDone = {}
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        )
    )

    Button(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        onClick = {
            createList(
                listName,
                tag,
                sharedMail,
                description
            )
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
            text = stringResource(R.string.form_create_list_button),
            style = Typography.bodyMedium,
        )
    }
}