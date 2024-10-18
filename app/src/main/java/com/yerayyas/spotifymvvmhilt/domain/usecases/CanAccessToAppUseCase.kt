package com.yerayyas.spotifymvvmhilt.domain.usecases

import com.yerayyas.spotifymvvmhilt.data.Repository

class CanAccessToAppUseCase {

    val repository = Repository()

    suspend operator fun invoke(): Boolean {
        val currentVersion = repository.getCurrentVersion()
        val minAllowedVersion = repository.getMinAllowedVersion()

        for ((currentVersionPart, minVersionPart) in currentVersion.zip(minAllowedVersion)) {
            if (currentVersionPart != minVersionPart) {
                return currentVersionPart > minVersionPart
            }
        }
        return true

    }
}