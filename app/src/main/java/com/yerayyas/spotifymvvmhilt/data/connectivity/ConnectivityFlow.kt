package com.yerayyas.spotifymvvmhilt.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.provider.Settings
import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectionStatus
import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConnectivityFlow(private val connectivityManager: ConnectivityManager, private val context: Context) {

    private val _connectionStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Disconnected)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus

    init {
        startPeriodicConnectionCheck()
    }

    private fun startPeriodicConnectionCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(2000) // Every 2 seconds
                checkInitialConnectionStatus()
            }
        }
    }


    private val networkCallbackInstance =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (!isAirplaneModeOn()) {
                    // Actualizar de inmediato el tipo de conexión cuando hay un cambio
                    val connectionType = getConnectionType()
                    _connectionStatus.value = if (connectionType == ConnectionType.UNKNOWN) {
                        ConnectionStatus.Disconnected // Sin conexión reconocida
                    } else {
                        ConnectionStatus.Connected(connectionType)
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                if (isAirplaneModeOn()) {
                    _connectionStatus.value = ConnectionStatus.AirplaneMode
                } else {
                    _connectionStatus.value = ConnectionStatus.Disconnected
                }
            }
        }

    fun startListening() {
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), networkCallbackInstance)
        checkInitialConnectionStatus()
    }

    fun stopListening() {
        connectivityManager.unregisterNetworkCallback(networkCallbackInstance)
    }

    private fun checkInitialConnectionStatus() {
        if (isAirplaneModeOn()) {
            _connectionStatus.value = ConnectionStatus.AirplaneMode
        } else {
            val connectionType = getConnectionType()
            _connectionStatus.value = if (connectionType == ConnectionType.UNKNOWN) {
                ConnectionStatus.Disconnected
            } else {
                ConnectionStatus.Connected(connectionType)
            }
        }
    }

    private fun getConnectionType(): ConnectionType {
        val activeNetwork = connectivityManager.activeNetwork ?: return ConnectionType.UNKNOWN
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return ConnectionType.UNKNOWN

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.MOBILE
            else -> ConnectionType.UNKNOWN
        }
    }

    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
    }
}
