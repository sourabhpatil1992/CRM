package com.venter.regodigital.EmployeeMangment

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.regodigital.databinding.ActivityRawDataDetBinding
import com.venter.regodigital.models.RawCandidateData
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class RawDataDetActivity : AppCompatActivity() {
    private var _binding: ActivityRawDataDetBinding? = null
    private val binding: ActivityRawDataDetBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var dataId = 0

    var checkCall = false

    var data: RawCandidateData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRawDataDetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataId = intent.getIntExtra("rawDataId", 0)

        try {

            if (dataId != 0) {
                candidateViewModel.getRawCandidateData(dataId)
                candidateViewModel.rawCandidateDataLiveData.observe(this) {
                    binding.progressbar.visibility = View.GONE
                    when (it) {
                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> Toast.makeText(
                            this,
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        is NetworkResult.Success -> {
                            setView(it.data)

                        }
                    }
                }


            } else {
                Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt is " + e.message)
        }

        binding.btnCall.setOnClickListener {
            try {
                if (data != null) {
                    // Log.d(TAG, data!!.mob_no)

                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" + data!!.mob_no)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CALL_PHONE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.CALL_PHONE),
                            500
                        )

                        // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    } else {
                        //You already have permission
                        try {
                            startActivity(callIntent)
                        } catch (e: SecurityException) {
                            e.printStackTrace()
                        }
                    }

                   // Thread.sleep(2000)
                }
            } catch (e: Exception) {
                Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error in RawDataDetActivity.kt binding.btnCall is  " + e.message)
            }


        }

        binding.btnCommnet.setOnClickListener {
            setComment()
        }

    }

    private fun setComment() {
        try {
            //Get Call Time
            var callTime = "0"
            val managedCursor: Cursor? =
                this.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
            val number = managedCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val type = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
            val sim_id = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)
            val duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION)


            if (android.os.Build.BRAND == "samsung")
                managedCursor.moveToFirst()
            else
                managedCursor.moveToLast()

            var candNo = ""
            if (managedCursor.getString(number).isNotEmpty()) {

                candNo = managedCursor.getString(number).replace("+91", "")
                if (data!!.mob_no.contains(candNo)) {
                    callTime = managedCursor.getInt(duration).toString()
                }
            }


            //Message Box After Click on Comment Button
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Candidate Comment")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(20, 20, 20, 20)

            //Call Type
            val callTimeText = TextView(this)
            callTimeText.text = "Call Time"
            callTimeText.textSize = 20F
            callTimeText.setTextColor(getColor(R.color.black))
            layout.addView(callTimeText)
            val callTText = TextView(this)
            callTText.text = callTime.toString() + " Sec"
            callTText.textSize = 20F
            callTText.setTextColor(getColor(R.color.black))
            layout.addView(callTText)

            //Prospect Type
            val prospectText = TextView(this)
            prospectText.text = "Prospect Type"
            prospectText.textSize = 20F
            prospectText.setTextColor(getColor(R.color.black))
            layout.addView(prospectText)

            val prosType = arrayOf("Warm", "Hot", "Cold","Not Responding", "Admission")
            val prospectSpinner = Spinner(this)
            val adapters = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item,
                prosType
            )
            prospectSpinner.adapter = adapters
            layout.addView(prospectSpinner)


            val commentList = arrayOf(
                "Not Responding","Call Busy","Out of Coverage area","Switch off","Invalid Number","Not Interested","Not ready to pay expecting salary",
                "Interested for demo.\nNeed a Reminder call for Demo.",
                "He/She will discuss with family and let you knows.",
                "Interested Ready to visit the office location",
                "Will join from ",
                "Shared location and details",
                "Traveling,Need to call back after some time. ",
                "Interested for only placement not for course"
            )


            //Remark
            val RemarkText = TextView(this)
            RemarkText.text = "Remark"
            RemarkText.textSize = 20F
            RemarkText.setTextColor(getColor(R.color.black))
            layout.addView(RemarkText)
            val RemarkEditText= AutoCompleteTextView (this)
            RemarkEditText.setHint("Remark")
            RemarkEditText.textSize = 20F
            RemarkEditText.setTextColor(getColor(R.color.black))
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, commentList)
            RemarkEditText.threshold = 1
            RemarkEditText.setAdapter(adapter)
            layout.addView(RemarkEditText)

            //Next Follow-up Date

            val letterDateText = TextView(this)
            letterDateText.textSize = 20F
            letterDateText.setTextColor(resources.getColor(com.venter.regodigital.R.color.black))
            letterDateText.setText("Follow-up Date")

            //For Folloup Date we are putting the Date Edit Text and and calnder Button for the Calder View
            val linDate = LinearLayout(this)
            linDate.orientation = LinearLayout.HORIZONTAL
            val letterDate = EditText(this)
            letterDate.textSize = 20F
            letterDate.inputType = 20
            letterDate.setHint("DD-MM-YYYY")
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            linDate.addView(letterDate)

            val calView = ImageButton(this)
            calView.setBackgroundResource(R.drawable.ic_menu_my_calendar);

            linDate.addView(calView)

            layout.addView(letterDateText)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linDate.setLayoutParams(layoutParams)

            layout.addView(linDate)
            linDate.setOnClickListener {
                setDate(letterDate)
            }
            letterDate.setOnClickListener {
                setDate(letterDate)
            }
            letterDateText.setOnClickListener {
                setDate(letterDate)
            }
            calView.setOnClickListener {
                setDate(letterDate)
            }

            //Marketing Template
            val marketingText = TextView(this)
            marketingText.text = "WhatsApp Template"
            marketingText.textSize = 20F
            marketingText.setTextColor(getColor(R.color.black))
            layout.addView(marketingText)

            val tempType = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "0")
            val tempSpinner = Spinner(this)
            val adapterss = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item,
                tempType
            )
            tempSpinner.adapter = adapterss
            layout.addView(tempSpinner)


            builders.setPositiveButton("Submit") { dialogInterface, which ->
                //Set Validation
                try {
                    if (RemarkEditText.text.isNotEmpty()) {

                        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                        dateFormat.parse(letterDate.text.toString())

                        val update =
                           if (data!!.prospect_type != prospectSpinner.selectedItem.toString())
                        1
                        else
                            0
                        if ( prospectSpinner.selectedItem.toString() == "Not Responding")
                            callTime = "15"
                        submitData(
                            callTime,
                            prospectSpinner.selectedItem.toString(),
                            RemarkEditText.text.toString(),
                            letterDate.text.toString(),
                            tempSpinner.selectedItem.toString(),update
                        )
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Please fill date in DD-MM-YYYY format",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }

            builders.setView(layout)
            builders.setIcon(
                ContextCompat.getDrawable(
                    this,
                    com.venter.regodigital.R.drawable.regologo
                )
            )
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()


        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt setComment() is " + e.message)
        }


    }

    private fun submitData(
        callTime: String,
        prosType: String,
        remark: String,
        folloupDate: String,
        selectedItem: String,
        update: Int
    ) {
        try {
            candidateViewModel.setEmpRawDataComment(
                callTime,
                prosType,
                remark,
                folloupDate,
                selectedItem,
                dataId.toString(),update
            )
            candidateViewModel.stringResData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility =
                        View.VISIBLE

                    is NetworkResult.Error -> Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        Toast.makeText(
                            this,
                            "Comment Submitted successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
//                        val intent = Intent(this, RawDataDetActivity::class.java)
//                        startActivity(intent)
//                        this.finishAffinity()
                        this.finish()
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt submitData() is " + e.message)
        }

    }


    private fun setView(cdata: RawCandidateData?) {
        data = cdata
        binding.viewPager.adapter = CandDetViewPagerAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setDate(letterDate: TextView) {
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilder.setTitleText("SELECT A DATE")


        val materialDatePicker = materialDateBuilder.build()


        materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

        materialDatePicker.addOnPositiveButtonClickListener {

            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it as Long
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

            val formatted = format.format(utc.time).toString()
            letterDate.setText(formatted)

            //candidateViewModel.getEmpReport(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString())
        }
    }
}