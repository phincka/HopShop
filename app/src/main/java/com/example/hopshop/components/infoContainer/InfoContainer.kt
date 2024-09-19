package com.example.hopshop.components.infoContainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopshop.ui.theme.AppTheme
import com.example.hopshop.ui.theme.Typography

@Composable
fun InfoContainer(
    title: String,
    text: String,
    showIcon: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 105.dp)
            .fillMaxWidth()
    ) {
        if (showIcon) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Label,
                contentDescription = "Localized description",
                tint = AppTheme.colors.purple,
                modifier = Modifier.size(56.dp)
            )
        }

        Text(
            text = title,
            style = Typography.h5,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.black90,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(264.dp)
        )

        Text(
            text = text,
            style = Typography.label,
            color = AppTheme.colors.grey,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(264.dp)
        )

    }
}