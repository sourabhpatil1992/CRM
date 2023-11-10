package com.venter.crm.hrMangment

import android.content.ContentResolver
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.crm.R
import com.venter.crm.databinding.FragmentCreateEmpBinding
import com.venter.crm.models.EmpDet
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


/**************************************************************
 *
 * HREmployee Fragment In get the details of employee
 * Create,Update can possible
 * diff is Emp may be not have an access of app
 * Created By : Sourabh Patil
 * Create On : 09 Nov 2023
 *
 ***************************************************************/


@AndroidEntryPoint
class CreateEmpFragment : Fragment() {

    private var _binding: FragmentCreateEmpBinding? = null
    private val binding: FragmentCreateEmpBinding
        get() = _binding!!

    private var profileUri: Uri? = null
    private var documentUri: ArrayList<Uri?> = ArrayList()

    private val candidateViewModel by viewModels<CandidateViewModel>()


    private lateinit var adapter: EmployeeDocAdapter

    private var empId = 0

    private var contract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                profileUri = uri
                binding.imgProfile.setImageURI(null)
                binding.imgProfile.setImageURI(profileUri)
            }
        }
    private var contractDocument =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uri: List<Uri>? ->
            if (uri != null) {
                uri.forEach {
                    val mimeType = getMimeType(requireContext().contentResolver, it)
                    if (mimeType.startsWith("image/") || mimeType == "application/pdf") {
                        documentUri.add(it)
                    }
                }


                setDocumentRc()
            }
        }

    private fun getMimeType(contentResolver: ContentResolver, uri: Uri): String {
        return contentResolver.getType(uri) ?: ""
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentCreateEmpBinding.inflate(layoutInflater)

            adapter = EmployeeDocAdapter(requireContext())

            empId = arguments?.getInt("id").toString().toInt()



            cascadingView()

            val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Date())
            binding.edtDOB.text = todayDate
            binding.joiningDate.text = todayDate

            binding.btnDp.setOnClickListener {
                contract.launch("image/*")
            }
            binding.btnAddDoc.setOnClickListener {
                contractDocument.launch(arrayOf("*/*"))

            }

            binding.btnSubmit.setOnClickListener {
                submitData()
            }

            getEmpDet()





            binding.root

        } catch (e: Exception) {
            Log.d(TAG, "Error in CreateEmpFragment.kt is : ${e.message}")
            null
        }
    }

    private fun getEmpDet() {
        if (empId > 0) {
            candidateViewModel.getEmpDet(empId)
            candidateViewModel.empDetLiveData.observe(viewLifecycleOwner)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(
                        context,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        setData(it.data!!)
                    }
                }

            }
        }

    }

    private fun setData(data: EmpDet) {
        val spinSalutation = resources.getStringArray(R.array.salutation)
        binding.spinnerSalutation.setSelection(spinSalutation.indexOf(data.salutation))
        binding.firstName.setText(data.fName)
        binding.middleName.setText(data.mName)
        binding.lastName.setText(data.lName)
        binding.chkMname.isChecked = data.mVisible == 1
        binding.mobNo.setText(data.mobNo)
        binding.edtEmailId.setText(data.emailId)
        if (data.gender == "Male")
            binding.rdoMale.isChecked = true
        else
            binding.rdoFemale.isChecked = true
        binding.edtDOB.text = data.dob
        binding.edtAdd.setText(data.address)
        binding.edtBankName.setText(data.bank)
        binding.edtAccNo.setText(data.accNo)
        binding.edtPanNo.setText(data.pan)
        binding.edtBloodGroup.setText(data.bloodGrp)
        binding.empId.setText(data.empId)
        binding.department.setText(data.dept)
        binding.edtJobTitle.setText(data.job)
        binding.joiningDate.text = data.joinDate
        binding.edtJoinPackage.setText(data.jPackage.toString())
        binding.chkAppAccess.isChecked = data.appAccess == 1

        val userRole = resources.getStringArray(R.array.roles)
        binding.spinnerRole.setSelection(userRole.indexOf(data.userType))

        binding.edtCurJobTitle.setText(data.cJob)
        binding.edtCurPackage.setText(data.cPackage.toString())

        //Set DP Image and documents Image


    }

    private fun submitData() {
        if (checkValidation()) {
            val mVisible = if (binding.chkMname.isChecked)
                1
            else
                0

            val gender = if (binding.rdoMale.isChecked)
                "Male"
            else
                "Female"

            val joinPackage: Double =
                if (binding.edtJoinPackage.text.toString().toDoubleOrNull() == null)
                    0.0
                else
                    binding.edtJoinPackage.text.toString().toDouble()

            val currentPackage: Double =
                if (binding.edtCurPackage.text.toString().toDoubleOrNull() == null)
                    joinPackage
                else
                    binding.edtCurPackage.text.toString().toDouble()

            val appAccess = if (binding.chkAppAccess.isChecked)
                1
            else
                0

            val userType = if (appAccess == 1)
                binding.spinnerRole.selectedItem.toString()
            else
                ""

            val currentJob = binding.edtCurJobTitle.text.toString()
                .ifEmpty { binding.edtJobTitle.text.toString() }


            val empDet = EmpDet(
                empId,
                binding.spinnerSalutation.selectedItem.toString(),
                binding.firstName.text.toString(),
                binding.middleName.text.toString(),
                binding.lastName.text.toString(),
                mVisible,
                binding.mobNo.text.toString(),
                binding.edtEmailId.text.toString(),
                gender,
                binding.edtDOB.text.toString(),
                binding.edtAdd.text.toString(),
                binding.edtBankName.text.toString(),
                binding.edtAccNo.text.toString(),
                binding.edtPanNo.text.toString(),
                binding.edtBloodGroup.text.toString(),
                binding.empId.text.toString(),
                binding.department.text.toString(),
                binding.edtJobTitle.text.toString(),
                binding.joiningDate.text.toString(),
                joinPackage, appAccess, userType, currentJob, currentPackage
            )


            candidateViewModel.createEmployee(empDet)

        } else {
            Toast.makeText(context, "Please fill all details correctly.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkValidation(): Boolean {
        try {
            val firstName = binding.firstName.text.toString()
            val mobNo = binding.mobNo.text.toString()
            val email = binding.edtEmailId.text.toString()

            if (firstName.isEmpty() || mobNo.isEmpty() || email.isEmpty()) {
                binding.linCandidateDet.visibility = View.VISIBLE
                binding.btnEmpDet.text = getString(R.string.minus)

                if (firstName.isEmpty()) {
                    binding.firstName.requestFocus()
                } else if (mobNo.isEmpty()) {
                    binding.mobNo.requestFocus()
                } else {
                    binding.edtEmailId.requestFocus()
                }

                return false
            }
            if (binding.mobNo.text.length < 10) {
                binding.mobNo.requestFocus()
                return false
            }


            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!email.matches(emailPattern.toRegex())) {
                binding.edtEmailId.requestFocus()
                return false
            }

            return true
        } catch (e: Exception) {
            Log.d(TAG, "Error in CreateEmpFragment.kt checkValidation() is ${e.message}")
            return false
        }
    }


    private fun cascadingView() {

        binding.relCandidateDet.setOnClickListener {

            if (binding.linCandidateDet.visibility == View.VISIBLE) {
                binding.linCandidateDet.visibility = View.GONE
                binding.btnEmpDet.text = getString(R.string.plus)
            } else {
                binding.linCandidateDet.visibility = View.VISIBLE
                binding.btnEmpDet.text = getString(R.string.minus)
            }
        }
        binding.relJoinJob.setOnClickListener {

            if (binding.linJoiningJob.visibility == View.VISIBLE) {
                binding.linJoiningJob.visibility = View.GONE
                binding.btnJoiningDet.text = getString(R.string.plus)
            } else {
                binding.linJoiningJob.visibility = View.VISIBLE
                binding.btnJoiningDet.text = getString(R.string.minus)
            }
        }

        binding.relCurrentJob.setOnClickListener {

            if (binding.linCurrentJobDet.visibility == View.VISIBLE) {
                binding.linCurrentJobDet.visibility = View.GONE
                binding.btnCurrentJob.text = getString(R.string.plus)

            } else {
                binding.linCurrentJobDet.visibility = View.VISIBLE
                binding.btnCurrentJob.text = getString(R.string.minus)
            }
        }
        binding.relDocuments.setOnClickListener {

            if (binding.linDocumets.visibility == View.VISIBLE) {
                binding.linDocumets.visibility = View.GONE
                binding.btnDoc.text = getString(R.string.plus)

            } else {
                binding.linDocumets.visibility = View.VISIBLE
                binding.btnDoc.text = getString(R.string.minus)
            }
        }

        binding.chkAppAccess.setOnCheckedChangeListener { _, isChecked ->

            binding.linAppAccess.visibility =
                if (isChecked)
                    View.VISIBLE
                else
                    View.GONE
        }

        binding.joiningDate.setOnClickListener {
            setDate(binding.joiningDate)
        }
        binding.btnCal.setOnClickListener {
            setDate(binding.joiningDate)
        }
        binding.edtDOB.setOnClickListener {
            setDate(binding.edtDOB)
        }
        binding.btnDobCal.setOnClickListener {
            setDate(binding.edtDOB)
        }

    }

    private fun setDocumentRc() {
        try {
            adapter.submitList(documentUri)
            binding.rcView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            binding.rcView.adapter = adapter

            binding.rcView.visibility = if (documentUri.isNotEmpty())
                View.VISIBLE
            else
                View.GONE

        } catch (e: Exception) {
            Log.d(TAG, "Error in CreateEmpFragment.kt setDocumentRc() is : ${e.message}")
        }
    }

    private fun setDate(edtText: TextView) {
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilder.setTitleText("SELECT A DATE")


        val materialDatePicker = materialDateBuilder.build()


        materialDatePicker.show(requireActivity().supportFragmentManager, "MATERIAL_DATE_PICKER")

        materialDatePicker.addOnPositiveButtonClickListener {

            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it as Long
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

            val formatted = format.format(utc.time).toString()
            edtText.text = formatted

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}