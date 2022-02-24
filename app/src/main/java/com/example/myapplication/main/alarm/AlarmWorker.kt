package com.example.myapplication.main.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.R
import com.example.myapplication.main.common.Constants
import java.util.concurrent.atomic.AtomicInteger

class AlarmWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.DefaultValue.CHANNEL_ID
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.DefaultValue.CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, Constants.DefaultValue.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_qr_code)
            .setContentTitle(inputData.getString(Constants.DefaultValue.KEY_NAME))
            .setContentText(context.getString(R.string.notification_desc))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context)
            .notify(AtomicInteger(0).incrementAndGet(), notification.build())
        return Result.success()
    }
}
