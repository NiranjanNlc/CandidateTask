package org.nlc.candidatetask.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ItemSyncWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        return try {
//            val items = itemRepository.getAllItems()
//            firebaseItemDataSource.saveAllItems(items)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}