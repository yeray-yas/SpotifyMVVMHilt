package com.yerayyas.spotifymvvmhilt.presentation.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.yerayyas.spotifymvvmhilt.presentation.viewmodels.ConnectivityViewModel

@Composable
fun ShowToast(context: Context, connectivityViewModel: ConnectivityViewModel) {
    val toastMessage by connectivityViewModel.toastMessage.observeAsState()

    toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        connectivityViewModel.clearToastMessage()
    }
}