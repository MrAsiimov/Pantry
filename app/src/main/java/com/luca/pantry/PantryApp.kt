package com.luca.pantry

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.luca.pantry.Database.DB
import com.luca.pantry.Database.DatabaseApp
import com.luca.pantry.Notification.ExpireNotificationReceiver
import com.luca.pantry.Notification.ExpireWorkerReceiver
import com.luca.pantry.dao.ContainerDao
import java.time.Duration
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit

class PantryApp: Application() {
    companion object {
        lateinit var database: DatabaseApp
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            DatabaseApp::class.java,
            "pantry_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        DB.init(this)

        createNotificationChannel()

        val now = LocalDateTime.now()
        val targetTime = now.withHour(18).withMinute(52).withSecond(30)

        val delay = Duration.between(now, targetTime)
        val initialDelay = if (delay.isNegative) delay.plusDays(1) else delay

        val workRequest = PeriodicWorkRequestBuilder<ExpireWorkerReceiver>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay.toMinutes(), TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "expire_check",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )



        /*val intent = Intent(this, ExpireNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 5)
            set(Calendar.SECOND, 30)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent )*/
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel (
                "expiring_channel",
                "expiring_notify",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    val containerDao: ContainerDao
        get() = database.containerDao()
}