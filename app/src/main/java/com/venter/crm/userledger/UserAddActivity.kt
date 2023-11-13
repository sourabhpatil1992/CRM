package com.venter.crm.userledger

import android.R
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.crm.Candidate.CandidateProfileFrgnd
import com.venter.crm.Dashboard.AdminDashboard
import com.venter.crm.databinding.ActivityUserAddBinding
import com.venter.crm.models.UserListRes
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserAddActivity : AppCompatActivity() {
    private var _binding: ActivityUserAddBinding? = null
    private val binding: ActivityUserAddBinding
        get() = _binding!!

    private var ProfileUri: Uri? = null

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var user: UserListRes? = null

    private var userId: Int = 0

    private var contract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                ProfileUri = uri
                binding.imgProfile.setImageURI(null)
                binding.imgProfile.setImageURI(ProfileUri)
            }
        }

    @Inject
    lateinit var tokenManger: TokenManger

    private val roles = arrayOf(
        "Business Analyst", "Senior Business Analyst", "Team Lead", "Floor Manager",
        "Sales Manager", "Admin"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, roles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRole.adapter = adapter

            user = intent.getParcelableExtra<UserListRes>("user")


            if (user != null) {
                Picasso.get()
                    .load(Constans.BASE_URL + "assets/userProfile/" + user!!.id + ".jpeg")
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(binding.imgProfile)

                binding.edtTxtName.setText(user!!.user_name)
                binding.edtTxtMobNo.setText(user!!.mobile_no)
                binding.edtTxtEmail.setText(user!!.email_id ?: "")
               // binding.edtTxtDesignation.setText(user!!.job_title ?: "")


                val position = adapter.getPosition(user!!.user_type)
                if (position != AdapterView.INVALID_POSITION) {
                    binding.spinnerRole.setSelection(position)
                }



                userId = user!!.id

            }

            binding.btnSubmit.setOnClickListener {
                submitAction()
            }

            binding.btnDp.setOnClickListener {
                contract.launch("image/*")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAddActivity.kt is : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun submitAction() {
        try {
            if (binding.edtTxtName.text.isNotEmpty() && binding.edtTxtMobNo.text.length == 10) {
                val userType = binding.spinnerRole.selectedItem.toString()
                candidateViewModel.createUser(
                    binding.edtTxtName.text.toString(),
                    binding.edtTxtMobNo.text.toString(),
                    binding.edtTxtEmail.text.toString(),
                    binding.edtTxtDesignation.text.toString(),
                    userType,
                    userId
                )

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
                            if (ProfileUri != null) {
                                val intent = Intent(this, CandidateProfileFrgnd::class.java)
                                intent.putExtra("candidateId", it.data!!.toInt())
                                intent.putExtra("ProfileUri", ProfileUri.toString())
                                intent.putExtra("ProfileType", "User")

                                startForegroundService(intent)
                            }

                            Toast.makeText(this, "User Created Successfully.", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, AdminDashboard::class.java)
                            startActivity(intent)
                            this.finishAffinity()
                        }
                    }

                }
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAddActivity.kt submitAction() is " + e.message)
        }

    }
}