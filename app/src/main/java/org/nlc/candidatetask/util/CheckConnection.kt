package org.nlc.candidatetask.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat


fun isConnected(context: Context): Boolean {
    val connectivityManager = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
    val network = connectivityManager?.activeNetwork
    val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)
    return networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}