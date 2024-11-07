package com.yerayyas.spotifymvvmhilt.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun login(auth: FirebaseAuth, navigateToHome: () -> Unit) {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            viewModelScope.launch {
                _errorMessage.emit("Please fill in both email and password")
            }
        } else {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            if (!_email.value.matches(Regex(emailPattern))) {
                viewModelScope.launch {
                    _errorMessage.emit("Please enter a valid email address")
                }
            } else {
                auth.signInWithEmailAndPassword(_email.value, _password.value).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModelScope.launch {
                            _isLoginSuccessful.emit(true)
                        }
                        navigateToHome()
                    } else {
                        viewModelScope.launch {
                            _errorMessage.emit("Login failed: Incorrect credentials")
                        }
                    }
                }
            }
        }
    }
}
