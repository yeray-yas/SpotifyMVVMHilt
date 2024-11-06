package com.yerayyas.spotifymvvmhilt.data.repositories

import android.content.Context
import com.yerayyas.spotifymvvmhilt.data.AppVersionProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appVersionProvider: AppVersionProvider
) : Repository {

    init {
        appVersionProvider.setupRemoteConfig()
    }

    override fun getCurrentVersion(): List<Int> {
        return runCatching {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.split(".").map { it.toInt() }
        }.getOrDefault(listOf(0, 0, 0))
    }

    override suspend fun getMinAllowedVersion(): List<Int> {
        return runCatching {
            appVersionProvider.getMinAllowedVersion()
        }.getOrElse {
            listOf(0, 0, 0)
        }
    }
}

