package com.example.hopshop.components.modalDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.hopshop.components.buttons.Button
import com.example.hopshop.components.buttons.SmallButton
import com.example.hopshop.ui.theme.AppTheme

@Composable
fun FormModalDialog(
    isVisible: Boolean,
    setVisible: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    if (!isVisible) return

    Dialog(
        onDismissRequest = {
            setVisible(false)
        },
    ) {
        Box(
            modifier = Modifier.background(
                color = AppTheme.colors.white,
                shape = RoundedCornerShape(32.dp)
            )
                .padding(horizontal = 16.dp, vertical = 32.dp),
        ) {
            Column {
                content()
            }
        }
    }
}