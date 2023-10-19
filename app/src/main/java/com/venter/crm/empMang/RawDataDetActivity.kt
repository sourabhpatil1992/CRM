package com.venter.crm.empMang

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
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.crm.databinding.ActivityRawDataDetBinding
import com.venter.crm.databinding.LayoutCommentBinding
import com.venter.crm.models.RawCandidateData
import com.venter.crm.models.UserList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@AndroidEntryPoint
class RawDataDetActivity : AppCompatActivity() {
    private var _binding: ActivityRawDataDetBinding? = null
    private val binding: ActivityRawDataDetBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var dataId = 0


    var data: RawCandidateData? = null

    @Inject
    lateinit var tokenManger: TokenManger


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRawDataDetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTransfer.visibility = if (tokenManger.getUserType() != "Employee")
            View.VISIBLE
        else
            View.GONE

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
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error in RawDataDetActivity.kt binding.btnCall is  " + e.message)
            }


        }

        binding.btnCommnet.setOnClickListener {
            setComment()
        }
        binding.whatsButton.setOnClickListener {

            var mob = data!!.mob_no.replace("+", "")
            if (mob.length > 10) {
                mob = mob.drop(2)
            }

            showPopupMenu(it, mob)
            /*else if (mob.length <= 10)
                mob = "91$mob"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("http://api.whatsapp.com/send?phone=$mob&text=hi")
            startActivity(intent)*/
        }

        binding.editButton.setOnClickListener {
            editAction()
        }

        binding.editTransfer.setOnClickListener {
            dataTransfer()
        }

    }

    private fun dataTransfer() {
        try {
            candidateViewModel.getEmpListRawData()
            candidateViewModel.userListLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }

                    is NetworkResult.Error -> Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        setTransferAlertBox(it.data!!)

                    }
                }

            }
            binding.progressbar.visibility
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt dataTransfer() is ${e.message}")
        }
    }

    private fun setTransferAlertBox(user: List<UserList>) {

        // Create Alert Box for the transfer candidate from one emp to other by Tl or admin
        val empList: List<String> = user.map { it.user_name }


        val empSpinner = Spinner(this)

        val adapters = ArrayAdapter<String>(
            this,
            R.layout.select_dialog_item,
            empList
        )
        empSpinner.adapter = adapters
        val builders = AlertDialog.Builder(this)
        builders.setTitle("Candidate Comment")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(20, 20, 20, 20)
        layout.addView(empSpinner)

        builders.setPositiveButton("Submit") { _, _ ->
            Log.d(TAG, user[empSpinner.selectedItemId.toInt()].toString())

            if (dataId != 0) {
                candidateViewModel.swipeData(dataId, user[empSpinner.selectedItemId.toInt()].id)
                observerRes()
            }


        }
        builders.setNegativeButton("Cancel") { _, _ ->

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


    }

    private fun editAction() {
        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Candidate Comment")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(20, 20, 20, 20)

            val txtMobNo = TextView(this)
            val edtMobNo = EditText(this)
            edtMobNo.setTextColor(resources.getColor(com.venter.crm.R.color.black))
            txtMobNo.setTextColor(resources.getColor(com.venter.crm.R.color.black))
            edtMobNo.filters = arrayOf(InputFilter.LengthFilter(10))
            edtMobNo.inputType = 2
            txtMobNo.text = "Mobile No"
            edtMobNo.setText(data!!.mob_no)
            layout.addView(txtMobNo)
            layout.addView(edtMobNo)

            val alterTxtMobNo = TextView(this)
            val alterEdtMobNo = EditText(this)
            alterEdtMobNo.setTextColor(resources.getColor(com.venter.crm.R.color.black))
            alterTxtMobNo.setTextColor(resources.getColor(com.venter.crm.R.color.black))
            alterEdtMobNo.filters = arrayOf(InputFilter.LengthFilter(10))
            alterEdtMobNo.inputType = 2
            alterTxtMobNo.text = "Alternate Mobile No"
            alterEdtMobNo.hint = "Alternate Mobile No"
            if (data!!.altenate_mobno != null)
                alterEdtMobNo.setText(data!!.altenate_mobno)
            layout.addView(alterTxtMobNo)
            layout.addView(alterEdtMobNo)



            builders.setPositiveButton("Submit") { dialogInterface, which ->

                if (edtMobNo.text.length == 10) {
                    candidateViewModel.updateRawCandidateData(
                        dataId,
                        edtMobNo.text.toString(),
                        alterEdtMobNo.text.toString()
                    )
                    observerRes()

                } else
                    Toast.makeText(this, "Please insert mobile no correctly.", Toast.LENGTH_SHORT)
                        .show()

            }
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

        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt editAction() is " + e.message)
        }
    }

    private fun observerRes() {
        candidateViewModel.stringResData.observe(this) {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(
                    this,
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                is NetworkResult.Success -> {
                    Toast.makeText(this, "Data Update Successfully.", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
            }
        }
    }

    private fun setCommentOrigin() {
        try {

            //Get The Call Time
            var callTime = getTime()

            //Message Box After Click on Comment Button
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Data Comment")

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(20, 20, 20, 20)

            /****************************************************/
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
            /*****************************************************/


            /******************************************************/
            //Prospect Type
            val prospectText = TextView(this)
            prospectText.text = "Prospect Type"
            prospectText.textSize = 20F
            prospectText.setTextColor(getColor(R.color.black))
            layout.addView(prospectText)
            val prosType: Array<String> =
                arrayOf("Not Interested", "Interested", "Not Responding", "Paid")

            val prospectSpinner = Spinner(this)
            val adapters = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item,
                prosType
            )
            prospectSpinner.adapter = adapters
            layout.addView(prospectSpinner)
            /*****************************************************************/

            /********************************************************/
            //Prospect Level
            val prospectSubTypeLayout = LinearLayout(this)
            prospectSubTypeLayout.orientation = LinearLayout.VERTICAL

            val prospectLevelText = TextView(this)
            prospectLevelText.text = "Prospect Sub-Type"
            prospectLevelText.textSize = 20F
            prospectLevelText.setTextColor(getColor(R.color.black))
            prospectSubTypeLayout.addView(prospectLevelText)
            val prospectLevel: Array<String> = arrayOf(
                "NA",
                "Coming for visit",
                "Visited",
                "Demo",
                "Not Interested",
                "Information on call",
                "Will Join/Inform"
            )
            val prospectLevelSpinner = Spinner(this)
            val prosAdapters = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item,
                prospectLevel
            )
            prospectLevelSpinner.adapter = prosAdapters
            prospectSubTypeLayout.addView(prospectLevelSpinner)
            prospectSubTypeLayout.visibility = View.GONE
            layout.addView(prospectSubTypeLayout)
            /************************************************************/


            /***********************************************************/
            //Comment

            val remarkLayout = LinearLayout(this)
            remarkLayout.orientation = LinearLayout.VERTICAL

            val commentList = arrayOf(
                "Not Responding",
                "Call Busy",
                "Out of Coverage area",
                "Switch off",
                "Invalid Number",
                "Not Interested",
                "Not ready to pay expecting salary",
                "Interested for demo.\nNeed a Reminder call for Demo.",
                "He/She will discuss with family and let you knows.",
                "Interested Ready to visit the office location",
                "Will join from ",
                "Shared location and details",
                "Traveling,Need to call back after some time. ",
                "Interested for only placement not for course"
            )

            val RemarkText = TextView(this)
            RemarkText.text = "Remark"
            RemarkText.textSize = 20F
            RemarkText.setTextColor(getColor(R.color.black))
            remarkLayout.addView(RemarkText)
            val RemarkEditText = AutoCompleteTextView(this)
            RemarkEditText.setHint("Remark")
            RemarkEditText.textSize = 20F
            RemarkEditText.setTextColor(getColor(R.color.black))
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, commentList)
            RemarkEditText.threshold = 1
            RemarkEditText.setAdapter(adapter)
            remarkLayout.addView(RemarkEditText)
            remarkLayout.visibility = View.GONE
            layout.addView(remarkLayout)
            /************************************************************************/
            //Need An Follow Up Message
            val folloupMsgLayout = LinearLayout(this)
            folloupMsgLayout.orientation = LinearLayout.VERTICAL
            val folloupMsgText = TextView(this)
            folloupMsgText.textSize = 20F
            folloupMsgText.setTextColor(resources.getColor(com.venter.crm.R.color.black))
            folloupMsgText.setText("Required Follow Up?")
            folloupMsgLayout.addView(folloupMsgLayout)

            layout.addView(folloupMsgLayout)
            /***************************************************************************/
            //Next Follow-up Date

            val folloupLayout = LinearLayout(this)
            folloupLayout.orientation = LinearLayout.VERTICAL
            val letterDateText = TextView(this)
            letterDateText.textSize = 20F
            letterDateText.setTextColor(resources.getColor(com.venter.crm.R.color.black))
            letterDateText.setText("Follow-up Date")

            //For Folloup Date we are putting the Date Edit Text and and calnder Button for the Calder View
            val linDate = LinearLayout(this)
            linDate.orientation = LinearLayout.HORIZONTAL
            val letterDate = EditText(this)
            letterDate.textSize = 20F
            letterDate.inputType = 20
            letterDate.setHint("DD-MM-YYYY")
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            folloupLayout.addView(letterDate)

            val calView = ImageButton(this)
            calView.setBackgroundResource(R.drawable.ic_menu_my_calendar);

            linDate.addView(calView)

            folloupLayout.addView(letterDateText)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linDate.setLayoutParams(layoutParams)


            folloupLayout.addView(linDate)

            folloupLayout.visibility = View.GONE
            layout.addView(folloupLayout)


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


            val tempType = arrayOf(
                "Null",
                "Template 1",
                "Template 2",
                "Template 3",
                "Template 4",
                "Template 5",
                "Template 6",
                "Template 7",
                "Template 8",
                "Template 9",
                "Template 10",
            )
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
                        if (prospectSpinner.selectedItem.toString() == "Not Responding" || RemarkEditText.text.contains(
                                "Not Responding"
                            )
                        )
                            callTime = "30"

                        submitData(
                            callTime,
                            prospectSpinner.selectedItem.toString(),
                            RemarkEditText.text.toString(),
                            letterDate.text.toString(),
                            tempType.indexOf(tempSpinner.selectedItem.toString()).toString(),
                            update,
                            data!!.mob_no.toString(),
                            data!!.altenate_mobno.toString(),
                            prospectLevelSpinner.selectedItem.toString()
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
                    com.venter.crm.R.drawable.crm
                )
            )
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)

            val lastData = tokenManger.getLastCommentDet()

            if (callTime == lastData["call_time"] && data!!.mob_no == lastData["mob_no"]) {
                if (callTime == "0")
                    alertDialog.show()
                else
                    Toast.makeText(
                        applicationContext,
                        "Comment already submitted on this call.",
                        Toast.LENGTH_SHORT
                    ).show()
            } else
                alertDialog.show()


        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt setComment() is " + e.message)
        }


    }

    private fun setComment() {
        try {
            var callTime = getTime()
            val bindingMsg = LayoutCommentBinding.inflate(layoutInflater)
            bindingMsg.txtCallTime.text = "${callTime} Sec"


            val prosType: Array<String> =
                arrayOf("Not Interested", "Interested", "Not Responding", "Paid")

            var adapter = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item, // Custom layout for Spinner items
                prosType
            )

            bindingMsg.spinProsType.adapter = adapter

            val prospectLevel: Array<String> = arrayOf(
                "NA",
                "Coming for visit",
                "Visited",
                "Demo",
                "Not Interested",
                "Information on call",
                "Will Join/Inform"
            )
            adapter = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item, // Custom layout for Spinner items
                prospectLevel
            )
            bindingMsg.spinProsLevel.adapter = adapter

            bindingMsg.spinProsType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener,
                    AdapterView.OnItemClickListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (bindingMsg.spinProsType.selectedItemPosition != 0)
                        {
                            bindingMsg.linProsSubType.visibility = View.VISIBLE
                        }
                        else
                        {
                            bindingMsg.linProsSubType.visibility = View.GONE
                            bindingMsg.spinProsLevel.setSelection(0)
                        }



                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                    }


                }




            val whatsTemp: Array<String> = arrayOf("NA",
                "Temp 1", "Temp 2", "Temp 3", "Temp 4", "Temp 5", "Temp 7",
                "Temp 8", "Temp 9", "Temp 10"

            )
            adapter = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item, // Custom layout for Spinner items
                whatsTemp
            )
            bindingMsg.spinWhatas.adapter = adapter

            bindingMsg.rdoGrp.setOnCheckedChangeListener { group, checkedId ->
               if(bindingMsg.rdoYes.isChecked)
               {
                   bindingMsg.linFollowUpDate.visibility = View.VISIBLE
                   bindingMsg.followUpDate.text.clear()
               }
                else
                {
                   bindingMsg.linFollowUpDate.visibility = View.GONE
                   bindingMsg.followUpDate.setText("01-01-1900")
               }
            }

            bindingMsg.followUp.setOnClickListener{
                setDate(bindingMsg.followUpDate)
            }
            bindingMsg.followUpDate.setOnClickListener {
                setDate(bindingMsg.followUpDate)
            }


            val builder = AlertDialog.Builder(this)
            builder.setView(bindingMsg.root)
            builder.setTitle("Data Comment")
            builder.setIcon(
                ContextCompat.getDrawable(
                    this,
                    com.venter.crm.R.drawable.crm
                )
            )
            builder.setPositiveButton("OK") { dialog, _ ->
                if (bindingMsg.spinProsType.selectedItem.toString() == "Not Responding")
                    callTime = "30"
                val update =
                    if (data!!.prospect_type != bindingMsg.spinProsType.selectedItem.toString())
                        1
                    else
                        0
                submitData(
                    callTime,
                    bindingMsg.spinProsType.selectedItem.toString(),
                    bindingMsg.remark.text.toString(),
                    bindingMsg.followUpDate.text.toString(),
                    bindingMsg.spinWhatas.selectedItemId.toString(),
                    update,
                    data!!.mob_no.toString(),
                    data!!.altenate_mobno.toString(),
                    bindingMsg.spinProsLevel.selectedItem.toString()
                )
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                // Handle Cancel button click if needed
            }
            val alertDialog = builder.create()
            alertDialog.show()

        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt setComment() is: ${e.message} ")
        }
    }

    private fun getTime(): String {
        try {
            var callTime = "0"
            val managedCursor: Cursor? = this.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
            )


            val number = managedCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val type = managedCursor.getColumnIndex(CallLog.Calls.TYPE)

            val duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION)


            managedCursor.moveToNext()


            while (managedCursor.getString(type) == CallLog.Calls.MISSED_TYPE.toString() || managedCursor.getString(
                    type
                ) == CallLog.Calls.REJECTED_TYPE.toString()
            ) {

                managedCursor.moveToNext()
            }


            var candNo = ""
            if (managedCursor.getString(number).isNotEmpty()) {

                candNo = managedCursor.getString(number).replace("+91", "")

                if (data!!.mob_no.contains(candNo) || data!!.altenate_mobno!!.contains(candNo)) {
                    callTime = managedCursor.getInt(duration).toString()
                }

            }

            return callTime
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt getTime() is : ${e.message}")
            return "0"
        }


    }

    private fun submitData(
        callTime: String,
        prosType: String,
        remark: String,
        folloupDate: String,
        selectedItem: String,
        update: Int, mobNo: String, alternateMob: String, prosLevel: String
    ) {
        try {
            candidateViewModel.setEmpRawDataComment(
                callTime,
                prosType,
                remark,
                folloupDate,
                selectedItem,
                dataId.toString(), update, mobNo, alternateMob, prosLevel
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

                        tokenManger.saveLastCommentDet(mobNo, callTime)
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
        binding.whatsButton.visibility = View.VISIBLE
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

    private fun showPopupMenu(view: View, mobNo: String) {
        try {
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(
                com.venter.crm.R.menu.whats_menu,
                popupMenu.menu
            )
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                // Handle the selected item here
                when (item.itemId) {
                    com.venter.crm.R.id.temp1 -> {
                        sendWhatsMsg(1, mobNo)
                        true
                    }

                    com.venter.crm.R.id.temp2 -> {
                        sendWhatsMsg(2, mobNo)
                        true
                    }

                    com.venter.crm.R.id.temp3 -> {
                        sendWhatsMsg(3, mobNo)
                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp4 -> {
                        sendWhatsMsg(4, mobNo)

                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp5 -> {
                        sendWhatsMsg(5, mobNo)

                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp6 -> {
                        sendWhatsMsg(6, mobNo)

                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp7 -> {
                        sendWhatsMsg(7, mobNo)

                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp8 -> {
                        sendWhatsMsg(8, mobNo)

                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp9 -> {
                        sendWhatsMsg(9, mobNo)

                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp10 -> {
                        sendWhatsMsg(10, mobNo)

                        // Do something when Option 2 is selected
                        true
                    }

                    com.venter.crm.R.id.temp11 -> {
                        var mob = mobNo
                        if (mobNo.length <= 10)
                            mob = "91$mobNo"
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data =
                            Uri.parse("http://api.whatsapp.com/send?phone=$mob&text=hi")
                        startActivity(intent)
                        // Do something when Option 2 is selected
                        true
                    }
                    // Add more cases for other options as needed
                    else -> false
                }
            }
            popupMenu.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt showPopupMenu() ${e.message}")
        }
    }

    private fun sendWhatsMsg(temNo: Int, mobNo: String) {
        try {

            candidateViewModel.sendWhatsMsg(temNo, mobNo)
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt sendWhatsMsg() ${e.message}")
        }
    }

}