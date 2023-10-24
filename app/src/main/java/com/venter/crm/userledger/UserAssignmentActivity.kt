package com.venter.crm.userledger

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import com.venter.crm.databinding.ActivityUserAssignmentBinding
import com.venter.crm.models.UserDataList
import com.venter.crm.models.UserHierarchyData
import com.venter.crm.models.UserListRes
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAssignmentActivity : AppCompatActivity() {
    private var _binding: ActivityUserAssignmentBinding? = null
    private val binding: ActivityUserAssignmentBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var user: UserListRes? = null

    private var userList: List<UserDataList>? = null

    val userTypes =
        listOf("Sales Manager", "Floor Manager", "Team Lead", "Senior Business Analyst")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            user = intent.getParcelableExtra<UserListRes>("user")

            getUserDet()

            binding.btnSubmit.setOnClickListener {
                submitAction()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAssignmentActivity.kt is : ${e.message}")
        }
    }

    private fun submitAction() {
        try {
           val slId =  userList?.find { it.user_name == binding.spinSl.selectedItem }?.id ?: 0
           val flId =  userList?.find { it.user_name == binding.spinFl.selectedItem }?.id ?: 0
           val tlId =  userList?.find { it.user_name == binding.spinTl.selectedItem }?.id ?: 0
           val sbaId =  userList?.find { it.user_name == binding.spinSba.selectedItem }?.id ?: 0

            candidateViewModel.updateUserHierarchy(user?.id,slId,flId,tlId,sbaId)
            candidateViewModel.stringResData.observe(this)
            {
                binding.progressbar.visibility = View.GONE

                when(it)
                {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    is NetworkResult.Success -> Toast.makeText(this,it.data.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in UserAssignmentActivity.kt submitAction() is : ${e.message}")
        }
    }

    private fun getUserDet() {
        try {
            candidateViewModel.getEmpHierarchyData(user!!.id)

            candidateViewModel.userHierarchyDataLiveData.observe(this)
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
                        //setData(it.data!!)
                        setView(it.data!!)
                    }
                }

            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAssignmentActivity.kt getUserDet() is : ${e.message}")

        }
    }


    private fun setView(data: UserHierarchyData) {
        try {
            var userRole = data.userRole[0]

            userList = data.userList


            binding.linUser.visibility = View.VISIBLE
            binding.txtEmpName.text = user?.user_name
            binding.txtEmpType.text = user?.user_type

            binding.linAssign.visibility =
                if (user?.user_type != "Admin" && user?.user_type != "Sales Manager") View.VISIBLE
                else View.GONE


            for (userType in userTypes) {
                val usersOfType = data.userList.filter { it.user_type == userType }
                val adapterArray = ArrayList(usersOfType.map { it.user_name }.toMutableList())
                adapterArray.add(0, "NA")

                val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, adapterArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                when (userType) {
                    "Sales Manager" -> {
                        binding.spinSl.adapter = adapter
                        val getSlName =
                            data.userList.find { it.id == userRole.sales_manger }?.user_name ?: "NA"
                        val index = adapterArray.indexOf(getSlName)

                        binding.spinSl.setSelection(index, true)

                        setSpinnerItemSelectedListener(binding.spinSl, "SL")
                    }

                    "Floor Manager" -> {
                        binding.spinFl.adapter = adapter
                        val getSlName =
                            data.userList.find { it.id == userRole.floor_manager }?.user_name
                                ?: "NA"
                        val index = adapterArray.indexOf(getSlName)

                        binding.spinFl.setSelection(index, true)

                        setSpinnerItemSelectedListener(binding.spinFl, "FL")

                    }

                    "Team Lead" -> {
                        binding.spinTl.adapter = adapter

                        val getSlName =
                            data.userList.find { it.id == userRole.tl }?.user_name ?: "NA"
                        val index = adapterArray.indexOf(getSlName)

                        binding.spinTl.setSelection(index, true)

                        setSpinnerItemSelectedListener(binding.spinTl, "TL")
                    }

                    "Senior Business Analyst" -> {
                        binding.spinSba.adapter = adapter
                        val getSlName =
                            data.userList.find { it.id == userRole.sba }?.user_name ?: "NA"
                        val index = adapterArray.indexOf(getSlName)

                        binding.spinSba.setSelection(index, true)
                    }
                }

                if (user?.user_type != userType) {
                    when (userType) {
                        "Sales Manager" -> binding.linSl.visibility = View.VISIBLE
                        "Floor Manager" -> binding.linFl.visibility = View.VISIBLE
                        "Team Lead" -> binding.linTl.visibility = View.VISIBLE
                        "Senior Business Analyst" -> binding.linSba.visibility = View.VISIBLE
                    }
                }
            }


        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAssignmentActivity.kt setData() is : ${e.message}")
        }
    }

    private fun setSpinnerItemSelectedListener(spinner: Spinner, userType: String) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                try {

                    getSubOrdinate(userType, spinner.selectedItem.toString())

                    //val adapterArray:ArrayList<String> = ArrayList()
                    //adapterArray.add(0, "NA")
                    //whe


                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "Error in EmpProsFragment.kt binding.spinEmpName.setOnItemSelectedListener is " + e.message
                    )
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }
    }

    private fun getSubOrdinate(userType: String, selectedUser: String) {
        try {
            val selectedId = userList?.find { it.user_name == selectedUser }?.id ?: 0
            candidateViewModel.getSubOrdinateList(selectedId, userType)
            candidateViewModel.subOrdinateDataLiveData.observe(this)
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
                        val data = it.data!!
                        val uType = when (data.userType) {
                            "SL" -> "Floor Manager"
                            "FL" -> "Team Lead"
                            else ->
                                "Senior Business Analyst"
                        }

                        val usersOfType = userList?.filter { it.user_type == uType && (data.userList.contains(it.id))}
                        val adapterArray =
                            ArrayList(usersOfType?.map { it.user_name }!!.toMutableList())
                        adapterArray.add(0, "NA")

                        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, adapterArray)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        when (data.userType) {
                            "SL" -> {
                                binding.spinFl.adapter = adapter
                            }

                            "FL" -> {
                                binding.spinTl.adapter = adapter
                            }
                            "TL" -> {
                                binding.spinSba.adapter = adapter
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAssignmentActivity.kt getSubOrdinate() is : ${e.message}")
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}