package com.example.hopshop.presentation.createList

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.hopshop.R
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.presentation.components.LoadingDialog
import com.example.hopshop.presentation.components.TextError
import com.example.hopshop.presentation.dashboard.DashboardLayout
import com.example.hopshop.presentation.destinations.BaseAuthScreenDestination
import com.example.hopshop.presentation.destinations.ListScreenDestination
import com.example.hopshop.presentation.list.ListState
import com.example.hopshop.presentation.main.bottomBarPadding
import com.example.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@SuppressLint("StateFlowValueCalledInComposition")
@Destination
@Composable
fun CreateListScreen(
    listId: String? = null,
    navigator: DestinationsNavigator,
    viewModel: CreateListViewModel = koinViewModel(parameters = { parametersOf(listId) }),
    navController: NavController,
) {
    val createListState = viewModel.createListState.collectAsState().value

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (createListState) {
            is CreateListState.Loading -> LoadingDialog(stringResource(R.string.home_loading))

            is CreateListState.Success -> CreateListLayout(
                navigator = navigator,
                list = createListState.list,
                createList = viewModel::createList,
                editList = viewModel::editList,
            )

            is CreateListState.Redirect -> navigator.navigate(ListScreenDestination(listId = createListState.listId))

            is CreateListState.Error -> TextError(createListState.message)

            is CreateListState.None -> Unit
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateListLayout(
    navigator: DestinationsNavigator,
    list: ListModel?,
    createList: (name: String, tag: String, sharedMail: String, description: String) -> Unit,
    editList: (listId: String, name: String, tag: String, description: String) -> Unit,
) {
    var listName by remember { mutableStateOf(list?.name ?: "") }
    var tag by remember { mutableStateOf(list?.tag ?: "") }
    var sharedMail by remember { mutableStateOf("") }
    var description by remember { mutableStateOf(list?.description ?: "") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TopAppBar(
            title = {

            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            navigationIcon = {
                Text(
                    text = if (list != null) "Edytuj listę zakupów" else "Dodaj nową listę zakupów",
                    style = Typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color("#344054".toColorInt())
                )
            },
            actions = {
                IconButton(
                    onClick = { navigator.popBackStack() },
                    modifier = Modifier
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = "Localized description"
                    )
                }
            },
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = listName,
                onValueChange = { listName = it },
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

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = tag,
                onValueChange = { tag = it },
                label = {
                    Text("Kategoria")
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

            if (list == null) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = sharedMail,
                    onValueChange = { sharedMail = it },
                    label = {
                        Text("Udostępnij listę")
                    },
                    placeholder = { Text("Wpisz adres e-mail") },
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
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = {
                    Text("Opis")
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
                minLines = 3,
                keyboardActions = KeyboardActions(
                    onDone = {}
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )
        }

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                if (list != null) {
                    editList(
                        list.id,
                        listName,
                        tag,
                        description
                    )
                } else {
                    createList(
                        listName,
                        tag,
                        sharedMail,
                        description
                    )
                }
            },
            contentPadding = PaddingValues(16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = if (list != null) "Zapisz" else "Utwórz listę",
                style = Typography.bodyMedium,
            )
        }
    }
}
//
//val context = LocalContext.current
//fun isAppInstalled(packageName: String, context: Context): Boolean {
//    val packageManager = context.packageManager
//
//    return try {
//        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
//        true
//    } catch (e: PackageManager.NameNotFoundException) {
//        false
//    }
//}


//enum class BottomBarDestination(
//    val direction: DirectionDestinationSpec,
//    val icon: ImageVector,
//    val label: String
//) {
//    DashboardScreen(DashboardScreenDestination, Icons.Default.Home, "Dashboard"),
//    ApiariesScreen(ApiariesScreenDestination, Icons.Default.Email, "Apiaries"),
//}

//@Composable
//fun BottomBar(
//    navController: NavController
//) {
//    val currentDestination: TypedDestination<*> = navController.appCurrentDestinationAsState().value
//        ?: NavGraphs.root.startAppDestination
//
//    NavigationBar {
//        BottomBarDestination.entries.forEach { destination ->
//            NavigationBarItem(
//                selected = currentDestination == destination.direction,
//                onClick = {
//                    navController.navigate(destination.direction, fun NavOptionsBuilder.() {
//                        launchSingleTop = true
//                    })
//                },
//                icon = { Icon(destination.icon, contentDescription = destination.label)},
//                label = { Text(destination.label) },
//            )
//        }
//    }
//}

@Composable
fun VerticalSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))