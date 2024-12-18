package pl.hincka.hopshop.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.hincka.hopshop.ui.theme.Typography

@Composable
fun TextError(
    text: String
) {
    Text(
        text = text,
        color = Color.Red,
        style = Typography.label,
        modifier = Modifier.padding(top = 16.dp),
    )
}