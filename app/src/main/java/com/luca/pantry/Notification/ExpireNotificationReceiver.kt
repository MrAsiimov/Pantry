package com.luca.pantry.Notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.luca.pantry.Database.DB
import com.luca.pantry.Database.DatabaseApp
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpireNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            val products = DB.prodottoDao.getAllItems()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val today = LocalDate.now()

            val expiring = products.filter {
                val expire = LocalDate.parse(it.expiringDate, formatter)
                expire.isBefore(today.plusDays(15)) && expire.isAfter(today.minusDays(1))
            }

            if (expiring.isNotEmpty()) {
                showNotification(context, expiring.size)
            }

            Log.d("RECEIVER", "Ricevuto trigger di notifica")
        }
    }

    private fun showNotification(context: Context, count: Int) {
        val builder = NotificationCompat.Builder(context, "expiring_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Prodotti in scadenza")
            .setContentText("Hai $count prodotti in scadenza nei prossimi 15 giorni")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        NotificationManagerCompat.from(context).notify(1001, builder.build())
    }
}