package com.venter.crm.hrMangment

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.IBinder
import android.provider.OpenableColumns
import android.util.Log
import com.venter.crm.MainActivity
import com.venter.crm.R
import com.venter.crm.api.AuthInterceptorWorker
import com.venter.crm.api.UserAuthApi
import com.venter.crm.utils.Constans
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

class EmpProfileDPService : Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "crm.apps.notifications"
    private val description = "Server Sync Process"
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var syncApi: UserAuthApi

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {

            notifyNotification()



            storeDoc(intent)


        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in EmpProfileDPService.kt onStartCommand is : ${e.message}")

        }
        return START_STICKY
    }

    private fun storeDoc(intent: Intent?) {
        val img = Uri.parse(intent!!.getStringExtra("ProfileUri"))
        val empId: Int = intent.getIntExtra("empId", 0)

        scope.launch {
            try {
                val authInterceptor = AuthInterceptorWorker(applicationContext)
                syncApi = Retrofit.Builder()
                    .client(OkHttpClient.Builder().addInterceptor(authInterceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constans.BASE_URL)
                    .build()
                    .create(UserAuthApi::class.java)


                val parcelFilled =
                    contentResolver.openFileDescriptor(img, "r", null) ?: return@launch
                val inputStream = FileInputStream(parcelFilled.fileDescriptor)

                val file = File(cacheDir, contentResolver.getFileName(img))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                Compressor.compress(applicationContext, file) {
                    resolution(320, 320)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(2_000_000) //size in bytes
                    destination(file)

                }

                val requestBody: RequestBody =
                    RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                val filePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("pic", file.name, requestBody)

                 syncApi.employeeProfileDp(
                    filePart, empId.toString()
                )
                parcelFilled.close()


                stopSelf()


            } catch (e: Exception) {
                Log.d(Constans.TAG, "Error in EmpProfileDPService.kt Store_doc()  is ${e.message}")
            }


        }
        stopSelf()
    }

    private fun notifyNotification() {
        try {

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intent = Intent(this, MainActivity::class.java)


            val pendingIntent =
                PendingIntent.getActivity(
                    this,
                    1,
                    intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )

            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)


            builder = Notification.Builder(this, channelId)
                .setContentText("Uploading Data on server.....")
                .setContentTitle("CRM")
                .setSmallIcon(R.drawable.crm)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.crm
                    )
                )
                .setContentIntent(pendingIntent)

            startForeground((1..9999).random(), builder.build())


        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in EmpProfileDPService.kt notifyNotification() is .. ${e.message}")
        }
    }

    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }
}