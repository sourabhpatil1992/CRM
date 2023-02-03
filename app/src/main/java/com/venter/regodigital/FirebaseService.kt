package com.venter.regodigital

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.TokenManger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseService : FirebaseMessagingService()
{
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "GoPromo.apps.notifications"
    private val description = "Server Sync Process"
    @Inject
    lateinit var tokenManger: TokenManger

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        try {

            tokenManger.saveFirebaseToken(token)
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in FirebaseService.kt onNewToken() is "+e.message)
        }

    }

}