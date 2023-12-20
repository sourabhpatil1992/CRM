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
import android.widget.EditText
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
import com.venter.crm.databinding.LayoutCustdeteditBinding
import com.venter.crm.models.CommentConf
import com.venter.crm.models.CustUpdateDet
import com.venter.crm.models.RawCandidateData
import com.venter.crm.models.RawCommentData
import com.venter.crm.models.SpinnerItem
import com.venter.crm.models.UserList
import com.venter.crm.models.WhatsTempNameList
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

    private var tempList: ArrayList<WhatsTempNameList> = ArrayList()
    private var commentConf: CommentConf? = null


    var data: RawCandidateData? = null

    @Inject
    lateinit var tokenManger: TokenManger

    private var selectedTemplate: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRawDataDetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTransfer.visibility = if (tokenManger.getUserType() != "Admin")
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

                getTemplateList()


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

        }

        binding.editButton.setOnClickListener {
            //editAction()
            editCustDet()
        }

        binding.editTransfer.setOnClickListener {
            dataTransfer()
        }

    }

    private fun getTemplateList() {
        candidateViewModel.getWhatsMsgNameList()
        candidateViewModel.getCommentConfig()
        tempList = ArrayList()

        candidateViewModel.commentConfDataLiveData.observe(this)
        {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    commentConf = it.data
                    tempList = it.data!!.whatsTemp as ArrayList<WhatsTempNameList>
                    // tempList.add(0, WhatsTempNameList(0, "NA"))

                }
            }
        }
        /*candidateViewModel.msgNameListResLiveData.observe(this)
        {
            when (it) {
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> {}
                is NetworkResult.Success -> {
                    tempList = (it.data as ArrayList<WhatsTempNameList>?)!!
                    // tempList.add(0, WhatsTempNameList(0, "NA"))

                }
            }
        }*/
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
        builders.setTitle("Lead Transfer")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(20, 20, 20, 20)
        layout.addView(empSpinner)

        builders.setPositiveButton("Submit") { _, _ ->


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
            builders.setTitle("Customer Details")
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

    private fun editCustDet() {
        try {
            val bindingMsg = LayoutCustdeteditBinding.inflate(layoutInflater)

            val builder = AlertDialog.Builder(this)

            bindingMsg.custName.setText(data!!.candidate_name)
            bindingMsg.email.setText(data!!.emailId)
            bindingMsg.alterMob.setText(data!!.altenate_mobno)
            if (data!!.trader == 1) {
                bindingMsg.rdoYes.isChecked = true
                bindingMsg.linTrade.visibility = View.VISIBLE
            } else {
                bindingMsg.rdoNo.isChecked = true
                bindingMsg.linTrade.visibility = View.GONE
            }
            bindingMsg.capital.setText(data!!.capital.toString())
            if (!data!!.segment.isNullOrEmpty()) {
                val segment = data!!.segment!!.split(",")
                if (segment.contains("F&O"))
                    bindingMsg.chkFo.isChecked = true
                if (segment.contains("Cash"))
                    bindingMsg.chkCash.isChecked = true
                if (segment.contains("Commodity"))
                    bindingMsg.chkCommodity.isChecked = true

            }




            builder.setView(bindingMsg.root)
            builder.setTitle("Customer Details")
            builder.setIcon(
                ContextCompat.getDrawable(
                    this,
                    com.venter.crm.R.drawable.crm
                )
            )

            bindingMsg.rdoTrade.setOnCheckedChangeListener { _, _ ->

                bindingMsg.linTrade.visibility = if (bindingMsg.rdoYes.isChecked)
                    View.VISIBLE
                else
                    View.GONE
            }





            builder.setPositiveButton("OK") { _, _ ->

                if (bindingMsg.email.text.isNotEmpty())
                    if (!emailValidation(bindingMsg.email.text.toString())) {
                        Toast.makeText(this, "Please Enter Valid Email Id.", Toast.LENGTH_SHORT)
                            .show()
                        return@setPositiveButton
                    }
                val trader = if (bindingMsg.rdoYes.isChecked)
                    1
                else
                    0
                val segment: ArrayList<String> = ArrayList()
                if (bindingMsg.chkFo.isChecked)
                    segment.add("F&O")
                if (bindingMsg.chkCash.isChecked)
                    segment.add("Cash")
                if (bindingMsg.chkCommodity.isChecked)
                    segment.add("Commodity")
                candidateViewModel.updateCustumerData(
                    CustUpdateDet(
                        dataId,
                        bindingMsg.custName.text.toString(),
                        bindingMsg.alterMob.text.toString(),
                        bindingMsg.email.text.toString(),
                        bindingMsg.capital.text.toString().toDouble(),
                        trader,
                        segment
                    )
                )

            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                // Handle Cancel button click if needed
            }
            val alertDialog = builder.create()

            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataDetActivity.kt editCustDet() is: ${e.message}")
        }

    }

    private fun emailValidation(emailId: String): Boolean {

        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
        return emailPattern.matches(emailId)

    }


    private fun observerRes() {
        candidateViewModel.stringResData.observe(this) {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> {
                    try {
                        Toast.makeText(
                            this,
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in RawDataDetActivity.kt observerRes() is : ${e.message}")
                    }
                }

                is NetworkResult.Success -> {
                    Toast.makeText(this, "Data Update Successfully.", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
            }
        }
    }

    private fun setComment() {
        try {
            var callTime = getTime()
            val bindingMsg = LayoutCommentBinding.inflate(layoutInflater)
            bindingMsg.txtCallTime.text = "${callTime} Sec"


            // val prosType: Array<String> = arrayOf("Not Interested", "Interested", "Not Responding", "Paid")
            val prosType: ArrayList<String> =
                commentConf!!.prosType.toString().split("\n") as ArrayList<String>

            if (!prosType.contains("Not Interested"))
                prosType.add(0, "Not Interested")
            else {
                // If "Not Interested" is already in the list, move it to the first position
                prosType.remove("Not Interested")
                prosType.add(0, "Not Interested")
            }

            prosType.remove("Not Responding")
            prosType.add("Not Responding")

            prosType.remove("Paid")
            prosType.add("Paid")


            var adapter = ArrayAdapter<String>(
                this,
                R.layout.select_dialog_item, // Custom layout for Spinner items
                prosType
            )

            bindingMsg.spinProsType.adapter = adapter

            /*val prospectLevel: Array<String> = arrayOf(
                "NA",
                "Coming for visit",
                "Visited",
                "Demo",
                "Not Interested",
                "Information on call",
                "Will Join/Inform",
                "Paid"
            )
            Log.d(TAG,commentConf!!.prosSubType.toString())*/

            val prospectLevel: ArrayList<String> = commentConf!!.prosSubType?.map {
                it.subType
            } as ArrayList<String>
            /*val prospectLevel: ArrayList<String> =
                commentConf!!.prosSubType.toString().split("\n") as ArrayList<String>*/

            prospectLevel.remove("NA")
            prospectLevel.add(0, "NA")


            prospectLevel.remove("Paid")
            prospectLevel.add("Paid")

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
                        if (bindingMsg.spinProsType.selectedItemPosition != 0) {
                            bindingMsg.linProsSubType.visibility = View.VISIBLE
                            if (bindingMsg.spinProsType.selectedItemPosition == 3) {
                                bindingMsg.linAmount.visibility = View.VISIBLE
                            } else {
                                bindingMsg.linAmount.visibility = View.GONE
                            }
                        } else {
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

            val remarkTemp: ArrayList<String> =
                commentConf!!.commentTemp.toString().split("\n") as ArrayList<String>

            val remarkAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, remarkTemp)
            bindingMsg.remark.setAdapter(remarkAdapter)



            val tempNames = tempList.mapNotNull { it.temp_name }

            val whatsTemp: ArrayList<String> = ArrayList(tempNames)


            val items = ArrayList<SpinnerItem>()
            whatsTemp.forEach {
                items.add(SpinnerItem(it, false))
            }
            val selectedItems = BooleanArray(whatsTemp.size)
            var selectedItemsList = mutableListOf<String>()
            var selectedTempList = ArrayList<Int>()
            bindingMsg.temp.setOnClickListener {

                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Select WhatsApp Template")
                dialog.setMultiChoiceItems(
                    whatsTemp.toTypedArray(),
                    selectedItems
                ) { _, which, isChecked ->

                    selectedItems[which] = isChecked
                }
                dialog.setPositiveButton("OK") { _, _ ->

                    selectedItemsList = mutableListOf<String>()
                    selectedTempList = ArrayList()
                    for (i in whatsTemp.indices) {
                        if (selectedItems[i]) {
                            selectedItemsList.add(whatsTemp[i])
                            tempList.forEach {
                                if (it.temp_name == whatsTemp[i])
                                    selectedTempList.add(it.id)
                            }
                        }
                    }
                    if (selectedItemsList.isEmpty())
                        bindingMsg.temp.text = "[NA]"
                    else {
                        bindingMsg.temp.text = selectedItemsList.toString()

                    }


                }
                dialog.setNegativeButton("Cancel") { _, _ ->
                    // Handle the Cancel button click if needed
                }
                // if(tempList.isNotEmpty())
                dialog.show()
            }




            bindingMsg.rdoGrp.setOnCheckedChangeListener { group, checkedId ->
                if (bindingMsg.rdoYes.isChecked) {
                    bindingMsg.linFollowUpDate.visibility = View.VISIBLE
                    bindingMsg.followUpDate.text.clear()
                } else {
                    bindingMsg.linFollowUpDate.visibility = View.GONE
                    bindingMsg.followUpDate.setText("01-01-1900")
                }
            }

            bindingMsg.followUp.setOnClickListener {
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
                try {
                    if (bindingMsg.spinProsType.selectedItem.toString() == "Not Responding")
                        callTime = "30"
                    val update =
                        if (data!!.prospect_type != bindingMsg.spinProsType.selectedItem.toString())
                            1
                        else
                            0


                    var amount =
                        if (bindingMsg.amt.text.isNullOrEmpty())
                            0
                        else
                            bindingMsg.amt.text.toString().toInt()


                    submitData(
                        callTime,
                        bindingMsg.spinProsType.selectedItem.toString(),
                        bindingMsg.remark.text.toString(),
                        bindingMsg.followUpDate.text.toString(),
                        selectedTempList,
                        update,
                        data!!.mob_no.toString(),
                        data!!.altenate_mobno.toString(),
                        bindingMsg.spinProsLevel.selectedItem.toString(),
                        amount
                    )
                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "Error in RawDataDetActivity.kt setComment() alertDo=ialog() is : ${e.message}"
                    )
                }
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
        selectedItem: ArrayList<Int>,
        update: Int, mobNo: String, alternateMob: String, prosLevel: String, amount: Int
    ) {
        try {
            val commentData = RawCommentData(
                callTime,
                prosType,
                remark,
                folloupDate,
                selectedItem,
                update,
                mobNo,
                alternateMob,
                prosLevel,
                amount,
                dataId
            )
            //Log.d(TAG, commentData.toString())
            candidateViewModel.setEmpRawDataComment(commentData)
            /*callTime,
            prosType,
            remark,
            folloupDate,
            selectedItem,
            dataId.toString(), update, mobNo, alternateMob, prosLevel
        )*/
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

            val tempNames = tempList.mapNotNull { it.temp_name }
            val whatsTemp: ArrayList<String> = ArrayList(tempNames)
            whatsTemp.add("Chat")

            whatsTemp.forEach {
                popupMenu.menu.add(it)
            }


            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                try {
                    when (item.title) {
                        "Chat" -> {
                            var mob = mobNo
                            if (mobNo.length <= 10)
                                mob = "91$mobNo"
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data =
                                Uri.parse("http://api.whatsapp.com/send?phone=$mob&text=hi")
                            startActivity(intent)
                        }

                        "NA" -> {
                            Log.d(TAG, item.title.toString())
                        }

                        else -> {
                            if (tempList.isNotEmpty())
                                tempList.forEach {
                                    if (it.temp_name == item.title)
                                        sendWhatsMsg(it.id, data!!.mob_no)
                                }
                        }
                    }
                    true
                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "Error in RawDataDetActivity.kt showPopupMenu() setOnMenuItemClickListener() is : ${e.message}"
                    )
                    false
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