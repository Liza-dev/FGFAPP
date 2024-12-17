package com.akshat.newapplication.network

import javax.inject.Inject

class CheckInternetConnectivity @Inject constructor(
    private val network: NetworkAPI) {
    operator fun invoke(): Boolean {
        return network.isInternetAvailable()
    }
}