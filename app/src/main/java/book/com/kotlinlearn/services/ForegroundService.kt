package book.com.kotlinlearn.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class ForegroundService : Service() {

    private val LOG_TAG = "ForegroundService"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "My Channel"
            val channel = NotificationChannel(CHANNEL_ID,
                    "Channel Human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notification = Notification.Builder(this, CHANNEL_ID)
            notification.setContentTitle("ForeGroundServices")
                    .setContentText("ForegroundService Message")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)

            startForeground(1, notification.build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOG_TAG, "In onDestroy")
        Toast.makeText(this, "Service Detroyed!", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent): IBinder {
        // Used only in case if services are bound (Bound Services).
        throw UnsupportedOperationException("Not yet implemented")
    }
}