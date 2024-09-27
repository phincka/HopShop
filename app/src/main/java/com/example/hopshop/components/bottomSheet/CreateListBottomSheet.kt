package com.example.hopshop.components.bottomSheet

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
import com.example.hopshop.R
import com.example.hopshop.components.buttons.Button
import com.example.hopshop.components.design.VerticalSpacer
import com.example.hopshop.components.form.InputText
import com.example.hopshop.data.model.FormListModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.presentation.components.LoadingDialog
import com.example.hopshop.presentation.dashboard.CreateListState
import com.example.hopshop.ui.theme.AppTheme
import com.example.hopshop.ui.theme.Typography

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CreateListBottomSheet(
    setVisible: (Boolean) -> Unit,
    createList: (FormListModel) -> Unit,
    createListState: CreateListState,
    listModel: ListModel? = null,
) {
    if (createListState is CreateListState.Loading) LoadingDialog()

    var formListModel by remember { mutableStateOf(FormListModel()) }

    listModel?.let {
        formListModel = formListModel.copy(
            id = it.id,
            name = it.name,
            description = it.description,
            tag = it.tag,
            sharedIds = it.sharedIds,
        )
    }

    Text(
        text = stringResource(R.string.create_list),
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
    )

    InputText(
        placeholder = stringResource(R.string.category),
        value = formListModel.tag,
        onValueChange = {
            formListModel = formListModel.copy(tag = it)
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

    InputText(
        placeholder = stringResource(R.string.form_description_label),
        value = formListModel.description,
        onValueChange = {
            formListModel = formListModel.copy(description = it)
        },
        onDone = {
            setVisible(false)
            createList(formListModel)
        },
    )

    VerticalSpacer(24.dp)

    Button(
        text = stringResource(R.string.form_create_list_button),
        onClick = {
            createList(formListModel)
            setVisible(false)
        },
    )
}