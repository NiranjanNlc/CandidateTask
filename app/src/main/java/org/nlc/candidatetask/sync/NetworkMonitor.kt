package org.nlc.candidatetask.sync

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest


class NetworkMonitor(
    private val connectivityManager: ConnectivityManager
) {
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            // Trigger sync operation
//            itemRepository.syncItems()
        }
    }

    fun start() {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    fun stop() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}