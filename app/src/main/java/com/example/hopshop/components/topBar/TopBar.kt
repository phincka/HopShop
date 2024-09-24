package com.example.hopshop.components.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopshop.data.model.UserModel
import com.example.hopshop.ui.theme.AppTheme
import com.example.hopshop.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    user: UserModel,
    signOut: () -> Unit,
) {
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
                        .background(AppTheme.colors.purple50)
                ) {
                    Text(
                        text = user.email.first().toString().uppercase(),
                        color = AppTheme.colors.purple,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = user.name,
                        style = Typography.p,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.black
                    )
                    Text(
                        text = user.email,
                        style = Typography.small,
                        color = AppTheme.colors.grey,
                    )
                }
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(
                    onClick = { signOut() },
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                AppTheme.colors.grey5,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Localized description",
                            tint = AppTheme.colors.grey,
                        )
                    }
                }
            }
        },
    )
}