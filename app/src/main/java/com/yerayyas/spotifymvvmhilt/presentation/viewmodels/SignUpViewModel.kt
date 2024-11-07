package com.yerayyas.spotifymvvmhilt.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = mutableStateOf<SignUpState>(SignUpState.Initial)
    val uiState: State<SignUpState> = _uiState

    fun signUp(email: String, password: String) {
        _uiState.value = SignUpState.Loading

        when {
            email.isBlank() || password.isBlank() -> {
                _uiState.value = SignUpState.Error("Please fill in both email and password")
            }
            !email.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) -> {
                _uiState.value = SignUpState.Error("Please enter a valid email address")
            }
            password.length < 8 -> {
                _uiState.value = SignUpState.Error("Password must be at least 8 characters")
            }
            !password.matches(Regex(".*[A-Z].*")) -> {
                _uiState.value = SignUpState.Error("Password must contain at least one uppercase letter")
            }
            !password.matches(Regex(".*\\d.*")) -> {
                _uiState.value = SignUpState.Error("Password must contain at least one number")
            }
            !password.matches(Regex(".*[!@#$%^&*()_,.?\":{}|<>].*")) -> {
                _uiState.value = SignUpState.Error("Password must contain at least one special character")
            }
            else -> {
                try {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _uiState.value = SignUpState.Success
                        } else {
                            _uiState.value = SignUpState.Error("Registration failed: ${task.exception?.message}")
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = SignUpState.Error("An unexpected error occurred: ${e.message}")
                }
            }
        }
    }

}

sealed class SignUpState {
    data object Initial : SignUpState()
    data object Loading : SignUpState()
    data object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}

