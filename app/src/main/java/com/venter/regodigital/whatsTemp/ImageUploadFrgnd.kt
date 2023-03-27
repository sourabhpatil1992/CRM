package com.venter.regodigital.whatsTemp

import android.annotation.SuppressLint
import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.OpenableColumns
import android.util.Log
import com.venter.regodigital.MainActivity
import com.venter.regodigital.R
import com.venter.regodigital.api.AuthInterceptorWorker
import com.venter.regodigital.api.UserAuthApi
import com.venter.regodigital.models.WhatsappTemplateMsg
import com.venter.regodigital.utils.Constans.BASE_URL
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.TokenManger
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
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
class ImageUploadFrgnd : Service() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "rego.apps.notifications"
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


            val intentType = intent!!.getStringExtra("intentType").toString()
            if(intentType == "UploadImageWithNewTemp")
                store_doc(intent)
            else if(intentType == "UploadImage")
                upload_image(intent)


        } catch (e: Exception) {
            Log.d(TAG, "Error in foreground clientDocService startcommand is " + e.message)

        }
        return START_STICKY
    }

    fun notify_noti() {
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)


                builder = Notification.Builder(this, channelId)
                    .setContentText("Uploading Data on server.....")
                    .setContentTitle("Rego Digital")
                    .setSmallIcon(R.drawable.regologo)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.regologo
                        )
                    )
                    .setContentIntent(pendingIntent)
            } else {

                builder = Notification.Builder(this)
                    .setContentText("Uploading Data on server.....")
                    .setContentTitle("Gold Ledger")
                    .setSmallIcon(R.drawable.regologo)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.regologo
                        )
                    )
                    .setContentIntent(pendingIntent)
            }

            startForeground((1..9999).random(), builder.build())


        } catch (e: Exception) {
            Log.d(TAG, "Error in ImageUploafFrgnd.kt notify_nity() is .. " + e.message)
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

    private fun store_doc(intent: Intent?) {


        scope.launch {
            try {
                val fileUri = Uri.parse(intent!!.getStringExtra("FileUri").toString())
                val header = intent!!.getStringExtra("header").toString()
                val template = intent!!.getStringExtra("template").toString()
                val temp: WhatsappTemplateMsg? = intent!!.getParcelableExtra<WhatsappTemplateMsg>("temp")

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

                if (temp!!.hederType == "IMAGE")

                    Compressor.compress(applicationContext, file) {
                        resolution(320, 320)
                        quality(80)
                        format(Bitmap.CompressFormat.JPEG)
                        size(100_000) //size in bytes
                        destination(file)

                    }

                val requestBody: RequestBody =
                    RequestBody.create(mimeType.toString().toMediaTypeOrNull(), file)


                val filePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("pic", file.name, requestBody)



                val response =syncApi.UpdateWhatsApiAttachments(
                    filePart,
                    temp.id.toString(),header,temp.tempMsg.toString(),
                    temp.hederType.toString()
                )

                if(response.toString().isNotEmpty())
                    stopSelf()




            } catch (e: Exception) {
                Log.d(TAG, "Error in clientDocservice Store_doc()  is " + e.message)
            }


        }


    }

    private fun upload_image(intent: Intent)
    {

        scope.launch {
            try {
                val fileUri = Uri.parse(intent!!.getStringExtra("FileUri").toString())
                val tempId = intent.getStringExtra("tempId").toString()
                val headerType = intent.getStringExtra("headerType").toString()




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

                if ( headerType == "IMAGE")

                    Compressor.compress(applicationContext, file) {
                        resolution(320, 320)
                        quality(80)
                        format(Bitmap.CompressFormat.JPEG)
                        size(100_000) //size in bytes
                        destination(file)

                    }

                val requestBody: RequestBody =
                    RequestBody.create(mimeType.toString().toMediaTypeOrNull(), file)


                val filePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("pic", file.name, requestBody)



                val response =syncApi.ChangeWhatsApiAttachments(filePart, tempId,headerType)

                if(response.toString().isNotEmpty())
                    stopSelf()




            } catch (e: Exception) {
                Log.d(TAG, "Error in clientDocservice Store_doc()  is " + e.message)
            }


        }

    }


}