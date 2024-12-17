package com.akshat.newapplication.di

import android.content.Context
import com.akshat.newapplication.network.CheckInternetConnectivity
import com.akshat.newapplication.network.NetworkAPI
import com.akshat.newapplication.network.PostsAPI
import com.akshat.newapplication.repository.NetworkRepository
import com.akshat.newapplication.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providePostsDataApi(): PostsAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PostsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(@ApplicationContext context: Context): NetworkAPI {
        return NetworkRepository(context)
    }

    @Provides
    @Singleton
    fun provideCheckInternetConnectivityUseCase(networkRepository: NetworkRepository): CheckInternetConnectivity {
        return CheckInternetConnectivity(networkRepository)
    }
}