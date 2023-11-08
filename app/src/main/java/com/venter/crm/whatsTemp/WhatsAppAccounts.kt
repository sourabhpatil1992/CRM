package com.venter.crm.whatsTemp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.venter.crm.R
import com.venter.crm.databinding.ActivityWhatsAppAccountsBinding
import com.venter.crm.models.WhatsAppAccList
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket

@AndroidEntryPoint
class WhatsAppAccounts : AppCompatActivity(), WhatsAccInterface {
    private var _binding: ActivityWhatsAppAccountsBinding? = null
    private val binding: ActivityWhatsAppAccountsBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter: WhatsAccAdapter

    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWhatsAppAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            adapter = WhatsAccAdapter(this)

            val options = IO.Options()
            options.forceNew =
                true // Use this option if you want to establish a new connection every time
            mSocket = IO.socket(Constans.BASE_URL, options)
            mSocket.connect()

            getData()
            binding.floatingActionButton.setOnClickListener {
                createAccount()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in WhatsAppAccounts.kt is : ${e.message}")
        }
    }

    private fun createAccount() {
        try {
            candidateViewModel.createWhatsAppAcc()
            candidateViewModel.stringResData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                        candidateViewModel.getWhatsAppList()
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in WhatAppAccounts.kt createAccount() is : ${e.message}")
        }
    }

    private fun getData() {
        try {
            candidateViewModel.getWhatsAppList()
            candidateViewModel.whatsAccListResLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        binding.rcView.layoutManager =
                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcView.adapter = adapter
                        adapter.submitList(null)
                        adapter.submitList(it.data)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in WhatAppAccounts.kt getData() is : ${e.message}")
        }
    }

    override fun accLongClick(acc: WhatsAppAccList) {
        try {
            if (acc.acc_status == 1) {
                candidateViewModel.logOutWhatsApp(acc.id)
            }


                Toast.makeText(
                    this,
                    "Please don't refresh or close the activity.",
                    Toast.LENGTH_SHORT
                ).show()

                binding.progressbar.visibility = View.VISIBLE


                mSocket.emit("WhatsApp", acc.id)

                mSocket.on("qrCode")
                { args ->

                    runOnUiThread {
                        try {
                            binding.progressbar.visibility = View.GONE
                            when (args[0].toString()) {
                                "Application is Authenticated" -> {
                                    Toast.makeText(
                                        this,
                                        "Application Authenticate.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    this.finish()
                                }

                                "Please try after some Time" -> {
                                    Toast.makeText(
                                        this,
                                        "Please try after some Time",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    this.finish()
                                }

                                "Application is Authenticated Failed" -> {
                                    Toast.makeText(
                                        this,
                                        "Application is Authenticated Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    this.finish()
                                }

                                else -> {
                                    val qrCodeBitmap = generateQRCode(args[0].toString())

                                    val builders = AlertDialog.Builder(this)
                                    builders.setTitle("WhatsApp QR Code")
                                    val layout = LinearLayout(this)
                                    layout.orientation = LinearLayout.VERTICAL
                                    layout.setPadding(20, 20, 20, 20)

                                    val qrImage = ImageView(this)
                                    qrImage.setImageBitmap(qrCodeBitmap)
                                    layout.addView(qrImage)


                                    builders.setNeutralButton("Cancel") { dialogInterface, which ->

                                    }

                                    builders.setView(layout)
                                    builders.setIcon(
                                        ContextCompat.getDrawable(
                                            this,
                                            com.venter.crm.R.drawable.crm
                                        )
                                    )
                                    val alertDialog: AlertDialog = builders.create()
                                    alertDialog.setCancelable(true)
                                    alertDialog.show()
                                    //binding.qrCodeImageView.setImageBitmap(qrCodeBitmap)
                                }
                            }


                        } catch (e: Exception) {
                            Log.d(TAG, "Error In the WhatsAppAccounts.kt creatingQr() ${e.message}")

                        }
                    }
                }

        } catch (e: Exception) {
            Log.d(TAG, "Error in WhatsAppAccounts.kt accLongClick() is : ${e.message}")
        }
    }

    override fun logOut(acc: WhatsAppAccList) {
        candidateViewModel.logOutWhatsApp(acc.id)
        candidateViewModel.getWhatsAppList()
    }

    override fun chatWhats(acc: WhatsAppAccList) {
        Toast.makeText(this,"This feature is not available in your plan.",Toast.LENGTH_SHORT).show()
    }



    private fun generateQRCode(data: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
                )
            }
        }

        return bitmap
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        _binding = null
    }
}