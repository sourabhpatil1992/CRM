package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.databinding.FragmentEmpProsFragementBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.models.UserList
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EmpProsFragement : Fragment(), chkListner {
    private var _binding: FragmentEmpProsFragementBinding? = null
    private val binding: FragmentEmpProsFragementBinding
        get() = _binding!!

    private lateinit var adapter: RawDataListAdapter

    // private lateinit var act: EmpDashboard
    var rawDataList: ArrayList<RawDataList> = ArrayList()
    var userDataList: ArrayList<UserList> = ArrayList()
    var othersProsDataList: ArrayList<RawDataList> = ArrayList()
    var showDataList: ArrayList<RawDataList> = ArrayList()

    private val candidateViewModel by viewModels<CandidateViewModel>()

    @Inject
    lateinit var tokenManger: TokenManger


    private fun filterResult() {
        try {
            //Create Filtered Empty List
            var rawList: ArrayList<RawDataList> = ArrayList()
            var srNo = 1
            //Insert Filtered Data
            if (!showDataList.isNullOrEmpty()) {
                showDataList.forEach {
                    if (binding.edtSearch.text.isNotEmpty()) {
                        val text = binding.edtSearch.text.toString()
                        if (it.candidate_name.contains(
                                text,
                                true
                            ) || it.mob_no.contains(text) || (it.id.toString() == text)
                        ) {
                            /*  if (binding.chkCold.isChecked && it.prospect_type == "Cold")
                                  rawList.add(it)*/
                            if (binding.chkWarm.isChecked && it.prospect_type == "Warm")
                                rawList.add(it)
                            if (binding.chkHot.isChecked && it.prospect_type == "Hot")
                                rawList.add(it)
                        }
                    } else {
                        /*if (binding.chkCold.isChecked && it.prospect_type == "Cold")
                            rawList.add(it)*/
                        if (binding.chkWarm.isChecked && it?.prospect_type == "Warm")
                            rawList.add(it)
                        if (binding.chkHot.isChecked && it.prospect_type == "Hot")
                            rawList.add(it)
                    }
                }

                //Set Rc View
                rawList.forEach {
                    it.srNo = srNo++
                }
                adapter.submitList(rawList)
                adapter.notifyDataSetChanged();
                //binding.rcCandidate.layoutManager =
                //    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
               // binding.rcCandidate.adapter = adapter

                binding.linviewFilter.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpProsFrgmanrt.kt filterResult() is " + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        try {
            _binding = FragmentEmpProsFragementBinding.inflate(layoutInflater)
            adapter = RawDataListAdapter(requireContext(), this, empType = tokenManger.getUserType().toString())
            rawDataList = ArrayList()
            userDataList = ArrayList()
            othersProsDataList = ArrayList()
            showDataList = ArrayList()
            val empList: ArrayList<String> = ArrayList()


            empList.add("You")

            val adapters = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.select_dialog_item,
                empList
            )
            binding.spinEmpName.adapter = adapters



            if (tokenManger.getUserType().toString() != "Employee")
                getData()
            else
                getProsData()

            binding.edtSearch.doOnTextChanged { text, start, before, count ->
                filterResult()
            }







            return binding.root
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpProsFrgmant createView() is " + e.message)
            return null
        }
    }

    private fun showData() {
        try {
            var srNo = 1
            if (showDataList.isNotEmpty()) {
                rawDataList.forEach {
                    it.srNo = srNo++
                }
                adapter.submitList(rawDataList)
                binding.rcCandidate.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                binding.rcCandidate.adapter = adapter
            } else {
                Toast.makeText(context, "Data not found!!!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpProsFragament.kt showData() is " + e.message)
        }
    }

    private fun getData() {
        try {
            binding.linEmpSpin.visibility = View.VISIBLE
            candidateViewModel.getEmpListRawData()
            candidateViewModel.userListLiveData.observe(viewLifecycleOwner)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            if (it.data != null) {


                                val empList: ArrayList<String> = ArrayList()


                                empList.add("You")
                                userDataList = ArrayList()

                                it.data?.forEach { data ->
                                    userDataList.add(data)
                                    if (data.id.toString() != tokenManger.getUserId().toString())
                                        empList.add(data.user_name)
                                }


                                val adapters = ArrayAdapter<String>(
                                    requireContext(),
                                    android.R.layout.select_dialog_item,
                                    empList
                                )
                                binding.spinEmpName.adapter = adapters

                                binding.spinEmpName.setOnItemSelectedListener(object :
                                    OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        try {
                                            if (binding.spinEmpName.selectedItem == "You") {
                                                getProsData()
                                            } else {
                                                getOthersProsData()

                                            }
                                        } catch (e: Exception) {
                                            Log.d(
                                                TAG,
                                                "Error in  EmpProsFragment.kt binding.spinEmpName.setOnItemSelectedListener is " + e.message
                                            )
                                        }
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {
                                        return
                                    }
                                })


                            }

                            getProsData()


                        } catch (e: Exception) {
                            Log.d(TAG, "Error in EmpProsFrgmant.kt getData() is  " + e.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                            context,
                            "Something went wrong.Please contact the System Administrator.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpProsFragament.kt getData() is " + e.message)
        }

    }

    private fun getOthersProsData() {
        try {
            showDataList = ArrayList()
            var othersUserId = 0
            othersProsDataList = ArrayList()

            userDataList.forEach {
                if (it.user_name.contains(binding.spinEmpName.selectedItem.toString(), true))
                    othersUserId = it.id.toInt()
            }

            if (othersUserId != 0) {

                candidateViewModel.getOthersProsData(othersUserId)
                candidateViewModel.othersrawDataListResLiveData.observe(viewLifecycleOwner) {
                    binding.progressbar.visibility = View.GONE
                    showDataList = ArrayList()
                    when (it) {

                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> {
                            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                            showDataList = othersProsDataList
                            filterResult()
                        }
                        is NetworkResult.Success -> {
                            othersProsDataList = (it.data as ArrayList<RawDataList>?)!!
                            showDataList = othersProsDataList
                            filterResult()
                        }


                    }
                }

            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpProsFragment.kt getOthersProsData() is " + e.message)
        }


    }

    private fun getProsData() {
        try {

            candidateViewModel.getEmpRawData()
            candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {

                        rawDataList = ArrayList()
                        // userDataList= ArrayList()
                        othersProsDataList = ArrayList()
                        showDataList = ArrayList()

                        it.data?.forEach { it ->

                            if (!it.prospect_type.isNullOrEmpty() && it.prospect_type != "Cold")
                                rawDataList.add(it)

                        }
                        showDataList = rawDataList
                        showData()

                        binding.btnFilter.setOnClickListener {
                            binding.linviewFilter.visibility = View.VISIBLE
                        }
                        binding.txtApply.setOnClickListener {
                            filterResult()
                        }


                    }
                }


            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpProsFragment.kt getProsData() is " + e.message)
        }

    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

    }

}