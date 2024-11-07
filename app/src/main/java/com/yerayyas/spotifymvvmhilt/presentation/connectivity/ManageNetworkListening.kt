package com.yerayyas.spotifymvvmhilt.presentation.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yerayyas.spotifymvvmhilt.presentation.viewmodels.ConnectivityViewModel

@Composable
fun ManageNetworkListening(connectivityViewModel: ConnectivityViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val lifecycleObserver = rememberUpdatedState { event: Lifecycle.Event ->
        when (event) {
            Lifecycle.Event.ON_START -> connectivityViewModel.startListening()
            Lifecycle.Event.ON_STOP -> connectivityViewModel.stopListening()
            else -> {}
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> lifecycleObserver.value(event) }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}