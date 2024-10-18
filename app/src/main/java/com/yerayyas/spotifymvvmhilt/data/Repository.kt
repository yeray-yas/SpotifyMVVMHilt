package com.yerayyas.spotifymvvmhilt.data

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.yerayyas.spotifymvvmhilt.SpotifyMVVMHiltApp.Companion.context
import com.yerayyas.spotifymvvmhilt.utils.Constants.Companion.MIN_VERSION
import kotlinx.coroutines.tasks.await

class Repository {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 3600L })
        fetchAndActivate()
    }

    fun getCurrentVersion(): List<Int> {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = packageInfo.versionName
            val versionParts = versionName.split(".")
            versionParts.map { it.toInt() }
        } catch (e: Exception) {
            listOf(0, 0, 0)
        }
    }

    suspend fun getMinAllowedVersion(): List<Int> {
        remoteConfig.fetch(0)
        remoteConfig.activate().await()
        val minVersion = remoteConfig.getString(MIN_VERSION)
        return if (minVersion.isBlank()) {
            listOf(0, 0, 0)
        } else {
            val versionParts = minVersion.split(".")
            versionParts.map { it.toInt() }
        }
    }
}