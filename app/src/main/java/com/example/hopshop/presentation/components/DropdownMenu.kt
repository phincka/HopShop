package pl.hincka.hopshop.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.hincka.hopshop.data.util.DropdownMenuItemData

@Composable
fun Dropdown(
    isDropdownMenuVisible: Boolean,
    setDropdownMenuVisible: (Boolean) -> Unit,
    menuItems: List<DropdownMenuItemData>
) {
    if (!isDropdownMenuVisible) return
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .offset(y = (-24).dp)
            .background(Color.LightGray)
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { setDropdownMenuVisible(false) },
            modifier = Modifier.width(240.dp)
        ) {
            menuItems.forEachIndexed { index, item ->
                if (index == menuItems.size - 1 && menuItems.size > 1) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(6.dp))
                }

                DropdownMenuItem(
                    text = {
                        Text(item.text)
                    },
                    onClick = {
                        item.onClick()
                    },
                    leadingIcon = {
                        Icon(
                            item.icon,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}