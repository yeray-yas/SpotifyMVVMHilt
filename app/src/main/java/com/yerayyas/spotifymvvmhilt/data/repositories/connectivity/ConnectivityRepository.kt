package com.yerayyas.spotifymvvmhilt.data.repositories.connectivity

import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectionStatus
import kotlinx.coroutines.flow.StateFlow

interface ConnectivityRepository {
    fun getConnectionStatus(): StateFlow<ConnectionStatus>
    fun startListening()
    fun stopListening()
}


