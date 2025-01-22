package pl.hincka.hopshop.components.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import pl.hincka.hopshop.R
import pl.hincka.hopshop.components.design.VerticalSpacer
import pl.hincka.hopshop.ui.theme.AppTheme
import pl.hincka.hopshop.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    label: String? = null,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1,
    maxLines: Int = 1,
    onDone: () -> Unit = {},
    isError: Boolean = false,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        label?.let {
            Text(
                text = label,
                style = Typography.label,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.neutral90,
            )
        }

        VerticalSpacer(6.dp)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = Typography.label,
                )
            },
            shape = RoundedCornerShape(10.dp),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedBorderColor = AppTheme.colors.purple50,
//                unfocusedBorderColor = AppTheme.colors.neutral30,
//                focusedPlaceholderColor = AppTheme.colors.neutral30,
//                unfocusedPlaceholderColor = AppTheme.colors.neutral30,
//                errorBorderColor = AppTheme.colors.red,
//            ),
            textStyle = Typography.label.copy(
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.neutral90,
            ),
            maxLines = maxLines,
            minLines = minLines,
            keyboardActions = KeyboardActions(
                onNext = {
                    onDone()
                    keyboardController?.hide()
                },
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            ),
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = stringResource(R.string.required),
                        style = Typography.small,
                        color = AppTheme.colors.red
                    )
                }
            }
        )
    }
}