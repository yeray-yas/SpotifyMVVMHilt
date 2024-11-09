package com.yerayyas.spotifymvvmhilt.presentation.screens.signup

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.yerayyas.spotifymvvmhilt.R
import com.yerayyas.spotifymvvmhilt.presentation.viewmodels.SignUpState
import com.yerayyas.spotifymvvmhilt.presentation.viewmodels.SignUpViewModel
import com.yerayyas.spotifymvvmhilt.ui.theme.Black

@Composable
fun SignUpScreen(navHostController: NavHostController, signUpViewModel: SignUpViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val signUpState = signUpViewModel.uiState.value

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
                        navHostController.popBackStack("initial", false)
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
            onValueChange = { email = it },
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
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(48.dp))

        if (signUpState is SignUpState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color.White
            )
        } else {
            Button(onClick = {
                signUpViewModel.signUp(email, password)
            }) {
                Text("Sign up")
            }
        }

        if (signUpState is SignUpState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = signUpState.message,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }

        if (signUpState is SignUpState.Success) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                navHostController.navigate("home") {
                    popUpTo("signUp") { inclusive = true }
                }
            }
        }
    }
}






