package com.yerayyas.spotifymvvmhilt.data.repositories.connectivity

import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectionStatus
import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectivityFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ConnectivityRepositoryImpl @Inject constructor(
    private val connectivityFlow: ConnectivityFlow
) : ConnectivityRepository {

    override fun getConnectionStatus(): StateFlow<ConnectionStatus> = connectivityFlow.connectionStatus
    override fun startListening() = connectivityFlow.startListening()
    override fun stopListening() = connectivityFlow.stopListening()
}

