package com.example.hopshop.presentation.auth.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hopshop.presentation.components.LoadingDialog
import com.example.hopshop.presentation.components.PasswordField
import com.example.hopshop.presentation.components.TextError
import com.example.hopshop.R
import com.example.hopshop.data.util.AuthState
import com.example.hopshop.presentation.auth.signUp.SignUpViewModel
import com.example.hopshop.presentation.components.FilledButton
import com.example.hopshop.presentation.components.OutlinedButton
import com.example.hopshop.presentation.destinations.DashboardScreenDestination
import com.example.hopshop.presentation.destinations.SignInScreenDestination
import com.example.hopshop.presentation.main.bottomBarPadding
import com.example.hopshop.ui.theme.HopShopAppTheme
import com.example.hopshop.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val signInState = viewModel.signInState.collectAsState().value

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    if (signInState is AuthState.Loading) LoadingDialog(stringResource(R.string.signIn_title))
    if (signInState is AuthState.Success) navigator.navigate(DashboardScreenDestination)

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(445.dp)
                .offset(y = (-210).dp, x = (-37).dp)
                .background(
                    color = HopShopAppTheme.colors.purple.copy(alpha = 0.2F),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(342.dp)
                .offset(y = (-175).dp, x = 207.dp)
                .background(
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
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = HopShopAppTheme.colors.black90)) {
                        append("Hop")
                    }
                    withStyle(style = SpanStyle(color = HopShopAppTheme.colors.purple)) {
                        append("Shop")
                    }
                },
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(stringResource(R.string.signIn_form_name))
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = HopShopAppTheme.colors.purple,
                        unfocusedBorderColor = HopShopAppTheme.colors.gray30,
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = HopShopAppTheme.colors.grey,
                    ),
                    maxLines = 1,
                    keyboardActions = KeyboardActions(
                        onDone = {}
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(stringResource(R.string.signIn_form_login))
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = HopShopAppTheme.colors.purple,
                        unfocusedBorderColor = HopShopAppTheme.colors.gray30,
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = HopShopAppTheme.colors.grey,
                    ),
                    maxLines = 1,
                    keyboardActions = KeyboardActions(
                        onDone = {}
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                PasswordField(
                    label = stringResource(R.string.signIn_form_password),
                    value = password,
                    setValue = { newValue ->
                        password = newValue
                    },
                )

                PasswordField(
                    label = stringResource(R.string.signIn_form_repeat_password),
                    value = repeatPassword,
                    setValue = { newValue ->
                        repeatPassword = newValue
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FilledButton(
                text = stringResource(R.string.signIn_register_button),
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.signUp(name, email, password, repeatPassword) },
            )

            if (signInState is AuthState.Error) {
                val errorMessage = (signInState as AuthState.Error).error
                TextError(errorMessage)
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(stringResource(R.string.signUp_signIn_text))
                Text(
                    text = stringResource(R.string.signUp_signIn_button),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navigator.navigate(SignInScreenDestination)
                    }
                )
            }
        }
    }
}