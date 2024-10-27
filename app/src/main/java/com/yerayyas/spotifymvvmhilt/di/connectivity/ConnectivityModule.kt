package com.yerayyas.spotifymvvmhilt.di.connectivity

import android.content.Context
import android.net.ConnectivityManager
import com.yerayyas.spotifymvvmhilt.data.connectivity.ConnectivityFlow
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideConnectivityFlow(
        connectivityManager: ConnectivityManager,
        @ApplicationContext context: Context
    ): ConnectivityFlow {
        return ConnectivityFlow(connectivityManager, context)
    }
}
