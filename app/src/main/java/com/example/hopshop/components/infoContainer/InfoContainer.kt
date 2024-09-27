package com.example.hopshop.components.infoContainer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopshop.R
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
            .padding(top = 92.dp)
            .fillMaxWidth()
    ) {
        if (showIcon) {
            Image(
                painter = painterResource(R.drawable.empty_data),
                contentScale = ContentScale.Fit,
                contentDescription = "",
                modifier = Modifier.width(300.dp)
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