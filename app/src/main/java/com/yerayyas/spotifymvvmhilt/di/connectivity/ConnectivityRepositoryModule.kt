package com.yerayyas.spotifymvvmhilt.di.connectivity

import com.yerayyas.spotifymvvmhilt.data.repositories.connectivity.ConnectivityRepository
import com.yerayyas.spotifymvvmhilt.data.repositories.connectivity.ConnectivityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityRepositoryModule {

    @Binds
    abstract fun bindConnectivityRepository(
        connectivityRepositoryImpl: ConnectivityRepositoryImpl
    ): ConnectivityRepository
}