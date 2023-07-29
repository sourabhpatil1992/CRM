package com.venter.regodigital.userledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.venter.regodigital.databinding.ActivityDataTransBinding
import com.venter.regodigital.models.UserList
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class DataTransActivity : AppCompatActivity() {
    private var _binding:ActivityDataTransBinding? = null
    private val binding:ActivityDataTransBinding
        get() = _binding!!

    var userDataList: ArrayList<UserList> = ArrayList()

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var userId = 0
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDataTransBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("userName")!!
        userId = intent.getIntExtra("userId",0)
        binding.txtDataFrom.text = userName

        getData()
        
        binding.chkAll.setOnCheckedChangeListener { _, isChecked ->

                binding.chkCold.isChecked = isChecked
                binding.chkWarm.isChecked = isChecked
                binding.chkHot.isChecked = isChecked
                binding.chkNotResponding.isChecked = isChecked
                binding.chkAdmission.isChecked = isChecked
                binding.chkRaw.isChecked = isChecked


        }

        binding.btnSubmit.setOnClickListener {
            submitData()
        }
    }

    private fun submitData() {
        try{
            val cold = if(binding.chkCold.isChecked) 1 else 0
            val hot = if(binding.chkHot.isChecked) 1 else 0
            val warm = if(binding.chkWarm.isChecked) 1 else 0
            val notRes = if(binding.chkNotResponding.isChecked) 1 else 0
            val admission = if(binding.chkAdmission.isChecked) 1 else 0
            val raw = if(binding.chkRaw.isChecked) 1 else 0

            val dataTo = userDataList[binding.spinEmpName.selectedItemId.toInt()].id

            candidateViewModel.dataTransfer(userId,dataTo,cold,hot,warm,notRes,admission,raw)

            candidateViewModel.stringResData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when(it)
                {
                    is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    is NetworkResult.Success -> {
                        Toast.makeText(this, it.data.toString(),Toast.LENGTH_SHORT).show()
                        this.finish()
                    }
                }
            }
        }catch (e:Exception)
        {
            Log.d(TAG,"Error i DataTransActivity.kt submitData() is "+e.message)
        }
    }

    private fun getData() {
        candidateViewModel.getEmpListRawData()
        candidateViewModel.userListLiveData.observe(this)
        {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        if (it.data != null) {
                            userDataList = (it.data as ArrayList<UserList>?)!!
                            val empList: ArrayList<String> = ArrayList()



                            it.data.forEach { data ->
                                if(userName != data.user_name)
                                    empList.add(data.user_name)
                            }
                            val adapters = ArrayAdapter<String>(
                                this,
                                android.R.layout.select_dialog_item,
                                empList
                            )
                            binding.spinEmpName.adapter = adapters


                        }
                    }
                    catch (e:Exception)
                    {
                        Log.d(Constans.TAG,"Error in EmpReportsFrgmant.kt getData() is  "+e.message)
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        this,
                        "Something went wrong.Please contact the System Administrator.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}