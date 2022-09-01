package ru.udemy.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")

        val start = intent?.getIntExtra(EXTRA_START, 0) ?: 0

        coroutineScope.launch {
            for (i in start until start + 100) {
                delay(100)
                log("Timer $i")
            }
        }
        //return START_REDELIVER_INTENT - если приложение умирает, интент (из аргумента метода) перенаправляется снова
        //return START_STICKY - если приложение умирает, сервис перезапускается (интент null)
        //return START_NOT_STICKY - если приложение умирает, сервис НЕ перезапускается
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String) {
        Log.e("SERVICE_TAG", "MyService: $message")
    }

    companion object {

        private const val EXTRA_START = "start"

        fun newIntent(context: Context, start: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, start)
            }
        }
    }

}