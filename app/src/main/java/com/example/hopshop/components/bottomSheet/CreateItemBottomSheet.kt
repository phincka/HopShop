package pl.hincka.hopshop.components.bottomSheet

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.hincka.hopshop.R
import pl.hincka.hopshop.components.buttons.Button
import pl.hincka.hopshop.components.design.VerticalSpacer
import pl.hincka.hopshop.components.form.InputText
import pl.hincka.hopshop.presentation.components.LoadingDialog
import pl.hincka.hopshop.presentation.list.CreateItemState
import pl.hincka.hopshop.ui.theme.AppTheme
import pl.hincka.hopshop.ui.theme.Typography

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CreateItemBottomSheet(
    setVisible: (Boolean) -> Unit,
    listId: String,
    createItemState: CreateItemState,
    createItem: (String, String) -> Unit,
) {
    if (createItemState is CreateItemState.Loading) LoadingDialog()

    var itemName by remember { mutableStateOf("") }
    var isNameError by remember { mutableStateOf(false) }

    Text(
        text = stringResource(R.string.modal_create_item_title),
        style = Typography.h5,
        fontWeight = FontWeight.Bold,
        color = AppTheme.colors.black,
    )
    VerticalSpacer(16.dp)

    InputText(
        placeholder = stringResource(R.string.item_name),
        value = itemName,
        onValueChange = {
            itemName = it
        },
        onDone = {
            createItem(itemName, listId)
            setVisible(false)
        },
        isError = isNameError,
    )

    VerticalSpacer(24.dp)

    Button(
        text = stringResource(R.string.add),
        onClick = {
            if (itemName.isEmpty()) {
                isNameError = true
            } else {
                isNameError = false
                createItem(itemName, listId)
                setVisible(false)
            }
        },
    )
}