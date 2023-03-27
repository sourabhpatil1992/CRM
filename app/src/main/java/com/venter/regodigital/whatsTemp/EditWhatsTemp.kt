package com.venter.regodigital.whatsTemp

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.regodigital.Dashboard.AdminDashboard
import com.venter.regodigital.R
import com.venter.regodigital.databinding.ActivityEditWhatsTempBinding
import com.venter.regodigital.models.WhatsappTemplateMsg
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWhatsTemp : AppCompatActivity() {
    private var _binding:ActivityEditWhatsTempBinding?=  null
    private val binding:ActivityEditWhatsTempBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    var template:WhatsappTemplateMsg? =null

    private var ProfileUri: Uri? = null

    private var deleteFile: Boolean = false

    private lateinit var originalTemp:WhatsappTemplateMsg

    private var headerFile: String = ""

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
                val fileSize = fileDescriptor!!.length
                if (fileSize > 5000000) {
                    Toast.makeText(
                        this,
                        "File size is greater than 5MB. Please select another file.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditWhatsTempBinding.inflate(layoutInflater)
        setContentView(binding.root)

         template = intent.getParcelableExtra<WhatsappTemplateMsg>("temp")
        originalTemp = template!!
        if(template!=null)
        {
            binding.txtMsg.setText(template!!.tempMsg)
            when (template!!.hederType) {
                "IMAGE" -> if (template!!.headerPath != null && template!!.headerPath != "") {

                    binding.imgWhats.setImageURI(null)
                    Picasso.get()
                        .load(template!!.headerPath)
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(binding.imgWhats)
                    binding.imgWhats.visibility = View.VISIBLE
                }

                "VIDEO" -> {
                    binding.imgWhats.setImageURI(null)
                    binding.imgWhats.setImageResource(R.drawable.video_icon)
                    binding.imgWhats.visibility = View.VISIBLE
                    binding.imgWhats.setOnClickListener {
                        if (template!!.headerPath != null && template!!.headerPath != "") {
                            val builders = AlertDialog.Builder(this)
                            //set title for alert dialog
                            val vView = VideoView(this)
                            val uri = Uri.parse(template!!.headerPath)
                            vView.setVideoURI(uri)
                            val mediaController = MediaController(this)
                            mediaController.setAnchorView(vView)

                            mediaController.setMediaPlayer(vView)
                            vView.setMediaController(mediaController)

                            builders.setView(vView)
                            val alertDialog: AlertDialog = builders.create()
                            alertDialog.setCancelable(true)
                            alertDialog.show()
                            vView.start()

                        } else {
                            Toast.makeText(this, "Video not found.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                "DOCUMENT" -> {
                    binding.imgWhats.setImageURI(null)
                    binding.imgWhats.setImageResource(R.drawable.doc_icon)
                    binding.imgWhats.visibility = View.VISIBLE
                    binding.imgWhats.setOnClickListener {
                        if (template!!.headerPath != "" && template!!.headerPath != null) {
                            val urlString = template!!.headerPath
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.setPackage("com.android.chrome")
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                else -> {

                }

            }
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
            template!!.headerPath= ""
            template!!.hederType=""

        }

        binding.btnUpdateTemp.setOnClickListener {
            if (binding.txtMsg.text.isNotEmpty())
                updateWhatsReq()
            else
                Toast.makeText(this, "Empty Message is not allowed.", Toast.LENGTH_SHORT).show()
        }





    }

    private fun selectDoc() {
        try {
            val builders = AlertDialog.Builder(this)
            //set title for alert dialog
            builders.setTitle("Whatsapp Documents")


            builders.setMessage("Select the type of documents which will share on Whatsapp")
            builders.setPositiveButton("Documents") { _, which ->

                    try {

                        template!!.hederType = "DOCUMENT"
                        contract.launch("document/*")
                        headerFile = "DOCUMENT"
                        headerChange = true
                    } catch (e: Exception) {
                        Log.d(
                            TAG,
                            "Error in EditWhatsTempActivity.kt selectImage() is " + e.message
                        )
                    }

            }
            builders.setNegativeButton("Video") { _, _ ->

                    try {
                        template!!.hederType = "VIDEO"
                        contract.launch("video/*")
                        headerFile = "VIDEO"
                        headerChange = true
                    } catch (e: Exception) {
                        Log.d(
                            TAG,
                            "Error in EditWhatsTempActivity.kt selectImage() is " + e.message
                        )
                    }

            }
            builders.setNeutralButton("Image") { _, _ ->
                try {

                    template!!.hederType = "IMAGE"
                    contract.launch("image/*")
                    headerFile = "IMAGE"
                    headerChange = true
                } catch (e: Exception) {
                    Log.d(TAG, "Error in EditWhatsTempActivity.kt selectImage() is " + e.message)
                }

            }

            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))

            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()


        } catch (e: Exception) {
            Log.d(TAG, "Error in EditWhatsApiTempActivity.kt selectDoc() " + e.message)
        }

    }

    private fun updateWhatsReq() {
        val dbVer = (System.currentTimeMillis() / 1000).toString()
        if (!headerChange && template!!.tempMsg == binding.txtMsg.text.toString()) {
            //Here we check the Any changes detect in the template
            Toast.makeText(this, "Change not detect.", Toast.LENGTH_SHORT).show()
        }
        else {

            if (template!!.tempMsg != binding.txtMsg.text.toString()) {

                try {

                    if (ProfileUri != null) {
                            val intent = Intent(this, ImageUploadFrgnd::class.java)

                            intent.putExtra("intentType","UploadImageWithNewTemp")
                            intent.putExtra("FileUri", ProfileUri.toString())
                            intent.putExtra("temp",template)
                            intent.putExtra("template",binding.txtMsg.text.toString())


                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

                                startForegroundService(intent)

                            } else {

                                startService(intent)
                            }
                            Toast.makeText(this, "Updating WhatsApp Template may take a long time. Please wait for some time. Check after some time template is updated successfully or not.",Toast.LENGTH_LONG).show()
                            val intents  = Intent(this,AdminDashboard::class.java)
                            startActivity(intents)
                            this.finish()




                    } else {
                        template!!.tempMsg = binding.txtMsg.text.toString()

                        candidateViewModel.whatApiTemplateTextUpdate(template!!)
                        Toast.makeText(this, "Updating WhatsApp Template may take a long time. Please wait for some time. Check after some time template is updated successfully or not.",Toast.LENGTH_LONG).show()
                        val intent  = Intent(this,AdminDashboard::class.java)
                        startActivity(intent)
                        this.finish()
                    }




                }
                catch (e:Exception)
                {
                    Log.d(TAG,"Error in EditWhatsApiTemActivity.kt updateWhatsReq() GlobalScope.launch is "+e.message)
                }



            }
            else {
                if (ProfileUri != null) {
                        //Here is two conditions 1. Change Header type  2. Header Type not change
                        if(originalTemp.hederType != template!!.hederType)
                        {
                            Toast.makeText(this,"Please change the template for change the attachment of template.",Toast.LENGTH_SHORT).show()

                        }
                        else
                        {

                            val intent = Intent(this, ImageUploadFrgnd::class.java)

                            intent.putExtra("intentType","UploadImage")
                            intent.putExtra("FileUri", ProfileUri.toString())
                            intent.putExtra("tempId",template!!.id.toString())
                            intent.putExtra("headerType",template!!.hederType)



                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

                                startForegroundService(intent)

                            } else {

                                startService(intent)
                            }
                            Toast.makeText(this, "Updating WhatsApp Template may take a long time. Please wait for some time. Check after some time template is updated successfully or not.",Toast.LENGTH_LONG).show()
                            val intents  = Intent(this,AdminDashboard::class.java)
                            startActivity(intents)
                            this.finish()

                        }


                }
                else {
                    template!!.tempMsg = binding.txtMsg.text.toString()
                    candidateViewModel.whatApiTemplateUpdate(template!!)
                    Toast.makeText(this, "Updating WhatsApp Template may take a long time. Please wait for some time. Check after some time template is updated successfully or not.",Toast.LENGTH_LONG).show()
                    val intent  = Intent(this,AdminDashboard::class.java)
                    startActivity(intent)
                    this.finish()
                }

            }
        }
    }
}