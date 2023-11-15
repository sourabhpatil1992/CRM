package com.venter.crm.empMang

import android.content.Context
import android.content.Intent
import android.net.Network
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.venter.crm.R
import com.venter.crm.databinding.ActivityAdminRawDataBinding
import com.venter.crm.databinding.LayoutRawdatamsgBinding
import com.venter.crm.models.CampData
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminRawDataActivity : AppCompatActivity() {
    private var _binding:ActivityAdminRawDataBinding? = null
    private val binding:ActivityAdminRawDataBinding
        get() = _binding!!

    private var DataUri: Uri? = null

    var campData:List<CampData>? = null

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            val contentResolver = contentResolver
            val cursor = contentResolver.query(selectedUri, null, null, null, null)

            cursor?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                val fileName = cursor.getString(nameIndex)

                if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                    DataUri = selectedUri

                    val alertDialogBuilder = AlertDialog.Builder(this)

                    // Set the title and message for the AlertDialog
                    alertDialogBuilder.setTitle("Add Raw Data")
                    alertDialogBuilder.setIcon(R.drawable.crm)
                    val bindingAlert = LayoutRawdatamsgBinding.inflate(layoutInflater)
                    alertDialogBuilder.setView(bindingAlert.root)

                    bindingAlert.cmpFile.text = getFileNameFromUri(this@AdminRawDataActivity,DataUri!!)


                    alertDialogBuilder.setPositiveButton("OK") {_, _ ->
                        val intent = Intent(this, DatabaseUploadService::class.java)


                        intent.putExtra("FileUri", DataUri.toString())
                        intent.putExtra("Name", bindingAlert.cmpName.text.toString())
                        intent.putExtra("Details", bindingAlert.cmpDetails.text.toString())
                        intent.putExtra("DataType", bindingAlert.spinData.selectedItem.toString())


                        startForegroundService(intent)

                        Toast.makeText(this,"Uploading file may take longer.", Toast.LENGTH_SHORT).show()

                    }
                    alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->

                    }



                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()


                } else {
                    Toast.makeText(this@AdminRawDataActivity,"Please Select Excel File gor upload data.",
                        Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameColumnIndex != -1) {
                    val displayName = it.getString(displayNameColumnIndex)
                    if (!displayName.isNullOrBlank()) {
                        fileName = displayName
                    }
                }
            }
        }
        return fileName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminRawDataBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.floatingActionButton.setOnClickListener {
            filePicker.launch("*/*")

        }

        getData()
    }

    private fun getData() {
        try {
            candidateViewModel.getRawDataCamping()
            candidateViewModel.campDataLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when(it)
                {
                    is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    is NetworkResult.Success ->
                    {
                        campData = it.data
                        binding.viewPager.adapter = RawDataViewPagerAdapter(supportFragmentManager)
                        binding.tabLayout.setupWithViewPager(binding.viewPager)
                    }
                }
            }

        }catch (e:Exception)
        {
            Log.d(TAG,"Error i in AdminRawDataActivity.kt is : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}