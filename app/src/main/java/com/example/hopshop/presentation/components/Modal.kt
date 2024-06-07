package com.example.hopshop.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.hopshop.presentation.components.FilledButton
import com.example.hopshop.R

@Composable
fun Modal(
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    isModalActive: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit = { _ -> },
) {
    if (!isModalActive) return
    
    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = null
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            FilledButton(
                text = stringResource(R.string.remove_modal_remove),
                onClick = { onConfirmation("") }
            )
        },
        dismissButton = {
            FilledButton(
                text = stringResource(R.string.remove_modal_cancel),
                onClick = { onDismissRequest() }
            )
        }
    )
}