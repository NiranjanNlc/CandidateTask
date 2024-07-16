package org.nlc.candidatetask

import android.app.Application
import android.net.ConnectivityManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import org.nlc.candidatetask.sync.ItemSyncWorker
import org.nlc.candidatetask.sync.NetworkMonitor
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApplication : Application() {
    private val networkMonitor by lazy {
        NetworkMonitor(getSystemService(ConnectivityManager::class.java))
    }
    private val workManager by lazy { WorkManager.getInstance(this) }

    private val itemSyncWorker by lazy {
    }

    override fun onCreate() {
        super.onCreate()
        networkMonitor.start()
        setupPeriodicSync()
    }
    private fun setupPeriodicSync() {
            val periodicSyncRequest = PeriodicWorkRequestBuilder<ItemSyncWorker>(12, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            workManager.enqueueUniquePeriodicWork(
                "item_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicSyncRequest
            )
        }

    override fun onTerminate() {
        super.onTerminate()
        networkMonitor.stop()
    }
}