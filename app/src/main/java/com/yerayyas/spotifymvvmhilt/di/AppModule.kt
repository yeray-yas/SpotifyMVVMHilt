package com.yerayyas.spotifymvvmhilt.di

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.yerayyas.spotifymvvmhilt.data.Repository
import com.yerayyas.spotifymvvmhilt.domain.usecases.CanAccessToAppUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    // Provision of FirebaseRemoteConfig
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }

    // Provision of repository with dependency inyection
    @Provides
    fun provideRepository(
        @ApplicationContext context: Context, // Hilt inyecta el contexto automáticamente
        remoteConfig: FirebaseRemoteConfig
    ): Repository {
        return Repository(context, remoteConfig)
    }

    // Provisión del caso de uso que depende del repositorio
    @Provides
    fun provideCanAccessToAppUseCase(repository: Repository): CanAccessToAppUseCase {
        return CanAccessToAppUseCase(repository)
    }
}
