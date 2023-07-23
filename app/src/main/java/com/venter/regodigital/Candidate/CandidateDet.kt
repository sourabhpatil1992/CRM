package com.venter.regodigital.Candidate

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.regodigital.Dashboard.AdminDashboard
import com.venter.regodigital.databinding.ActivityCandidateDetBinding
import com.venter.regodigital.models.CandidetDetails
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CandidateDet : AppCompatActivity() {
    private var _binding: ActivityCandidateDetBinding? = null
    private val binding: ActivityCandidateDetBinding
        get() = _binding!!

    private var candidateId: String? = null

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var ProfileUri: Uri? = null
    val salutation = arrayOf("Mr", "Ms", "Mrs")

    @Inject
    lateinit var tokenManger: TokenManger

    private var contract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                ProfileUri = uri
                binding.imgProfile.setImageURI(null)
                binding.imgProfile.setImageURI(ProfileUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCandidateDetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        candidateId = intent.getStringExtra("id")
        setLinView()



        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, salutation
        )
        binding.spinnerSalutation.adapter = adapter

        if (candidateId != null)
            getDetails()

        binding.btnSubmit.setOnClickListener {
                submitAction()
        }

        binding.btnCal.setOnClickListener {
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
                binding.edtJoiningDate.setText(formatted)

            }

        }
        binding.btnDobCal.setOnClickListener {
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
                binding.edtDOB.setText(formatted)

            }

        }

        binding.btnDp.setOnClickListener {
            contract.launch("image/*")
        }


    }

    private fun getDetails() {
        try {
            candidateViewModel.getCandidateDet(candidateId.toString())
            candidateViewModel.CandidateDetResData.observe(this)
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
                        setData(it.data!!)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateDet.kt getDetails() is " + e.message)
        }
    }

    private fun setData(data: CandidetDetails) {
        Picasso.get()
            .load(Constans.BASE_URL + "assets/profile/" + candidateId + ".jpeg")
            .fit()
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .into(binding.imgProfile)

        binding.edtEmployeeId.setText(data.empId)
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, salutation
        )
        binding.spinnerSalutation.setSelection(adapter.getPosition(data.salutation))
        binding.edttxtFirstName.setText(data.firstName)
        binding.edtMiddleName.setText(data.middleName)
        binding.edtLastName.setText(data.lastName)
        if(data.midName!=null)
        binding.middName.isChecked = data.midName==1
        else
            binding.middName.isChecked = false
        binding.edtMobile.setText(data.mobNo)
        binding.edtEmailId.setText(data.emailId)
        if (data.gender == "Male")
            binding.rdobtnMale.isChecked = true
        else
            binding.rdobtnFemale.isChecked = true
        binding.edtDOB.setText(data.dob)
        binding.edtAdd.setText(data.address)
        binding.edtAccNo.setText(data.accNo)
        binding.edtBankName.setText(data.bankName)
        binding.edtPanNo.setText(data.panNo)
        binding.edtBloodGroup.setText(data.bloodGroup)
        binding.edtEduDegree.setText(data.eduDegree)
        binding.edtPassYear.setText(data.passingYear)
        binding.edtCollegeName.setText(data.collegeName)
        binding.edtUniName.setText(data.uniName)
        binding.edtJobTitle.setText(data.joinJobTitle)
        binding.edtJoiningDate.setText(data.joinDate)
        binding.edtJoinPackage.setText(data.joinPackage)
        binding.edtCurJobTitle.setText(data.curJobTitle)
        binding.edtCurPackage.setText(data.curPackage)
        binding.edtFee.setText(data.courseFee)

       if(data.transReq=="true")
           binding.rdobtnYes.isChecked = true
       else
            binding.rdobtNo.isChecked = true

        binding.edtTransFee.setText(data.transFee)
        binding.edtTransComm.setText(data.transComm)

    }

    private fun setLinView() {
        binding.relCandidateDet.setOnClickListener {
            if (binding.btnCandidateDet.text == "+") {
                binding.linCandidateDet.visibility = View.VISIBLE
                binding.btnCandidateDet.text = "-"
            } else {
                binding.linCandidateDet.visibility = View.GONE
                binding.btnCandidateDet.text = "+"

            }
        }
        binding.relEduDet.setOnClickListener {
            if (binding.btnEduDet.text == "+") {
                binding.linEduDet.visibility = View.VISIBLE
                binding.btnEduDet.text = "-"
            } else {
                binding.linEduDet.visibility = View.GONE
                binding.btnEduDet.text = "+"

            }
        }
        binding.relJoinJob.setOnClickListener {
            if (binding.btnJoiningDet.text == "+") {
                binding.linJoiningJob.visibility = View.VISIBLE
                binding.btnJoiningDet.text = "-"
            } else {
                binding.linJoiningJob.visibility = View.GONE
                binding.btnJoiningDet.text = "+"

            }
        }
        binding.relCurrentJob.setOnClickListener {
            if (binding.btnCurrentJob.text == "+") {
                binding.linCurrentJobDet.visibility = View.VISIBLE
                binding.btnCurrentJob.text = "-"
            } else {
                binding.linCurrentJobDet.visibility = View.GONE
                binding.btnCurrentJob.text = "+"

            }
        }

        binding.relFee.setOnClickListener {
            if (binding.btnFee.text == "+") {
                binding.linFee.visibility = View.VISIBLE
                binding.btnFee.text = "-"
            } else {
                binding.linFee.visibility = View.GONE
                binding.btnFee.text = "+"

            }
        }

        binding.rdogrpReqTrans.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            val rb = findViewById<View>(checkedId) as RadioButton
            if(rb.text =="Yes" && rb.isChecked)
            {
                binding.linTransFee.visibility = View.VISIBLE
            }
            else if(rb.text =="No" && rb.isChecked)
            {
                binding.edtTransFee.setText("0")
                binding.linTransFee.visibility = View.GONE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun submitAction() {
        try {
            if (binding.edttxtFirstName.text.isNotEmpty() && binding.edtMobile.text.isNotEmpty()
                && binding.edtEmailId.text.isNotEmpty() && binding.edtAdd.text.isNotEmpty() && binding.edtPassYear.text.isNotEmpty() &&
                binding.edtJobTitle.text.isNotEmpty() && binding.edtJoiningDate.text.isNotEmpty() &&
                binding.edtJoinPackage.text.isNotEmpty() && binding.edtFee.text.isNotEmpty() &&
                binding.edtTransFee.text.isNotEmpty() && binding.edtTransComm.text.isNotEmpty()
            ) {

                val dateFormat = SimpleDateFormat("dd-MM-yyyy")

                dateFormat.parse(binding.edtDOB.text.toString())
                dateFormat.parse(binding.edtJoiningDate.text.toString())

                val currentJobTitle = if (binding.edtCurJobTitle.text.isEmpty())
                    binding.edtJobTitle.text.toString()
                else
                    binding.edtCurJobTitle.text.toString()

                val currentPackage = if (binding.edtCurPackage.text.isEmpty())
                    binding.edtJoinPackage.text.toString()
                else
                    binding.edtCurPackage.text.toString()
                val gender = if (binding.rdobtnMale.isChecked)
                    "Male"
                else
                    "Female"

                val midName = if(binding.middName.isChecked)  1  else 0

                candidateViewModel.createCandidate(
                    CandidetDetails(
                        candidateId,
                        binding.edtEmployeeId.text.toString(),
                        binding.spinnerSalutation.selectedItem.toString(),
                        binding.edttxtFirstName.text.toString(),
                        binding.edtMiddleName.text.toString(),
                        binding.edtLastName.text.toString(),
                        binding.edtMobile.text.toString(),
                        binding.edtEmailId.text.toString(),
                        gender,
                        binding.edtDOB.text.toString(),
                        binding.edtAdd.text.toString(),
                        binding.edtAccNo.text.toString(),
                        binding.edtBankName.text.toString(),
                        binding.edtPanNo.text.toString(),
                        binding.edtBloodGroup.text.toString(),
                        binding.edtEduDegree.text.toString(),
                        binding.edtPassYear.text.toString(),
                        binding.edtCollegeName.text.toString(),
                        binding.edtUniName.text.toString(),
                        binding.edtJobTitle.text.toString(),
                        binding.edtJoiningDate.text.toString(),
                        binding.edtJoinPackage.text.toString(),
                        currentJobTitle,
                        currentPackage,
                        binding.rdobtnYes.isChecked.toString(),
                        binding.edtFee.text.toString(),
                        binding.edtTransFee.text.toString(),
                        binding.edtTransComm.text.toString(),
                        midName
                    
                    )
                )

                candidateViewModel.createCandidateResData.observe(this) {
                    binding.progressbar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = false
                    when (it) {
                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> {
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                            binding.btnSubmit.isEnabled = true
                        }
                        is NetworkResult.Success -> {
                            Toast.makeText(
                                this,
                                "Candidate Create Successfully. Please wait for upload profile picture.",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (ProfileUri != null) {
                                submitProfileDp(it.data!!.candidateId)
                            }

                            val intent = Intent(this, AdminDashboard::class.java)
                            startActivity(intent)
                            this.finishAffinity()

                        }
                    }
                }

            } else {
                Toast.makeText(
                    this,
                    "Please fill all important details. Date in DD-MM-YYYY Format",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateDet.kt SubmitAction() is " + e.message)
        }
    }

    private fun submitProfileDp(candidateId: Int) {

        val intent = Intent(this, CandidateProfileFrgnd::class.java)
        intent.putExtra("candidateId", candidateId)
        intent.putExtra("ProfileUri", ProfileUri.toString())
        intent.putExtra("ProfileType", "Candidate")

        startForegroundService(intent)

    }
}