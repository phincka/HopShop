package pl.hincka.hopshop.components.modalDialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import pl.hincka.hopshop.components.buttons.Button
import pl.hincka.hopshop.components.buttons.SmallButton

@Composable
fun ModalDialog(
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    isModalActive: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit = { _ -> },
    confirmButtonText: String,
    dismissButtonText: String,
) {
    if (!isModalActive) return

    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = dialogTitle,
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(
                text = dialogText,
                textAlign = TextAlign.Center,
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            SmallButton(
                text = confirmButtonText,
                onClick = { onConfirmation("") },
            )
        },
        dismissButton = {
            SmallButton(
                text = dismissButtonText,
                onClick = { onDismissRequest() },
            )
        }
    )
}