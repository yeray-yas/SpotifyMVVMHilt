package com.yerayyas.spotifymvvmhilt.data.repositories

interface Repository {
    fun getCurrentVersion(): List<Int>
    suspend fun getMinAllowedVersion(): List<Int>
}
