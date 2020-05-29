package com.interview.task.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O

class MyNotificationPublisher : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val DEFAULT_NOTIFICATION_CHANNEL_ID = "default"
        var NOTIFICATION_ID = "notification-id"
        var NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification =
            intent.getParcelableExtra(NOTIFICATION)
        if (SDK_INT >= O) {
            val importance = IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                DEFAULT_NOTIFICATION_CHANNEL_ID,
                importance
            )
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        assert(notificationManager != null)
        notificationManager.notify(id, notification)
    }

}