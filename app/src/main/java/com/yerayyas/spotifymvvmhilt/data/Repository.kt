package com.yerayyas.spotifymvvmhilt.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Repository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appVersionProvider: AppVersionProvider
) {

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        appVersionProvider.setupRemoteConfig()
    }

    fun getCurrentVersion(): List<Int> {
        return runCatching {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.split(".").map { it.toInt() }
        }.getOrDefault(listOf(0, 0, 0))
    }

    suspend fun getMinAllowedVersion(): List<Int> {
        return appVersionProvider.getMinAllowedVersion()
    }
}

