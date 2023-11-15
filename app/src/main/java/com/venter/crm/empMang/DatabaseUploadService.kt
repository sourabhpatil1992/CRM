package com.venter.crm.empMang

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.IBinder
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.venter.crm.MainActivity
import com.venter.crm.R
import com.venter.crm.api.AuthInterceptorWorker
import com.venter.crm.api.UserAuthApi
import com.venter.crm.utils.Constans.BASE_URL
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.TokenManger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class DatabaseUploadService : Service() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "crm.apps.notifications"
    private val description = "Server Sync Process"
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var tokenManger: TokenManger


    @Inject
    lateinit var syncApi: UserAuthApi

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {

            notify_noti()


            store_doc(intent)



        } catch (e: Exception) {
            Log.d(TAG, "Error in foreground clientDocService startcommand is " + e.message)

        }
        return START_STICKY
    }

    private fun notify_noti() {
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
                .setSmallIcon(R.drawable.c_logo)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.crm
                    )
                )
                .setContentIntent(pendingIntent)

            startForeground((1..9999).random(), builder.build())


        } catch (e: Exception) {
            Log.d(TAG, "Error in DatabaseUploadService.kt notify_noty() is .. " + e.message)
        }
    }

    private fun store_doc(intent: Intent?) {


        scope.launch {
            try {
                val fileUri = Uri.parse(intent!!.getStringExtra("FileUri").toString())
                val campName = intent.getStringExtra("Name").toString()
                val campDet = intent.getStringExtra("Details").toString()
                val dataType =intent.getStringExtra("DataType").toString()



                val mimeType = applicationContext.contentResolver.getType(fileUri)

                val authInterceptor = AuthInterceptorWorker(applicationContext)
                syncApi = Retrofit.Builder()
                    .client(OkHttpClient.Builder().addInterceptor(authInterceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(UserAuthApi::class.java)

                val parcelFiledsc =
                    contentResolver.openFileDescriptor(fileUri, "r", null) ?: return@launch
                val inputStream = FileInputStream(parcelFiledsc.fileDescriptor)
                val file = File(cacheDir, contentResolver.getFileName(fileUri))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                val requestBody: RequestBody =
                    RequestBody.create(mimeType.toString().toMediaTypeOrNull(), file)


                val filePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("pic", file.name, requestBody)



                val response =syncApi.updateDatabase(
                    filePart,campName,campDet,dataType
                )


                if(response.toString().isNotEmpty()) {
                    stopSelf()
                    Toast.makeText(applicationContext,"Template updated successfully.", Toast.LENGTH_SHORT).show()
                }




            } catch (e: Exception) {
                Log.d(TAG, "Error in DatabaseUploadService.kt Store_doc()  is " + e.message)
            }


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