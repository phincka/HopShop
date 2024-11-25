package pl.hincka.hopshop.presentation.main

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import pl.hincka.hopshop.presentation.destinations.AccountScreenDestination
import pl.hincka.hopshop.presentation.destinations.DashboardScreenDestination
import com.ramcosta.composedestinations.navigation.clearBackStack
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Route
import kotlinx.coroutines.launch


enum class BottomNavigationItems(
    val direction: Route,
    val icon: ImageVector,
    val label: String,
) {
    Dashboard(
        direction = DashboardScreenDestination,
        icon = Icons.Default.Home,
        label = "Dashboard",
    ),
    Account(
        direction = AccountScreenDestination,
        icon = Icons.Default.AccountCircle,
        label = "Konto",
    )
}

enum class VisibleBottomBarDestination(val route: String) {
    Account(AccountScreenDestination.route);

    companion object {
        fun find(route: String): VisibleBottomBarDestination? = entries.find { it.route == route }
    }
}

@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi",
    "UnusedBoxWithConstraintsScope"
)
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = shouldShowBottomBarByDestination(currentDestinationRoute = currentRoute)
    val bottomBarOffset by animateDpAsState(
        targetValue = getBottomBarOffset(showBottomBar),
        label = ""
    )

    Scaffold(
        bottomBar = {
            currentRoute?.let {
                Column(modifier = Modifier.offset {
                    IntOffset(
                        x = 0,
                        y = bottomBarOffset.roundToPx()
                    )
                }) {
                    HorizontalDivider(
                        thickness = DIVIDER_HEIGHT,
                        color = Color.LightGray
                    )
                    NavigationBar(
                        modifier = Modifier.height(BOTTOM_BAR_HEIGHT)
                    ) {
                        BottomNavigationItems.entries.forEach { item ->
                            val route = item.direction.route
                            val rootBottomNavItemRoute = navController
                                .currentBackStack
                                .value
                                .findLast { entry ->
                                    BottomNavigationItems.entries
                                        .map { it.direction.route }
                                        .contains(entry.destination.route)
                                }
                                ?.destination
                                ?.route
                            val isSelected = rootBottomNavItemRoute == route

                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        navController.popBackStack(route, false)
                                    } else {
                                        navController.navToBottomNavItem(
                                            route,
                                            popUpTo = DashboardScreenDestination
                                        )
                                    }
                                },
                            )
                        }
                    }
                }
            }
        },
        content = { content() }
    )
}

private fun shouldShowBottomBarByDestination(currentDestinationRoute: String?): Boolean =
    VisibleBottomBarDestination.entries.map { it.route }.contains(currentDestinationRoute)

private fun getBottomBarOffset(isBottomBarVisible: Boolean) = when (isBottomBarVisible) {
    true -> 0.dp
    else -> BOTTOM_BAR_HEIGHT + DIVIDER_HEIGHT
}

val BOTTOM_BAR_HEIGHT = 82.dp
val DIVIDER_HEIGHT = 1.dp


fun NavController.navToBottomNavItem(
    route: String,
    stackToClearRoute: Route? = null,
    popUpTo: DestinationSpec<*>,
) {
    stackToClearRoute?.let {
        if (!clearBackStack(it)) {
            popBackStack(it, inclusive = true, saveState = false)
        }
    }

    navigate(route) {
        popUpTo(popUpTo) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun Modifier.bottomBarPadding(navController: NavController): Modifier {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = shouldShowBottomBarByDestination(
        currentDestinationRoute = navBackStackEntry?.destination?.route
    )

    return if (showBottomBar) {
        padding(bottom = getBottomBarOffset(isBottomBarVisible = false))
    } else {
        this
    }
}