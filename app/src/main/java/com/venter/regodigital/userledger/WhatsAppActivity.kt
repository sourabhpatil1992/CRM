package com.venter.regodigital.userledger

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.venter.regodigital.Dashboard.AdminDashboard
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.Login.LogInActivity
import com.venter.regodigital.R
import com.venter.regodigital.databinding.ActivityWhatsAppBinding
import com.venter.regodigital.utils.Constans.BASE_URL
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException
import java.time.LocalDateTime

@AndroidEntryPoint
class WhatsAppActivity : AppCompatActivity() {
    private var _binding:ActivityWhatsAppBinding? = null
    private val binding:ActivityWhatsAppBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var userId = 0
    private var userName = ""

    private lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWhatsAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getIntExtra("userId",0)
        userName = intent.getStringExtra("userName")!!

        binding.msg.text = "Scan QR code for "+userName


        try {
            val options = IO.Options()
            options.forceNew = true // Use this option if you want to establish a new connection every time
            mSocket = IO.socket(BASE_URL, options)
            mSocket.connect()

            binding.progressbar.visibility = View.VISIBLE

            mSocket.emit("chat message", "Hi Server")

            mSocket.emit("WhatsApp", userId.toString())

            /*mSocket.on("chat message")
            { args ->

                runOnUiThread {
                    try
                    {
                        binding.progressbar.visibility = View.GONE
                        val qrCodeBitmap = generateQRCode(args[0].toString())
                        binding.qrCodeImageView.setImageBitmap(qrCodeBitmap)
//Log.d(TAG,"---------------------------"+args[0])

                    }
                    catch (e: Exception) {
                        Log.d(TAG, "Erro In the activity Budget.."+e.message.toString())

                    }
                }
            }*/

            mSocket.on("qrCode")
            { args ->

                runOnUiThread {
                    try
                    {
                        binding.progressbar.visibility = View.GONE
                        when(args[0].toString())
                        {
                            "Application is Authenticated" ->{
                                Toast.makeText(this, "Application Authenticate.", Toast.LENGTH_SHORT).show()
                                this.finish()
                            }
                            else ->{
                                val qrCodeBitmap = generateQRCode(args[0].toString())
                                binding.qrCodeImageView.setImageBitmap(qrCodeBitmap)
                            }
                        }



                    }
                    catch (e: Exception) {
                        Log.d(TAG, "Erro In the activity Budget.."+e.message.toString())

                    }
                }
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        /*candidateViewModel.intilWhats(userId)

        val currentDateTime = LocalDateTime.now()
        Log.d(TAG,currentDateTime.toString())

        Handler().postDelayed(
            {


                try {
                    Toast.makeText(this,"Please try again or wait for some time.",Toast.LENGTH_SHORT).show()
                    this.finish()
                } catch (e: Exception) {
                    Log.d(TAG, "Error in WhatsAppActivity.kt Handler().postDelayed is  " + e.message)
                }

            }, 60000
        )

        candidateViewModel.stringResData.observe(this)
        {
            binding.progressbar.visibility =View.GONE
            when(it)
            {
                is NetworkResult.Loading ->{
                    binding.progressbar.visibility =View.VISIBLE
                }
                is NetworkResult.Error ->{
                    Log.d(TAG,"Error in WhatsAppActivity.kt getting server data is () "+it.message)
                    Toast.makeText(this,"Please try again or wait for some time.",Toast.LENGTH_SHORT).show()
                    this.finish()

                }
                is NetworkResult.Success ->{
                    if(it.data!! != "Application is Authenticated") {
                        val qrCodeBitmap = generateQRCode(it.data!!)
                        binding.qrCodeImageView.setImageBitmap(qrCodeBitmap)

                        Handler().postDelayed(
                            {


                                try {
                                    Toast.makeText(
                                        this,
                                        "Please try again or wait for some time.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    this.finish()
                                } catch (e: Exception) {
                                    Log.d(
                                        TAG,
                                        "Error in WhatsAppActivity.kt Handler().postDelayed is  " + e.message
                                    )
                                }

                            }, 30000
                        )
                    }
                    else {
                        Toast.makeText(this, "Application Authenticate.", Toast.LENGTH_SHORT).show()
                        this.finish()
                    }

                }
            }
        }*/


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mSocket.disconnect()
    }

    private fun generateQRCode(data: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }

        return bitmap
    }
}