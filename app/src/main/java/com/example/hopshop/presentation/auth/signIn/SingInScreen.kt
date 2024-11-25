package pl.hincka.hopshop.presentation.auth.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.hincka.hopshop.R
import pl.hincka.hopshop.components.buttons.Button
import pl.hincka.hopshop.components.design.VerticalSpacer
import pl.hincka.hopshop.components.form.InputText
import pl.hincka.hopshop.data.util.AuthState
import pl.hincka.hopshop.presentation.components.LoadingDialog
import pl.hincka.hopshop.presentation.components.PasswordField
import pl.hincka.hopshop.presentation.destinations.DashboardScreenDestination
import pl.hincka.hopshop.presentation.destinations.SignUpScreenDestination
import pl.hincka.hopshop.presentation.main.SnackbarHandler
import pl.hincka.hopshop.presentation.main.bottomBarPadding
import pl.hincka.hopshop.ui.theme.AppTheme
import pl.hincka.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SignInScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: SignInViewModel = koinViewModel(),
    snackbarHandler: SnackbarHandler,
) {
    val signInState = viewModel.signInState.collectAsState().value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (signInState is AuthState.Loading) LoadingDialog()
    if (signInState is AuthState.Success) navigator.navigate(DashboardScreenDestination())

    LaunchedEffect(signInState) {
        launch {
            if (signInState is AuthState.Error) snackbarHandler.showErrorSnackbar(message = signInState.message)
        }
    }

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
        ) {
            VerticalSpacer(40.dp)

            Text(
                text = "Witaj,",
                style = Typography.h3,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.neutral90,
            )

            Text(
                text = "Miło znów Cię widzieć!",
                style = Typography.h5,
                color = AppTheme.colors.neutral90,
            )

            VerticalSpacer(20.dp)

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                InputText(
                    label = stringResource(R.string.signIn_form_login),
                    placeholder = stringResource(R.string.signIn_form_login),
                    value = email,
                    onValueChange = { email = it },
                )

                PasswordField(
                    label = stringResource(R.string.signIn_form_password),
                    value = password,
                    setValue = { newValue ->
                        password = newValue
                    },
                )
            }

            VerticalSpacer(20.dp)

            Button(
                text = stringResource(R.string.signIn_login_button),
                onClick = { viewModel.signIn(email, password) },
            )

            VerticalSpacer(32.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.signIn_register_button),
                    textAlign = TextAlign.Start,
                    style = Typography.small,
                    color = AppTheme.colors.neutral90,
                    modifier = Modifier.clickable(
                        onClick = { navigator.navigate(SignUpScreenDestination) }
                    )
                )

//                Text(
//                    text = stringResource(R.string.signIn_reset_password_text),
//                    textAlign = TextAlign.End,
//                    style = Typography.small,
//                    color = AppTheme.colors.neutral90,
//                )
            }
        }

        if (signInState is AuthState.Success) navigator.navigate(DashboardScreenDestination())
        if (signInState is AuthState.Loading) LoadingDialog()
    }
}

@Composable
fun BackgroundShapes() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
                .align(Alignment.TopStart)
                .offset {
                    IntOffset(
                        y = -100,
                        x = 0,
                    )
                },
        ) {
            Image(
                painter = painterResource(R.drawable.top_bg),
                contentScale = ContentScale.FillBounds,
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(391.dp)
                .align(Alignment.BottomEnd),
        ) {
            Image(
                painter = painterResource(R.drawable.bottom_bg),
                contentScale = ContentScale.FillBounds,
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}