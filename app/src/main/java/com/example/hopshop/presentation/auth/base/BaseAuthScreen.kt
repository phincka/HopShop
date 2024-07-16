package com.example.hopshop.presentation.auth.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hopshop.R
import com.example.hopshop.presentation.destinations.SignInScreenDestination
import com.example.hopshop.presentation.list.VerticalSpacer
import com.example.hopshop.presentation.main.bottomBarPadding
import com.example.hopshop.ui.theme.HopShopAppTheme
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
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier.size(445.dp).offset(y = (-210).dp, x = (-37).dp).background(
                color = HopShopAppTheme.colors.purple.copy(alpha = 0.2F),
                shape = CircleShape
            )
        )

        Box(
            modifier = Modifier.size(342.dp).offset(y = (-175).dp, x = 207.dp).background(
                color = HopShopAppTheme.colors.purple.copy(alpha = 0.2F),
                shape = CircleShape
            )
        )

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
                    contentDescription = "Opis obrazu",
                    modifier = Modifier.fillMaxWidth()
                )

                VerticalSpacer(54.dp)

                Text(
                    text = "Explore the app",
                    style = Typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = HopShopAppTheme.colors.black90,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(264.dp)
                )

                VerticalSpacer(12.dp)

                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi maecenas quis interdum enim enim molestie faucibus. Pretium non non massa eros, nunc, urna.",
                    style = Typography.bodySmall,
                    color = HopShopAppTheme.colors.grey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(264.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    navigator.navigate(SignInScreenDestination)
                },
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "Rozpocznij zakupy!",
                    style = Typography.bodyMedium,
                )
            }

//            OutlinedButton(
//                text = stringResource(R.string.signIn_register_button),
//                modifier = Modifier.fillMaxWidth(),
//                onClick = {
////                navigator.navigate(SignUpScreenDestination)
//                },
//            )
//
//            FilledButton(
//                text = stringResource(R.string.signIn_login_button),
//                modifier = Modifier.fillMaxWidth(),
//                onClick = {
//                    navigator.navigate(SignInScreenDestination)
//                },
//            )
        }
    }
}

//@Preview
//@Composable
//fun Preview() {
//    PszzAppTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            BaseAuthScreen(
//                navigator = DestinationsNavigator
//            )
//        }
//    }
//}