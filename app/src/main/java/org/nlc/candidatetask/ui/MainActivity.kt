 package org.nlc.candidatetask.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import org.nlc.candidatetask.sync.ItemSyncWorker
import org.nlc.candidatetask.ui.theme.CandidateTaskTheme
import org.nlc.candidatetask.ui.screens.BookListScreen
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Create the PeriodicWorkRequest
        val periodicSyncRequest = PeriodicWorkRequestBuilder<ItemSyncWorker>(12, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        // Enqueue the PeriodicWorkRequest
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "item_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicSyncRequest
            )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CandidateTaskTheme {
                BookListScreen( )
            }
        }
    }
}

