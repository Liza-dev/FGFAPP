package com.liza.fgfandroidapp.di

import android.content.Context
import com.liza.fgfandroidapp.network.CheckInternetConnectivity
import com.liza.fgfandroidapp.network.NetworkAPI
import com.liza.fgfandroidapp.network.ApiServices
import com.liza.fgfandroidapp.repository.NetworkRepository
import com.liza.fgfandroidapp.util.BASE_URL
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
    fun providePostsDataApi(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiServices::class.java)
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