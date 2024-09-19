package com.example.hopshop.presentation.auth.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hopshop.R
import com.example.hopshop.presentation.auth.signIn.BackgroundShapes
import com.example.hopshop.presentation.auth.signIn.Button
import com.example.hopshop.presentation.destinations.SignInScreenDestination
import com.example.hopshop.presentation.list.VerticalSpacer
import com.example.hopshop.presentation.main.bottomBarPadding
import com.example.hopshop.ui.theme.AppTheme
import com.example.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun BaseAuthScreen(
    navigator: DestinationsNavigator,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
    ) {
        BackgroundShapes()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_online_shopping),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )

                VerticalSpacer(54.dp)

                Text(
                    text = "Explore the app",
                    style = Typography.h4,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.black90,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(264.dp)
                )

                VerticalSpacer(12.dp)

                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi maecenas quis interdum enim enim molestie faucibus. Pretium non non massa eros, nunc, urna.",
                    style = Typography.label,
                    color = AppTheme.colors.grey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(264.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                text = "Rozpocznij zakupy!",
                onClick = {
                    navigator.navigate(SignInScreenDestination)
                },
            )
        }
    }
}