package com.venter.regodigital.userledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.venter.regodigital.Dashboard.AdminDashboard
import com.venter.regodigital.databinding.ActivityUserAddBinding
import com.venter.regodigital.models.UserListRes
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAddActivity : AppCompatActivity()
{
    private var _binding:ActivityUserAddBinding? = null
    private val binding:ActivityUserAddBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var user: UserListRes? = null

    private var userId:Int =0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<UserListRes>("user")

        if( user != null)
        {
            binding.edtTxtName.setText(user!!.user_name)
            binding.edtTxtMobNo.setText(user!!.mobile_no)
            binding.edtTxtEmail.setText(user!!.email_id)
            binding.edtTxtDesignation.setText(user!!.job_title)
            if(user!!.user_type == "admin")
            {
                binding.rdobtnAdmin.isChecked = true
            }
            else
            {
                binding.rdobtnEmployee.isChecked = true
            }
            userId = user!!.id

        }

        binding.btnSubmit.setOnClickListener {
            submitAction()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun submitAction()
    {
        try {
            if(binding.edtTxtName.text.isNotEmpty() && binding.edtTxtMobNo.text.length==10)
            {
                val userType = if(binding.rdobtnEmployee.isChecked)
                    "Employee"
                else
                    "Admin"
                candidateViewModel.createUser(binding.edtTxtName.text.toString(),binding.edtTxtMobNo.text.toString(),binding.edtTxtEmail.text.toString(),
                    binding.edtTxtDesignation.text.toString(),userType,userId)

                candidateViewModel.stringResData.observe(this)
                {
                    binding.progressbar.visibility = View.GONE
                    when(it)
                    {
                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                        is NetworkResult.Success ->{
                            Toast.makeText(this,"User Created Successfully.",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,AdminDashboard::class.java)
                            startActivity(intent)
                            this.finishAffinity()
                        }
                    }

                }
            }

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in UserAddActivity.kt submitAction() is "+e.message)
        }

    }
}