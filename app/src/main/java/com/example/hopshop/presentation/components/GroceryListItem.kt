package com.example.hopshop.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.presentation.destinations.ListScreenDestination
import com.example.hopshop.ui.theme.HopShopAppTheme
import com.example.hopshop.ui.theme.HopShopColors
import com.example.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun GroceryListItem(
    navigator: DestinationsNavigator,
    list: ListModel
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = HopShopAppTheme.colors.grey20,
        ),
        border = BorderStroke(1.dp, HopShopAppTheme.colors.grey50),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                onClick = { navigator.navigate(ListScreenDestination(list.id)) }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = list.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                    ),
                    color = HopShopAppTheme.colors.black,
                    textAlign = TextAlign.Center,
                )

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

            if (list.isShared) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.FolderShared,
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Lista użytkownika Paweł",
                        style = Typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = HopShopAppTheme.colors.grey,
                    )
                }
            }
        }
    }
}