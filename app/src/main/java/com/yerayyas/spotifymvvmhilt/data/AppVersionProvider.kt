package com.yerayyas.spotifymvvmhilt.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yerayyas.spotifymvvmhilt.utils.Constants.Companion.MIN_VERSION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppVersionProvider @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {

    init {
        setupRemoteConfig()
    }

    fun setupRemoteConfig() {
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600L
        })
        remoteConfig.fetchAndActivate()
    }

    suspend fun getMinAllowedVersion(): List<Int> {
        return remoteConfig.fetchAndActivateSuspend().let {
            remoteConfig.getString(MIN_VERSION).takeIf { it.isNotBlank() }
                ?.split(".")
                ?.map { it.toInt() }
                ?: listOf(0, 0, 0)
        }
    }

    private suspend fun FirebaseRemoteConfig.fetchAndActivateSuspend(): Boolean =
        fetch(0).await().let { activate().await() }
}
