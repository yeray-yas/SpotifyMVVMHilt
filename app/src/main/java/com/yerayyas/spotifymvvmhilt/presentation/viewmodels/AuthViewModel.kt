package com.yerayyas.spotifymvvmhilt.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _startDestination = MutableStateFlow("initial")
    val startDestination: StateFlow<String> = _startDestination

    private val auth: FirebaseAuth = Firebase.auth

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        _startDestination.value = if (auth.currentUser != null) "home" else "initial"
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }
}
