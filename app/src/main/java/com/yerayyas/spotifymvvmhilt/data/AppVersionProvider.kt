package com.yerayyas.spotifymvvmhilt.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigClientException
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectionStatus
import com.yerayyas.spotifymvvmhilt.data.repositories.connectivity.ConnectivityRepository
import com.yerayyas.spotifymvvmhilt.utils.Constants.Companion.MIN_VERSION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppVersionProvider @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
    private val connectivityRepository: ConnectivityRepository
) {

    init {
        setupRemoteConfig()
    }

    fun setupRemoteConfig() {
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600L
        })
        fetchRemoteConfigIfConnected()
    }

    private fun fetchRemoteConfigIfConnected() {
        val connectionStatus = connectivityRepository.getConnectionStatus().value
        if (connectionStatus is ConnectionStatus.Connected) {
            remoteConfig.fetchAndActivate()
        }
    }

    suspend fun getMinAllowedVersion(): List<Int> {
        return try {
            if (connectivityRepository.getConnectionStatus().value is ConnectionStatus.Connected) {
                remoteConfig.fetchAndActivateSuspend()
                remoteConfig.getString(MIN_VERSION).takeIf { it.isNotBlank() }
                    ?.split(".")
                    ?.map { it.toInt() }
                    ?: listOf(0, 0, 0)
            } else {
                listOf(0, 0, 0)
            }
        } catch (e: FirebaseRemoteConfigClientException) {
            listOf(0, 0, 0)
        }
    }

    private suspend fun FirebaseRemoteConfig.fetchAndActivateSuspend(): Boolean =
        fetch(0).await().let { activate().await() }
}
