package com.venter.regodigital.Candidate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.regodigital.R
import com.venter.regodigital.databinding.ActivityCandidateCertificateBinding

import com.venter.regodigital.models.CanCertDetRes
import com.venter.regodigital.utils.Constans.BASE_URL
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat


@AndroidEntryPoint
class CandidateCertificate : AppCompatActivity() {
    private var _binding: ActivityCandidateCertificateBinding? = null
    private val binding: ActivityCandidateCertificateBinding
        get() = _binding!!
    private var candidateId: Int = 0

    private val candidateViewModel by viewModels<CandidateViewModel>()
    private lateinit var data: CanCertDetRes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCandidateCertificateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        candidateId = intent.getIntExtra("id", 0)




        if (candidateId != 0) {
            candidateViewModel.candidateCertDet(candidateId)
            candidateViewModel.candidateCertDetResData.observe(this) {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                        .show()
                    is NetworkResult.Success -> {
                        data = it.data!!
                        setView()

                    }
                }
            }


        } else
            Toast.makeText(this, "Please contact System Administrator.", Toast.LENGTH_SHORT).show()
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, CandidateDet::class.java)
            intent.putExtra("id",candidateId.toString())
            startActivity(intent)
        }

        binding.btnDoc.setOnClickListener {
            val intent = Intent(this, CandidateDoc::class.java)
            intent.putExtra("Doc",data)
            intent.putExtra("id",candidateId.toString())
            startActivity(intent)
        }

    }

    private fun setView() {
        try {

            binding.lincandidateView.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnDoc.visibility = View.VISIBLE
            binding.txtCandidateName.text =
                "${data.first_name} ${data.middel_name} ${data.last_name}"
            binding.txtMobNo.text = "+91-${data.mobile_no}"
            binding.txtEmailId.text = data.email_id
            binding.txtAddress.text = data.address
            binding.txtDegree.text = data.edu_degree
            binding.txtPassYear.text = data.passing_year
            binding.txtJobPosition.text = data.current_job
            binding.txtFee.text = data.cource_fee.toString()
            binding.txtPaidFee.text = data.paid_fee.toString()
            binding.txtTransFee.text = data.transFee.toString()
            if (data.transFee.toFloat() >0)
                binding.linTransFee.visibility = View.VISIBLE
            Picasso.get()
                .load(BASE_URL + "assets/profile/" + candidateId + ".jpeg")
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(binding.imgProfile)

            binding.imgProfile.setOnClickListener {
                val builders = AlertDialog.Builder(this)
                builders.setTitle("Candidate Profile")
                val img_per = ImageView(this)
                img_per.setBackgroundResource(R.drawable.regologo)
                Picasso.get()
                    .load(BASE_URL + "assets/profile/" + candidateId + ".jpeg")
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(img_per)
                builders.setView(img_per)
                builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
                val alertDialog: AlertDialog = builders.create()
                alertDialog.setCancelable(true)
                alertDialog.show()


            }


            button_ops()
        }catch (e:Exception)
        {
            Log.d(TAG,"Error in CandidateCertificate.kt setView() is "+e.message)
        }


    }

    private fun button_ops() {
        binding.btnCandidateProfile.setOnClickListener {
            candidateViewModel.printCandidateProfile(candidateId)
            serverRes("Profile")
        }
        binding.btnOffer.setOnClickListener {
            offerLetter()
        }
        binding.btnIncrement.setOnClickListener {
            incrementLetter()
        }

        binding.btnExperience.setOnClickListener {
            experienceLetter()
        }

        binding.btnReleveing.setOnClickListener {
            relievingLetter()
        }

        binding.btnSalary.setOnClickListener {
            salarySlip()
        }

        binding.btnInternship.setOnClickListener {
            internshipLetter()
        }

        binding.btnInternshipCert.setOnClickListener {
            internshipCertLetter()
        }

        binding.btnidcard.setOnClickListener {
            printIdCard()
        }
    }

    private fun offerLetter() {
        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Offer Letter")

            val layout = LinearLayout(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
           params.setMargins(40, 40, 40, 40)
            layout.setLayoutParams(params)
            layout.orientation = LinearLayout.VERTICAL

            val letterDateText = TextView(this)
            letterDateText.setTextColor(resources.getColor(R.color.black))
            val letterDate = EditText(this)
            letterDate.inputType = 20
            val outWard = EditText(this)

            letterDateText.setText("Letter Date")
            letterDate.setHint("Letter Date")


            val outwardText = TextView(this)
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            outWard.setHint("Outward Index")
            outwardText.setText("Outward Index")
            outwardText.setTextColor(resources.getColor(R.color.black))

            outWard.filters = arrayOf(InputFilter.LengthFilter(20))

            val rdoGrp: RadioGroup = RadioGroup(this)
            val withStamp: RadioButton = RadioButton(this)
            withStamp.setText("With Stamp")
            //withStamp.isChecked = true
            val withoutStamp: RadioButton = RadioButton(this)
            withoutStamp.setText("Without Stamp")
            rdoGrp.addView(withStamp)
            rdoGrp.addView(withoutStamp)

            if (data.offerletter_date != null && data.offerletter_date != "" && data.offerletter_date != "null")
                letterDate.setText(data.offerletter_date)

            if (data.offerletter_outward != null && data.offerletter_outward != "" && data.offerletter_outward != "null")
                outWard.setText(data.offerletter_outward)



            layout.addView(letterDateText)
            layout.setPadding(40, 10, 40, 0);
            layout.addView(letterDate)
            layout.setPadding(60, 0, 60, 0);
            layout.addView(outwardText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(outWard)
            layout.setPadding(60, 0, 60, 0);
            layout.addView(rdoGrp)
            layout.setPadding(60, 0, 60, 0);

            builders.setView(layout)
            withStamp.isChecked = true

            builders.setPositiveButton("Create") { dialogInterface, which ->
                if (letterDate.text.toString().length == 10 && outWard.text.isNotEmpty()) {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    try {
                        dateFormat.parse(letterDate.text.toString())
                        candidateViewModel.printOfferLetter(
                            candidateId, outWard.text.toString(),
                            letterDate.text.toString(), withStamp.isChecked
                        )
                        serverRes("Offer")
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in CandidateCertificate.kt offerLetter() is " + e.message)
                        Toast.makeText(
                            this,
                            "Please add data in DD-MM-YYYY format.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else
                    Toast.makeText(
                        this,
                        "Please add data in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }


            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt offerLetter() is " + e.message)
        }
    }

    private fun incrementLetter() {
        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Hike Letter")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val letterDate = EditText(this)
            val letterDateText = TextView(this)
            letterDate.inputType = 20
            letterDate.setHint("Letter Date")
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            letterDateText.setTextColor(resources.getColor(R.color.black))
            letterDateText.setText("Letter Date")

            val effectDate = EditText(this)
            val effectDateText = TextView(this)
            effectDate.inputType = 20
            effectDate.setHint("Effective Date")
            effectDate.filters = arrayOf(InputFilter.LengthFilter(10))
            effectDateText.setTextColor(resources.getColor(R.color.black))
            effectDateText.setText("Effective Date")

            val jobPosition = EditText(this)
            val jobPositionText = TextView(this)
            jobPosition.setHint("New Job Position")
            jobPosition.filters = arrayOf(InputFilter.LengthFilter(30))
            jobPositionText.setTextColor(resources.getColor(R.color.black))
            jobPositionText.setText("New Job Position")

            val newPackage = EditText(this)
            val newPackageText = TextView(this)
            newPackage.setHint("New Package")
            newPackage.filters = arrayOf(InputFilter.LengthFilter(30))
            newPackage.inputType = 2
            newPackageText.setTextColor(resources.getColor(R.color.black))
            newPackageText.setText("New Package")

            val rdoGrp: RadioGroup = RadioGroup(this)
            val withStamp: RadioButton = RadioButton(this)
            withStamp.setText("With Stamp")

            val withoutStamp: RadioButton = RadioButton(this)
            withoutStamp.setText("Without Stamp")
            rdoGrp.addView(withStamp)
            rdoGrp.addView(withoutStamp)


            if (data.hikeletter_date != null && data.hikeletter_date != "" && data.hikeletter_date != "null") {
                letterDate.setText(data.hikeletter_date)
                effectDate.setText(data.effective_date)
                jobPosition.setText(data.newPosition)
                newPackage.setText(data.newPackage)
            }
            layout.setPadding(40, 10, 40, 0);
            layout.addView(letterDateText)
            layout.addView(letterDate)
            layout.addView(effectDateText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(effectDate)
            layout.addView(jobPositionText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(jobPosition)
            layout.addView(newPackageText)
            layout.addView(newPackage)
            layout.addView(rdoGrp)

            withStamp.isChecked = true
            builders.setView(layout)

            builders.setPositiveButton("Create") { dialogInterface, which ->

                if (letterDate.text.toString().length == 10 && effectDate.text.length == 10 && jobPosition.text.isNotEmpty() && newPackage.text.isNotEmpty()) {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    dateFormat.setLenient(false)
                    try {
                        dateFormat.parse(letterDate.text.toString())
                        dateFormat.parse(effectDate.text.toString())
                        candidateViewModel.printHikeLetter(
                            candidateId.toString(),
                            letterDate.text.toString(),
                            effectDate.text.toString(),
                            jobPosition.text.toString(),
                            newPackage.text.toString(),
                            withStamp.isChecked
                        )
                        serverRes("Hike")
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in CandidateCertificate.kt offerLetter() is " + e.message)
                        Toast.makeText(
                            this,
                            "Please add data in DD-MM-YYYY format.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else
                    Toast.makeText(
                        this,
                        "Please add data in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }


            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt incrementLetter() is " + e.message)
        }
    }

    private fun experienceLetter() {

        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Experience Letter")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val letterDate = EditText(this)
            val letterDateText = TextView(this)
            val jobActivityText = TextView(this)
            val jobActivityEdit  = EditText(this)

            letterDate.inputType = 20
            letterDate.setHint("Letter Date")
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            letterDateText.setTextColor(resources.getColor(R.color.black))
            letterDateText.setText("Letter Date")
            jobActivityText.setText("Job Activity")
            jobActivityText.setTextColor(resources.getColor(R.color.black))
            jobActivityEdit.setHint("Job Activity")

            val relaseDate = EditText(this)
            val relaseDateText = TextView(this)
            relaseDate.inputType = 20
            relaseDate.setHint("Release Date")
            relaseDate.filters = arrayOf(InputFilter.LengthFilter(10))
            relaseDateText.setTextColor(resources.getColor(R.color.black))
            relaseDateText.setText("Release Date")


            val rdoGrp: RadioGroup = RadioGroup(this)
            val withStamp: RadioButton = RadioButton(this)
            withStamp.setText("With Stamp")

            val withoutStamp: RadioButton = RadioButton(this)
            withoutStamp.setText("Without Stamp")
            rdoGrp.addView(withStamp)
            rdoGrp.addView(withoutStamp)


            if (data.expletter_date != null && data.expletter_date != "" && data.expletter_date != "null") {
                letterDate.setText(data.expletter_date)
                relaseDate.setText(data.last_date)
                jobActivityEdit.setText(data.jobActivity)
            }

            layout.setPadding(40, 10, 40, 0);
            layout.addView(letterDateText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(letterDate)
            layout.addView(relaseDateText)
            layout.addView(relaseDate)
            layout.addView(jobActivityText)
            layout.addView(jobActivityEdit)

            layout.addView(rdoGrp)

            withStamp.isChecked = true
            builders.setView(layout)

            builders.setPositiveButton("Create") { dialogInterface, which ->

                if (letterDate.text.toString().length == 10 && relaseDate.text.length == 10) {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    dateFormat.setLenient(false)
                    try {
                        dateFormat.parse(letterDate.text.toString())
                        dateFormat.parse(relaseDate.text.toString())
                        candidateViewModel.printExperienceLetter(
                            candidateId.toString(),
                            letterDate.text.toString(),
                            relaseDate.text.toString(),
                            jobActivityEdit.text.toString(),
                            withStamp.isChecked
                        )

                        serverRes("Experience")
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in CandidateCertificate.kt offerLetter() is " + e.message)
                        Toast.makeText(
                            this,
                            "Please add data in DD-MM-YYYY format.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else
                    Toast.makeText(
                        this,
                        "Please add data in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }


            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt incrementLetter() is " + e.message)
        }
    }

    private fun relievingLetter() {

        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Relieving Letter")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val letterDate = EditText(this)
            val letterDateText = TextView(this)
            letterDate.inputType = 20
            letterDate.setHint("Letter Date")
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            letterDateText.setTextColor(resources.getColor(R.color.black))
            letterDateText.setText("Letter Date")

            val resignDate = EditText(this)
            val resignDateText = TextView(this)
            resignDate.inputType = 20
            resignDate.setHint("Resign Date")
            resignDate.filters = arrayOf(InputFilter.LengthFilter(10))
            resignDate.setTextColor(resources.getColor(R.color.black))
            resignDateText.setText("Resign Date")

            val relaseDate = EditText(this)
            val relaseDateText = TextView(this)
            relaseDate.inputType = 20
            relaseDate.setHint("Release Date")
            relaseDate.filters = arrayOf(InputFilter.LengthFilter(10))
            relaseDateText.setTextColor(resources.getColor(R.color.black))
            relaseDateText.setText("Release Date")


            val rdoGrp: RadioGroup = RadioGroup(this)
            val withStamp: RadioButton = RadioButton(this)
            withStamp.setText("With Stamp")

            val withoutStamp: RadioButton = RadioButton(this)
            withoutStamp.setText("Without Stamp")
            rdoGrp.addView(withStamp)
            rdoGrp.addView(withoutStamp)


            if (data.releivingleeter_date != null && data.releivingleeter_date != "" && data.releivingleeter_date != "null") {
                letterDate.setText(data.releiving_date)
                resignDate.setText(data.resign_date)
                relaseDate.setText(data.releiving_date)
            }


            layout.setPadding(40, 10, 40, 0);
            layout.addView(letterDateText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(letterDate)
            layout.addView(resignDateText)
            layout.addView(resignDate)
            layout.addView(relaseDateText)
            layout.addView(relaseDate)

            layout.addView(rdoGrp)

            withStamp.isChecked = true
            builders.setView(layout)

            builders.setPositiveButton("Create") { dialogInterface, which ->

                if (letterDate.text.toString().length == 10 && relaseDate.text.length == 10) {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    dateFormat.setLenient(false)
                    try {
                        dateFormat.parse(letterDate.text.toString())
                        dateFormat.parse(relaseDate.text.toString())
                        candidateViewModel.printRelievingLetter(
                            candidateId.toString(),
                            letterDate.text.toString(),
                            resignDate.text.toString(),
                            relaseDate.text.toString(),
                            withStamp.isChecked
                        )

                        serverRes("Reliving")
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in CandidateCertificate.kt offerLetter() is " + e.message)
                        Toast.makeText(
                            this,
                            "Please add data in DD-MM-YYYY format.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else
                    Toast.makeText(
                        this,
                        "Please add data in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }


            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt incrementLetter() is " + e.message)
        }
    }

    private fun salarySlip()
    {
        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Salary Slip")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val monthArray = arrayOf("January","February","March","April","May","June","July","August","September","October",
            "November","December")
            val month = Spinner(this)
            val adapter = ArrayAdapter(
                this,
                R.layout.layout_spinneritem, monthArray
            )
            month.adapter = adapter
            val monthText = TextView(this)
            monthText.setTextColor(resources.getColor(R.color.black))
            monthText.setText("Salary Month")

            val year = EditText(this)
            val yearText = TextView(this)
            year.setHint("Year")
            year.inputType = 2
            year.filters = arrayOf(InputFilter.LengthFilter(4))
            yearText.setTextColor(resources.getColor(R.color.black))
            yearText.setText("Year")

            val lop = EditText(this)
            val lopText = TextView(this)
            lop.setText("0")
            lop.inputType = 2
            lop.filters = arrayOf(InputFilter.LengthFilter(2))
            lopText.setTextColor(resources.getColor(R.color.black))
            lopText.setText("LOP")

            val jobPosition = EditText(this)
            val jobPositionText = TextView(this)
            jobPosition.setHint("Job Position")
            jobPosition.filters = arrayOf(InputFilter.LengthFilter(30))
            jobPositionText.setTextColor(resources.getColor(R.color.black))
            jobPositionText.setText("Job Position")

            val newPackage = EditText(this)
            val newPackageText = TextView(this)
            newPackage.setHint("Package")
            newPackage.filters = arrayOf(InputFilter.LengthFilter(30))
            newPackage.inputType = 2
            newPackageText.setTextColor(resources.getColor(R.color.black))
            newPackageText.setText("Package")

            if(data.salaryslip_package !="null" && data.salaryslip_package!=null && data.salaryslip_package!="")
            {
                newPackage.setText(data.salaryslip_package)
                jobPosition.setText(data.salarysllip_jobpos)
            }
            else
            {
            newPackage.setText(data.newPackage.toString())
            jobPosition.setText(data.current_job.toString())
            }

            val salaryByChq = CheckBox(this)
            salaryByChq.setText("Salary by cheque.")





            layout.setPadding(40, 10, 40, 0);
            layout.addView(monthText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(month)
            layout.addView(yearText)
            layout.addView(year)
            layout.addView(lopText)
            layout.addView(lop)
            layout.addView(jobPositionText)
            layout.addView(jobPosition)
            layout.addView(newPackageText)
            layout.addView(newPackage)
            layout.addView(salaryByChq)



            builders.setView(layout)

            builders.setPositiveButton("Create") { dialogInterface, which ->

                if (year.text.length==4 && jobPosition.text.isNotEmpty() && newPackage.text.isNotEmpty()) {
                    try {

                        candidateViewModel.printSalarySlip(
                            candidateId.toString(),
                            month.selectedItem.toString(),year.text.toString(),jobPosition.text.toString(),
                            newPackage.text.toString(),lop.text.toString(),salaryByChq.isChecked
                        )
                        serverRes("SalarySlip",month.selectedItem.toString(),year.text.toString())
                    }
                    catch (e: Exception) { Log.d(TAG, "Error in CandidateCertificate.kt SalarySlip() is " + e.message) }

                } else
                    Toast.makeText(
                        this,
                        "Please add data in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }


            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }
        catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt salrySlip() is " + e.message)
        }
    }
    private fun internshipLetter() {
        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Internship Letter")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val letterDateText = TextView(this)
            letterDateText.setTextColor(resources.getColor(R.color.black))
            val letterDate = EditText(this)
            letterDate.inputType = 20
            val outWard = EditText(this)

            letterDateText.setText("Letter Date")
            letterDate.setHint("Letter Date")


            val outwardText = TextView(this)
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            outWard.setHint("Outward Index")
            outwardText.setText("Outward Index")
            outwardText.setTextColor(resources.getColor(R.color.black))
            outWard.filters = arrayOf(InputFilter.LengthFilter(20))

            val stampedText = TextView(this)
            val stamped = EditText(this)
            stampedText.setTextColor(resources.getColor(R.color.black))
            stampedText.setText("Stipend")
            stamped.inputType = 2


            val rdoGrp: RadioGroup = RadioGroup(this)
            val withStamp: RadioButton = RadioButton(this)
            withStamp.setText("With Stamp")
            //withStamp.isChecked = true
            val withoutStamp: RadioButton = RadioButton(this)
            withoutStamp.setText("Without Stamp")
            rdoGrp.addView(withStamp)
            rdoGrp.addView(withoutStamp)

            if (data.offerletter_date != null && data.offerletter_date != "" && data.offerletter_date != "null")
                letterDate.setText(data.offerletter_date)

            if (data.offerletter_outward != null && data.offerletter_outward != "" && data.offerletter_outward != "null")
                outWard.setText(data.offerletter_outward)

            layout.setPadding(40, 10, 40, 0);
            layout.addView(letterDateText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(letterDate)
            layout.addView(outwardText)
            layout.addView(outWard)
            layout.addView(stampedText)
            layout.addView(stamped)
            layout.addView(rdoGrp)


            builders.setView(layout)
            withStamp.isChecked = true

            builders.setPositiveButton("Create") { dialogInterface, which ->
                if (letterDate.text.toString().length == 10 && outWard.text.isNotEmpty() && stamped.text.isNotEmpty()) {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    try {
                        dateFormat.parse(letterDate.text.toString())
                        candidateViewModel.printInternshipLetter(
                            candidateId, outWard.text.toString(),
                            letterDate.text.toString(), withStamp.isChecked,stamped.text.toString().toInt().toString()
                        )
                        serverRes("Internship")
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in CandidateCertificate.kt internshipLetter() is " + e.message)
                        Toast.makeText(
                            this,
                            "Please add data in DD-MM-YYYY format.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else
                    Toast.makeText(
                        this,
                        "Please add data in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }


            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt offerLetter() is " + e.message)
        }
    }

    private fun internshipCertLetter() {

        try {
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Internship Certificate")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val letterDate = EditText(this)
            val letterDateText = TextView(this)
            letterDate.inputType = 20
            letterDate.setHint("Certificate Date")
            letterDate.filters = arrayOf(InputFilter.LengthFilter(10))
            letterDateText.setTextColor(resources.getColor(R.color.black))
            letterDateText.setText("Certificate Date")

            val relaseDate = EditText(this)
            val relaseDateText = TextView(this)
            relaseDate.inputType = 20
            relaseDate.setHint("Release Date")
            relaseDate.filters = arrayOf(InputFilter.LengthFilter(10))
            relaseDateText.setTextColor(resources.getColor(R.color.black))
            relaseDateText.setText("Release Date")


            val rdoGrp: RadioGroup = RadioGroup(this)
            val withStamp: RadioButton = RadioButton(this)
            withStamp.setText("With Stamp")

            val withoutStamp: RadioButton = RadioButton(this)
            withoutStamp.setText("Without Stamp")
            rdoGrp.addView(withStamp)
            rdoGrp.addView(withoutStamp)


            if (data.expletter_date != null && data.expletter_date != "" && data.expletter_date != "null") {
                letterDate.setText(data.expletter_date)
                relaseDate.setText(data.last_date)
            }


            layout.setPadding(40, 10, 40, 0);
            layout.addView(letterDateText)
            layout.setPadding(40, 0, 40, 0);
            layout.addView(letterDate)
            layout.addView(relaseDateText)
            layout.addView(relaseDate)

            layout.addView(rdoGrp)

            withStamp.isChecked = true
            builders.setView(layout)

            builders.setPositiveButton("Create") { dialogInterface, which ->

                if (letterDate.text.toString().length == 10 && relaseDate.text.length == 10) {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    dateFormat.setLenient(false)
                    try {
                        dateFormat.parse(letterDate.text.toString())
                        dateFormat.parse(relaseDate.text.toString())
                        candidateViewModel.printInternshipCertificate(
                            candidateId.toString(),
                            letterDate.text.toString(),
                            relaseDate.text.toString(),
                            withStamp.isChecked
                        )

                        serverRes("InternshipCert")
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in CandidateCertificate.kt offerLetter() is " + e.message)
                        Toast.makeText(
                            this,
                            "Please add data in DD-MM-YYYY format.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else
                    Toast.makeText(
                        this,
                        "Please add data in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }


            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt incrementLetter() is " + e.message)
        }
    }

    private fun printIdCard()
    {
        try {
            candidateViewModel.printIdCard(candidateId.toString())

            serverRes("IdCard")

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in CandidateCertificate.kt  printIdCard() is "+e.message)
        }
    }


    private fun serverRes(action: String, month: String ="", year: String = "") {
        candidateViewModel.stringResData.observe(this) {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                    .show()
                is NetworkResult.Success -> {
                    Toast.makeText(this, "File Generated Successfully.", Toast.LENGTH_SHORT).show()
                    candidateViewModel.stringResData.removeObservers(this)
                    binding.progressbar.visibility = View.VISIBLE
                    Toast.makeText(
                        this,
                        "File is preparing for Preview Please wait.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler().postDelayed({
                        binding.progressbar.visibility = View.GONE
                        download_share(action,month,year)
                    }, 4000)

                }
            }
        }
    }


    private fun download_share(action: String, month: String, year: String) {
        try {

           /* val urlString = when (action) {
                "Profile" -> BASE_URL + "assets/documents/candidateProfile.pdf"
                "Offer" -> BASE_URL + "assets/documents/offerletter.pdf"
                "Hike" -> BASE_URL + "assets/documents/candidateHikeLetter.pdf"
                //"Experience" -> BASE_URL + "assets/documents/exprianceLetter.pdf"
                "Experience" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Experience Letter.pdf"
                "Reliving" -> BASE_URL + "assets/documents/relievingLetter.pdf"
                "SalarySlip" ->BASE_URL + "assets/documents/candidateSalarySlip.pdf"
                "Internship" -> BASE_URL + "assets/documents/candidateIntershipLetter.pdf"
                "InternshipCert" -> BASE_URL + "assets/documents/candidateInternshipLetter.pdf"
                "IdCard" -> BASE_URL + "assets/documents/IdCard.pdf"

                else -> ""
            }*/
            val urlString = when (action) {
                "Profile" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Profile.pdf"
                "Offer" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Offer Letter.pdf"
                "Hike" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Hike Letter.pdf"
                //"Experience" -> BASE_URL + "assets/documents/exprianceLetter.pdf"
                "Experience" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Experience Letter.pdf"
                "Reliving" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Reliving Letter.pdf"
                "SalarySlip" ->BASE_URL + "assets/documents/${data.first_name} ${data.last_name} ${month.toString()} $year.pdf"
                "Internship" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Internship Letter.pdf"
                "InternshipCert" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Internship Cert.pdf"
                "IdCard" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Id Card.pdf"

                else -> ""
            }
            Log.d(TAG,"-------------"+urlString)



            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.apps.docs")

            startActivity(intent)

        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt download&shar() is " + e.message)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}