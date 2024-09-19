package com.example.hopshop.presentation.auth.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hopshop.R
import com.example.hopshop.data.util.AuthState
import com.example.hopshop.presentation.components.LoadingDialog
import com.example.hopshop.presentation.components.PasswordField
import com.example.hopshop.presentation.components.TextError
import com.example.hopshop.presentation.dashboard.ShareListState
import com.example.hopshop.presentation.destinations.DashboardScreenDestination
import com.example.hopshop.presentation.destinations.SignUpScreenDestination
import com.example.hopshop.presentation.list.VerticalSpacer
import com.example.hopshop.presentation.main.SnackbarHandler
import com.example.hopshop.presentation.main.bottomBarPadding
import com.example.hopshop.ui.theme.AppTheme
import com.example.hopshop.ui.theme.Typography
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
    if (signInState is AuthState.Success) navigator.navigate(DashboardScreenDestination)

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

        if (signInState is AuthState.Success) navigator.navigate(DashboardScreenDestination)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    label: String? = null,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1,
    maxLines: Int = 1,
) {
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = AppTheme.colors.purple50,
                unfocusedBorderColor = AppTheme.colors.neutral30,
                focusedPlaceholderColor = AppTheme.colors.neutral30,
                unfocusedPlaceholderColor = AppTheme.colors.neutral30,
            ),
            textStyle = Typography.label.copy(
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.neutral90,
            ),
            maxLines = maxLines,
            minLines = minLines,
            keyboardActions = KeyboardActions(
                onDone = {}
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )
    }
}

@Composable
fun Button(
    onClick: () -> Unit = {},
    text: String,
    isLoading: Boolean = false,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        onClick = onClick,
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        if (!isLoading) {
            Text(
                text = text,
                style = Typography.p,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = AppTheme.colors.white,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}