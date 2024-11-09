package com.yerayyas.spotifymvvmhilt.data.connectivity

sealed class ConnectionStatus {
    data object Disconnected : ConnectionStatus()
    data object AirplaneMode : ConnectionStatus()
    data class Connected(val connectionType: ConnectionType) : ConnectionStatus()
}

sealed class ConnectionType {
    data object WIFI : ConnectionType()
    data object MOBILE : ConnectionType()
    data object DISCONNECTED : ConnectionType()
}


