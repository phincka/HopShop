package com.example.hopshop.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.ui.theme.AppTheme
import com.example.hopshop.ui.theme.Typography

@Composable
fun GroceryListItem(
    list: ListModel,
    navigateToGroceryList: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 4.dp.toPx()
                shape = RoundedCornerShape(8.dp)
                clip = true
            }
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = { navigateToGroceryList(list.id) }
            )
            .background(AppTheme.colors.white)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = list.name,
                style = Typography.p,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.black,
                textAlign = TextAlign.Center,
            )

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
                    style = Typography.small,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.grey,
                )
            }
        }
    }
}