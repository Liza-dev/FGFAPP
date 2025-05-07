package com.liza.fgfandroidapp.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.liza.fgfandroidapp.network.NetworkAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworksRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkAPI {
    override fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}