package com.luca.pantry.Network

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.luca.pantry.PantryApp

class MissingImageWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = PantryApp.database.prodottoDao()
        val prefs = applicationContext.getSharedPreferences("off_checked_barcodes", Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()

        val toCheck = dao.getItemsMissingImage()
            .filter { p -> p.barcode != null && now - prefs.getLong(p.barcode, 0L) > RECHECK_MS}
            .take(30)

        //30 try max to not overload API
        for (product in toCheck) {
            val barcode = product.barcode ?: continue

            when (val result = OpenFoodFactsClient.lookupDetailed(barcode)) {
                is OffResult.Found -> {
                    val image = result.product.imageUrl
                    if (image != null) {
                        dao.setImageIfMissing(product.id, barcode, image, now)
                    } else {
                        prefs.edit().putLong(barcode, now).apply()
                    }
                }

                OffResult.NotFound -> prefs.edit().putLong(barcode, now).apply()
                OffResult.NetworkError -> return Result.retry()
            }
        }
        return Result.success()
    }

    companion object {
        private const val RECHECK_MS = 7L * 24 * 60 * 60 * 1000
        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<MissingImageWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "fetch_missing_images",
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request
            )
        }
    }
}