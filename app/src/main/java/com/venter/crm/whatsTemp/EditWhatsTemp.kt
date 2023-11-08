package com.venter.crm.whatsTemp

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.crm.Dashboard.AdminDashboard
import com.venter.crm.R
import com.venter.crm.databinding.ActivityEditWhatsTempBinding
import com.venter.crm.models.WhatsappTemplateMsg
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWhatsTemp : AppCompatActivity() {
    private var _binding: ActivityEditWhatsTempBinding? = null
    private val binding: ActivityEditWhatsTempBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    var template: WhatsappTemplateMsg? = null

    private var ProfileUri: Uri? = null

    private var deleteFile: Boolean = false


    //private var headerFile: String = ""

    private var headerChange = false

    private var contract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                headerChange = true
                ProfileUri = uri
                binding.imgWhats.setImageURI(null)
                val fileType = contentResolver.getType(uri).toString()
                if (fileType.split("/")[0] == "image")
                    binding.imgWhats.setImageURI(ProfileUri)
                else if (fileType.split("/")[0] == "video")
                    binding.imgWhats.setImageResource(R.drawable.video_icon)
                else
                    binding.imgWhats.setImageResource(R.drawable.doc_icon)
                binding.imgWhats.visibility = View.VISIBLE
                binding.btnDelImg.isEnabled = true
                binding.txtSelect.text = "Change File"
                deleteFile = false
                val fileDescriptor =
                    applicationContext.contentResolver.openAssetFileDescriptor(uri, "r")

                binding.btnDelImg.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.regocolor
                    )
                )

            }


        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditWhatsTempBinding.inflate(layoutInflater)
        setContentView(binding.root)

        template = intent.getParcelableExtra<WhatsappTemplateMsg>("temp")!!

        if (template != null) {
            binding.txtMsg.setText(template!!.tempMsg)
            binding.tempName.setText(template!!.temp_name)

            if (template!!.header_name != null && template!!.header_name != "") {
                binding.imgWhats.setImageURI(null)
                val filetype = template!!.header_name!!.split("\\.".toRegex())
                when (filetype[1]) {
                    "pdf" -> {
                        binding.imgWhats.setImageResource(R.drawable.doc_icon)
                    }

                    "mp4" -> {
                        binding.imgWhats.setImageResource(R.drawable.video_icon)
                    }

                    else -> {
                        Picasso.get()
                            .load(Constans.BASE_URL + "assets/whatstemp/" + template!!.header_name)
                            .fit()
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(binding.imgWhats)
                    }

                }

                binding.btnDelImg.isEnabled = true
                binding.txtSelect.text = "Change File"


                binding.imgWhats.visibility = View.VISIBLE
                binding.btnDelImg.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.regocolor
                    )
                )
            } else
                binding.btnDelImg.setBackgroundColor(ContextCompat.getColor(this, R.color.temp5))
        }

        binding.btnSelectImg.setOnClickListener {
            selectDoc()
        }

        binding.btnDelImg.setOnClickListener {
            deleteFile = true
            headerChange = true
            ProfileUri = null
            binding.imgWhats.visibility = View.GONE
            binding.btnDelImg.isActivated = false
            binding.txtSelect.text = "select File"
            binding.btnDelImg.setBackgroundColor(ContextCompat.getColor(this, R.color.temp5))
            template!!.header_name = ""

        }

        binding.btnUpdateTemp.setOnClickListener {

                updateWhatsReq()

        }


    }

    private fun selectDoc() {
        try {
            contract.launch("*/*")


        } catch (e: Exception) {
            Log.d(TAG, "Error in EditWhatsApiTempActivity.kt selectDoc() " + e.message)
        }

    }

    private fun updateWhatsReq() {
        try {
            if (ProfileUri != null || template!!.tempMsg != binding.txtMsg.text.toString() || deleteFile || template!!.temp_name != binding.tempName.text.toString()) {
                if (ProfileUri != null) {
                            //Header File Changed

                    val intent = Intent(this, ImageUploadFrgnd::class.java)

                    intent.putExtra("intentType", "UploadImageWithNewTemp")
                    intent.putExtra("FileUri", ProfileUri.toString())
                    intent.putExtra("temp", template)
                    intent.putExtra("template", binding.txtMsg.text.toString())
                    intent.putExtra("temp_name", binding.tempName.text.toString())


                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

                        startForegroundService(intent)

                    } else {

                        startService(intent)
                    }
                    Toast.makeText(this,"Uploading file may take longer.",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,AdminDashboard::class.java))
                    this.finish()


                } else {
                    //Only Text Change
                    if(template!!.header_name.toString()=="null")
                        template!!.header_name = ""
                    candidateViewModel.updateTemp(template!!.id.toString(),binding.txtMsg.text.toString(),
                        template!!.header_name.toString(),binding.tempName.text.toString())

                    candidateViewModel.stringResData.observe(this)
                    {
                        binding.progressbar.visibility = View.GONE
                        when(it)
                        {
                            is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                            is NetworkResult.Error ->
                                Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                            is NetworkResult.Success -> {
                                Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,AdminDashboard::class.java))
                                this.finish()
                            }

                        }
                    }
                }


            } else {
                Toast.makeText(this, "Changes not detect.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in EditWhatsTemp.kt EditWhatsReq() is " + e.message)
        }


    }
}