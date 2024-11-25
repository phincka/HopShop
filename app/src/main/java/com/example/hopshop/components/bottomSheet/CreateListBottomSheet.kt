package pl.hincka.hopshop.components.bottomSheet

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.presentation.components.LoadingDialog
import pl.hincka.hopshop.presentation.dashboard.CreateListState
import pl.hincka.hopshop.ui.theme.AppTheme
import pl.hincka.hopshop.ui.theme.Typography

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CreateListBottomSheet(
    setVisible: (Boolean) -> Unit,
    executeFunction: (FormListModel) -> Unit,
    createListState: CreateListState,
    listModel: ListModel? = null,
) {
    if (createListState is CreateListState.Loading) LoadingDialog()

    var formListModel by remember { mutableStateOf(FormListModel()) }

    var isNameError by remember { mutableStateOf(false) }

    LaunchedEffect(listModel) {
        listModel?.let {
            formListModel = formListModel.copy(
                id = it.id,
                name = it.name,
                description = it.description,
                tag = it.tag,
                sharedIds = it.sharedIds,
            )
        }
    }

    Text(
        text = if (listModel != null) stringResource(R.string.edit_list) else stringResource(R.string.create_list),
        style = Typography.h5,
        fontWeight = FontWeight.Bold,
        color = AppTheme.colors.black,
    )

    VerticalSpacer(16.dp)

    InputText(
        placeholder = stringResource(R.string.list_name),
        value = formListModel.name,
        onValueChange = {
            formListModel = formListModel.copy(name = it)
        },
        isError = isNameError,
    )

    InputText(
        placeholder = stringResource(R.string.category),
        value = formListModel.tag,
        onValueChange = {
            formListModel = formListModel.copy(tag = it)
        },
        onDone = {
            if (formListModel.name.isEmpty()) {
                isNameError = true
            } else {
                isNameError = false
                executeFunction(formListModel)
                setVisible(false)
            }
        },
    )

    if (formListModel.sharedIds.isNotEmpty()) {
        InputText(
            placeholder = stringResource(R.string.share),
            value = formListModel.sharedIds[0],
            onValueChange = {
                formListModel = formListModel.copy(sharedIds = formListModel.sharedIds.plus(it))
            },
        )
    }

    VerticalSpacer(24.dp)

    Button(
        text = if (listModel != null) stringResource(R.string.form_edit_list_button) else stringResource(R.string.form_create_list_button),
        onClick = {
            if (formListModel.name.isEmpty()) {
                isNameError = true
            } else {
                isNameError = false
                executeFunction(formListModel)
                setVisible(false)
            }
        },
    )
}