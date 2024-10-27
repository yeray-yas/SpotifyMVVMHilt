package com.yerayyas.spotifymvvmhilt.domain.usecases

import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectionStatus
import com.yerayyas.spotifymvvmhilt.data.repositories.connectivity.ConnectivityRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetConnectionStatusUseCase @Inject constructor (val repository: ConnectivityRepository) {
    fun execute(): StateFlow<ConnectionStatus> = repository.getConnectionStatus()
}



