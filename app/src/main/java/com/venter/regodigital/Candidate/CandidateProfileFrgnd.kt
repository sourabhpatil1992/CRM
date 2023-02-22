package com.venter.regodigital.Candidate

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
import com.venter.regodigital.utils.Constans.BASE_URL
import com.venter.regodigital.utils.Constans.TAG
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
class CandidateProfileFrgnd : Service() {


    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "regoDigital.apps.notifications"
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

            notify_noti()



            store_doc(intent)


        } catch (e: Exception) {
            Log.d(TAG, "Error in foreground CandidateProfileFrgrnd startcommand is " + e.message)

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
                    .setContentTitle("Gold Ledger")
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
                    .setContentTitle("Rego Digital Solution")
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
            Log.d(TAG, "Error in CandidateProfileFrgrnd.kt notify_nity() is .. " + e.message)
        }
    }

    private fun store_doc(intent: Intent?) {
        val img = Uri.parse(intent!!.getStringExtra("ProfileUri"))
        val candidateId: Int? = intent.getIntExtra("candidateId", 0)
        val protfileType = intent!!.getStringExtra("ProfileType")!!

        scope.launch {
            try {
                val authInterceptor = AuthInterceptorWorker(applicationContext)
                syncApi = Retrofit.Builder()
                    .client(OkHttpClient.Builder().addInterceptor(authInterceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(UserAuthApi::class.java)


                val parcelFiledsc =
                    contentResolver.openFileDescriptor(img, "r", null) ?: return@launch
                val inputStream = FileInputStream(parcelFiledsc.fileDescriptor)
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

                val response = syncApi.createCandidateProfile(
                    filePart, candidateId.toString(), protfileType
                )


                stopSelf()


            } catch (e: Exception) {
                Log.d(TAG, "Error in clientDocservice Store_doc()  is " + e.message)
            }


        }
        stopSelf()

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