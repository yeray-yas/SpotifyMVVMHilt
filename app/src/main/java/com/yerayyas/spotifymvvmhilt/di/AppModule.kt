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

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance()

    @Provides
    fun provideRepository(
        @ApplicationContext context: Context,
        remoteConfig: FirebaseRemoteConfig
    ): Repository = Repository(context, remoteConfig)

    @Provides
    fun provideCanAccessToAppUseCase(repository: Repository): CanAccessToAppUseCase =
        CanAccessToAppUseCase(repository)
}

