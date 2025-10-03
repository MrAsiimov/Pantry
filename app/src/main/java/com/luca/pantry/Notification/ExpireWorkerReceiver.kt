package com.luca.pantry.Notification

import android.Manifest
import android.R
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.luca.pantry.Database.DB
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class ExpireWorkerReceiver(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        val prodotti = runBlocking { DB.prodottoDao.getAllItems() }
        val today = LocalDate.now()

        val inScadenza = prodotti.filter {
            val expiry = runCatching { LocalDate.parse(it.expiringDate) }.getOrNull()
            expiry != null && expiry.isBefore(today.plusDays(15)) && expiry.isAfter(today.minusDays(1))
        }

        if (inScadenza.isNotEmpty() && (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)) {
            val builder = NotificationCompat.Builder(applicationContext, "expiring_channel")
                .setSmallIcon(R.drawable.ic_dialog_info)
                .setContentTitle("Prodotti in scadenza")
                .setContentText("Hai ${inScadenza.size} prodotti in scadenza nei prossimi 15 giorni")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            NotificationManagerCompat.from(applicationContext).notify(1001, builder.build())
        }

        return Result.success()
    }
}