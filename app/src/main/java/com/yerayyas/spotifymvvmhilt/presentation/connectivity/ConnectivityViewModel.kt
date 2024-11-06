package com.yerayyas.spotifymvvmhilt.presentation.connectivity

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectionStatus
import com.yerayyas.spotifymvvmhilt.domain.usecases.GetConnectionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase
) : ViewModel(), LifecycleObserver {

    private val connectionStatus: StateFlow<ConnectionStatus> = getConnectionStatusUseCase.execute()

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    init {
        viewModelScope.launch {
            connectionStatus.collect { status ->
                handleConnectionStatus(status)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startListening() {
        getConnectionStatusUseCase.repository.startListening()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopListening() {
        getConnectionStatusUseCase.repository.stopListening()
    }

    private fun handleConnectionStatus(status: ConnectionStatus) {
        val message = when (status) {
            is ConnectionStatus.Connected -> "Connected with ${status.connectionType}"
            ConnectionStatus.Disconnected -> "No Network"
            ConnectionStatus.AirplaneMode -> "Airplane Mode Enabled"
        }
        _toastMessage.value = message
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
