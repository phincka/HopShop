package pl.hincka.hopshop.presentation.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.hincka.hopshop.R
import pl.hincka.hopshop.components.bottomSheet.BottomSheet
import pl.hincka.hopshop.components.bottomSheet.CreateListBottomSheet
import pl.hincka.hopshop.components.design.VerticalSpacer
import pl.hincka.hopshop.components.infoContainer.InfoContainer
import pl.hincka.hopshop.components.modalDialog.FormModalDialog
import pl.hincka.hopshop.components.topBar.TopBar
import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.data.model.UserModel
import pl.hincka.hopshop.data.util.AccountUserState
import pl.hincka.hopshop.presentation.auth.signIn.BackgroundShapes
import pl.hincka.hopshop.presentation.components.GroceryListItem
import pl.hincka.hopshop.presentation.components.LoadingDialog
import pl.hincka.hopshop.presentation.components.TextError
import pl.hincka.hopshop.presentation.main.SnackbarHandler
import pl.hincka.hopshop.presentation.main.bottomBarPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import pl.hincka.hopshop.nav.destinations.BaseAuthScreenDestination
import pl.hincka.hopshop.nav.destinations.ListScreenDestination

@SuppressLint("StateFlowValueCalledInComposition")
@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreen(
    message: String? = null,
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = koinViewModel(),
    navController: NavController,
    snackbarHandler: SnackbarHandler,
) {
    LaunchedEffect(message) {
        launch {
            message?.let {
                snackbarHandler.showSuccessSnackbar(
                    message = it
                )
            }
        }
    }

    val accountUserState = viewModel.accountUserState.collectAsState().value

    var isShareListDialogVisible by remember { mutableStateOf(false) }

    val createListState = viewModel.createListState.collectAsState().value
    val listsState = viewModel.listsState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .bottomBarPadding(navController = navController)
    ) {
        BackgroundShapes()

        when (accountUserState) {
            is AccountUserState.Loading -> LoadingDialog()
            is AccountUserState.SignedInState -> DashboardLayout(
                listsState = listsState,
                user = accountUserState.user,
                isShareListDialogVisible = isShareListDialogVisible,
                setVisible = { isShareListDialogVisible = it },
                createList = viewModel::createList,
                createListState = createListState,
                signOut = viewModel::signOut,
                navigateToGroceryList = { navigator.navigate(ListScreenDestination(listId = it)) }
            )

            is AccountUserState.GuestState -> navigator.navigate(BaseAuthScreenDestination)
            is AccountUserState.Error -> TextError(accountUserState.message)
            is AccountUserState.None -> Unit
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun DashboardLayout(
    listsState: ListsState,
    user: UserModel,
    isShareListDialogVisible: Boolean,
    setVisible: (Boolean) -> Unit,
    createList: (FormListModel) -> Unit,
    createListState: CreateListState,
    signOut: () -> Unit,
    navigateToGroceryList: (String) -> Unit,
) {
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
        containerColor = Color.Transparent
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TopBar(
                user = user,
                signOut = signOut,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                when (listsState) {
                    is ListsState.None -> Unit

                    is ListsState.Loading -> LoadingDialog()

                    is ListsState.Success -> GroceryList(
                        groceryLists = listsState.lists,
                        navigateToGroceryList = navigateToGroceryList,
                    )

                    is ListsState.Error -> Unit
                }
            }
        }

        if (user.isModalAlternativeEnable) {
            FormModalDialog(
                isVisible = isShareListDialogVisible,
                setVisible = setVisible,
            ) {
                CreateListBottomSheet(
                    setVisible = setVisible,
                    executeFunction = createList,
                    createListState = createListState,
                )
            }
        } else {
            BottomSheet(
                isVisible = isShareListDialogVisible,
                setVisible = setVisible,
            ) {
                CreateListBottomSheet(
                    setVisible = setVisible,
                    executeFunction = createList,
                    createListState = createListState,
                )
            }
        }
    }
}

@Composable
fun GroceryList(
    groceryLists: List<ListModel>,
    navigateToGroceryList: (String) -> Unit,
) {
    if (groceryLists.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                VerticalSpacer(8.dp)
            }
            items(groceryLists) { list ->
                GroceryListItem(
                    navigateToGroceryList = navigateToGroceryList,
                    list = list
                )
            }
            item {
                VerticalSpacer(8.dp)
            }
        }
    } else {
        InfoContainer(
            title = stringResource(R.string.empty_lists_title),
            text = stringResource(R.string.empty_lists_text),
        )
    }
}