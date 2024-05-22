package com.example.hopshop.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.hopshop.data.model.UserModel
import com.example.hopshop.data.util.DropdownMenuItemData
import com.example.hopshop.presentation.destinations.AccountScreenDestination
import com.example.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    backNavigation: (() -> Unit)? = null,
    title: String,
    menuItems: List<DropdownMenuItemData> = emptyList(),
    isModalActive: Boolean = false,
    setModal: (Boolean) -> Unit = {},
    navigator: DestinationsNavigator,
    user: UserModel,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var isDropdownMenuVisible by remember { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = "https://www.bmw-smorawinski.pl/www/media/25/team/kinga_kasprzak.jpg")
            .apply(block = fun ImageRequest.Builder.() {
                transformations(CircleCropTransformation())
            }).build()
    )

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color("#F9F5FF".toColorInt()))
                ) {
                    Text(
                        text = user.email.first().toString().uppercase(),
                        color = Color("#7F56D9".toColorInt()),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = "Jan Kowalski",
                        style = Typography.bodyMedium,
                        color = Color("#344054".toColorInt())
                    )
                    Text(
                        text = user.email,
                        style = Typography.bodySmall,
                        color = Color("#667085".toColorInt())
                    )
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
                    Box(
                        modifier = Modifier
                            .background(Color("#F9F5FF".toColorInt()), shape = CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Localized description",
                            tint = Color("#7F56D9".toColorInt()),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { navigator.navigate(AccountScreenDestination) },
                ) {

                    Box(
                        modifier = Modifier
                            .background(Color("#F9FAFB".toColorInt()), shape = CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Localized description",
                            tint = Color("#667085".toColorInt()),
                        )
                    }
                }
            }
        },
    )
}