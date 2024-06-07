package com.example.hopshop.presentation.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.hopshop.R
import com.example.hopshop.data.model.UserModel
import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.presentation.components.GroceryListItem
import com.example.hopshop.presentation.components.LoadingDialog
import com.example.hopshop.presentation.components.TextError
import com.example.hopshop.presentation.components.TopBar
import com.example.hopshop.presentation.destinations.BaseAuthScreenDestination
import com.example.hopshop.presentation.destinations.CreateListScreenDestination
import com.example.hopshop.presentation.main.bottomBarPadding
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
    val listsState = viewModel.listsState.collectAsState().value

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
            .background(Color.Red)
    ) {
        when (val accountUserState = viewModel.accountUserState.collectAsState().value) {
            is AccountUserState.Loading -> LoadingDialog(stringResource(R.string.home_loading))

            is AccountUserState.SignedInState -> DashboardLayout(
                navigator = navigator,
                listsState = listsState,
                user = accountUserState.user,
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
) {
    var titlesState by remember { mutableIntStateOf(0) }
    val titles = listOf("Listy zakupów", "Wszystkie zakupy")

    Scaffold(
        topBar = {},
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.navigate(CreateListScreenDestination()) },
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
                title = "Dashboard",
                navigator = navigator,
                user = user
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                    color = Color("#344054".toColorInt())
                                )
                            },
                            modifier = if (titlesState == index) {
                                Modifier
                                    .height(40.dp)
                                    .clip(roundedCornerShape)
                                    .background(Color.White)
                                    .border(1.dp, Color("#EAECF0".toColorInt()), roundedCornerShape)
                            } else {
                                Modifier
                                    .height(40.dp)
                                    .clip(roundedCornerShape)
                                    .background(Color("#F2F4F7".toColorInt()))
                                    .border(1.dp, Color("#EAECF0".toColorInt()), roundedCornerShape)
                            }
                        )
                    }
                }

                Column {

                }

                when (titlesState) {
                    0 -> when (listsState) {
                        is ListsState.None -> Unit

                        is ListsState.Loading -> LoadingDialog(stringResource(R.string.home_loading))

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
                            tint = Color("#7F56D9".toColorInt()),
                            modifier = Modifier.size(56.dp)
                        )

                        Text(
                            text = "Dostępne już wkrótce.",
                            style = Typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color("#101828".toColorInt()),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(264.dp)
                        )

                        Text(
                            text = "Trwają prace nad tym widokiem. Zapraszam wkrótce.",
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