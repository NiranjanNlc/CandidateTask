 package org.nlc.candidatetask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.nlc.candidatetask.sync.ItemSyncWorker
import org.nlc.candidatetask.ui.theme.CandidateTaskTheme
import org.nlc.candidatetask.ui.view.ItemListScreen
import java.util.concurrent.TimeUnit

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
                ItemListScreen()
            }
        }
    }
}

