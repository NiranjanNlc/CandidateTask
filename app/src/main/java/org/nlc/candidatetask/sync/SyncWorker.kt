package org.nlc.candidatetask.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.nlc.candidatetask.repository.BookRepository
import javax.inject.Inject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: BookRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("SyncWorker", "doWork: Synchronizing data")
            repository.synchronize()
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "doWork: Error synchronizing data", e)
            Result.retry()
        }
    }
}
