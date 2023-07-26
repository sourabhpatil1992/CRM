package com.venter.regodigital

import android.app.Application
import com.venter.regodigital.utils.Constans.BASE_URL
import dagger.hilt.android.HiltAndroidApp
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException


@HiltAndroidApp
class RegoDigital: Application() {

    private lateinit var mSocket: Socket


    /*override fun onCreate() {
        super.onCreate()
        try {
            val options = IO.Options()
            options.forceNew = true // Use this option if you want to establish a new connection every time
            mSocket = IO.socket(BASE_URL, options)
            mSocket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun getSocket(): Socket {
        return mSocket
    }*/
}