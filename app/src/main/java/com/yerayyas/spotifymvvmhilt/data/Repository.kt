package com.yerayyas.spotifymvvmhilt.data

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.yerayyas.spotifymvvmhilt.utils.Constants.Companion.MIN_VERSION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Repository @Inject constructor(
    private val context: Context,
    private val remoteConfig: FirebaseRemoteConfig
) {

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 3600L })
        remoteConfig.fetchAndActivate()
    }

    fun getCurrentVersion(): List<Int> {
        // Manejo de posibles excepciones con un bloque seguro
        return runCatching {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.split(".").map { it.toInt() }
        }.getOrDefault(listOf(0, 0, 0))
    }

    suspend fun getMinAllowedVersion(): List<Int> {
        // Usar una función de extensión para evitar código repetido
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
