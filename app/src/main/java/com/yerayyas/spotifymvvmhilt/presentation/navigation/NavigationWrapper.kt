package com.yerayyas.spotifymvvmhilt.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.yerayyas.spotifymvvmhilt.presentation.connectivity.ConnectivityViewModel
import com.yerayyas.spotifymvvmhilt.presentation.screens.home.HomeScreen
import com.yerayyas.spotifymvvmhilt.presentation.screens.initial.InitialScreen
import com.yerayyas.spotifymvvmhilt.presentation.screens.login.LogInScreen
import com.yerayyas.spotifymvvmhilt.presentation.screens.signup.SignUpScreen

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    auth: FirebaseAuth,
    modifier: Modifier
) {
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    NavHost(navController = navHostController, startDestination = "home") {
        composable("initial") {
            InitialScreen(
                navigateToLogin = { navHostController.navigate("logIn") },
                navigateToSignUp = { navHostController.navigate("signUp") }
            )
        }
        composable("logIn") {
            LogInScreen(
                auth,
                navigateToHome = { navHostController.navigate("home") },
                onBack = { navHostController.popBackStack() }
            )
        }
        composable("signUp") {
            SignUpScreen(auth)
        }
        composable("home") {
            HomeScreen()
        }
    }
}
