package com.yerayyas.spotifymvvmhilt.domain.usecases

import com.yerayyas.spotifymvvmhilt.data.Repository
import javax.inject.Inject

class CanAccessToAppUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): Boolean {
        val currentVersion = repository.getCurrentVersion()
        val minAllowedVersion = repository.getMinAllowedVersion()

        return currentVersion.zip(minAllowedVersion).all { (current, min) ->
            current >= min
        }
    }
}
