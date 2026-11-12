package com.luca.pantry

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.luca.pantry.Database.DatabaseApp
import com.luca.pantry.Network.MissingImageWorker
import com.luca.pantry.Notification.ExpireWorkerReceiver
import com.luca.pantry.dao.ContainerDao
import java.time.Duration
import java.time.LocalDateTime
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

        createNotificationChannel()

        val now = LocalDateTime.now()
        val targetTime = now.withHour(19).withMinute(0).withSecond(0)

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

        MissingImageWorker.enqueue(this)
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