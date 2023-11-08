package com.venter.crm.userledger

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

import com.venter.crm.databinding.ActivityWhatsAppBinding
import com.venter.crm.utils.Constans.BASE_URL
import com.venter.crm.utils.Constans.TAG

import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

@AndroidEntryPoint
class WhatsAppActivity : AppCompatActivity() {
    private var _binding:ActivityWhatsAppBinding? = null
    private val binding:ActivityWhatsAppBinding
        get() = _binding!!



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



            mSocket.emit("WhatsApp", userId.toString())

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
                            "Please try after some Time"->{
                                Toast.makeText(this, "Please try after some Time", Toast.LENGTH_SHORT).show()
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

            mSocket.on("progress")
            { args->

                runOnUiThread {
                    try
                    {


                        if(args[0].toString() !="100")
                        {
                            binding.progress.visibility = View.GONE
                        }
                        else{
                            binding.progress.visibility = View.VISIBLE
                            binding.progress.progress = args[0].toString().toInt()
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