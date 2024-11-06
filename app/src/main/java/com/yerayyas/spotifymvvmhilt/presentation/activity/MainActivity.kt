package com.yerayyas.spotifymvvmhilt.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yerayyas.spotifymvvmhilt.presentation.navigation.NavigationWrapper
import com.yerayyas.spotifymvvmhilt.ui.theme.SpotifyMVVMHiltTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        setContent {
            navHostController = rememberNavController()

            val currentUser = auth.currentUser
            val startDestination = if (currentUser != null) {
                "home"
            } else {
                "initial"
            }

            SpotifyMVVMHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationWrapper(
                        navHostController,
                        auth,
                        modifier = Modifier.padding(innerPadding),
                        context = applicationContext,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}



