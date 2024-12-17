package com.akshat.newapplication.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.akshat.newapplication.network.NetworkAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkAPI {
    override fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}