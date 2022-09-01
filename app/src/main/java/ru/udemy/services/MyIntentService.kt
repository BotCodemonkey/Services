package ru.udemy.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*


// Отличия от Foreground
// - Все выполняется в методе onHandleIntent НЕ на главном потоке
// - Не нужно в ручную останоавливать сервис
// - Не запускается несколько сервисов параллельно
class MyIntentService : IntentService(SERVICE_NAME) {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        // START_REDELIVER_INTENT перезапускает интент
        setIntentRedelivery(true)

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    // выполняется не на главном потоке в отличии от onStartCommand!!!
    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log("Timer $i")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.e("SERVICE_TAG", "MyIntentService: $message")
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    companion object {

        private const val SERVICE_NAME = "MyIntentService"
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }

    }

}