package com.yerayyas.spotifymvvmhilt.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.yerayyas.spotifymvvmhilt.R
import com.yerayyas.spotifymvvmhilt.presentation.viewmodels.LogInViewModel
import com.yerayyas.spotifymvvmhilt.ui.theme.Black
import com.yerayyas.spotifymvvmhilt.utils.showToast

@Composable
fun LogInScreen(
    auth: FirebaseAuth,
    navigateToHome: () -> Unit,
    onBack: () -> Unit,
    logInViewModel: LogInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val email by logInViewModel.email.collectAsState()
    val password by logInViewModel.password.collectAsState()
    val isLoginSuccessful by logInViewModel.isLoginSuccessful.collectAsState()
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(logInViewModel.errorMessage) {
        logInViewModel.errorMessage.collect { message ->
            errorMessage.value = message
        }
    }

    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) {
            showToast(context, "Login successful")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.ic_back_24),
                contentDescription = "Back Arrow",
                tint = Color.White,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .size(24.dp)
                    .clickable {
                        onBack()
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Text(
            text = "Email",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
        TextField(
            value = email,
            onValueChange = { logInViewModel.onEmailChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Password",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
        TextField(
            value = password,
            onValueChange = { logInViewModel.onPasswordChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier
            .height(48.dp)
        )
        Button(onClick = {
            logInViewModel.login(auth, navigateToHome)
        }) {
            Text("Log in")
        }

        errorMessage.value?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

