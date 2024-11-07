package com.yerayyas.spotifymvvmhilt.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yerayyas.spotifymvvmhilt.presentation.navigation.NavigationWrapper
import com.yerayyas.spotifymvvmhilt.presentation.viewmodels.AuthViewModel
import com.yerayyas.spotifymvvmhilt.ui.theme.SpotifyMVVMHiltTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            val startDestination by authViewModel.startDestination.collectAsState()

                SpotifyMVVMHiltTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        NavigationWrapper(
                            navHostController = navHostController,
                            auth = authViewModel.getAuth(),
                            modifier = Modifier.padding(innerPadding),
                            context = this@MainActivity,
                            startDestination = startDestination
                        )
                    }
                }
        }
    }
}




