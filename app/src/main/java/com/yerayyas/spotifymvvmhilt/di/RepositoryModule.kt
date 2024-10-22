package com.yerayyas.spotifymvvmhilt.di

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.yerayyas.spotifymvvmhilt.data.AppVersionProvider
import com.yerayyas.spotifymvvmhilt.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppVersionProvider(remoteConfig: FirebaseRemoteConfig): AppVersionProvider {
        return AppVersionProvider(remoteConfig)
    }

    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context,
        appVersionProvider: AppVersionProvider
    ): Repository {
        return Repository(context, appVersionProvider)
    }
}


