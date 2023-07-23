package com.venter.regodigital.EmployeeMangment

import android.R.layout.select_dialog_item
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.databinding.FragmentEmpFolloupCandBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.models.UserList
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EmpFolloupCandFragment : Fragment(),chkListner {

    private var _binding: FragmentEmpFolloupCandBinding? = null
    private val binding: FragmentEmpFolloupCandBinding
        get() = _binding!!

    private lateinit var adapter: RawDataListAdapter


    private lateinit var rawDataList: ArrayList<RawDataList>

    var userDataList: ArrayList<UserList> = ArrayList()



    private val candidateViewModel by viewModels<CandidateViewModel>()

    @Inject
    lateinit var tokenManger: TokenManger

    private fun showData(dataList: ArrayList<RawDataList>) {
        try {
            var cnt =1
           dataList.forEach {
                it.srNo =cnt++

            }
                adapter.submitList(dataList)
                binding.rcCandidate.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                binding.rcCandidate.adapter = adapter

        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpFolloupCandFragment.kt showData() is " + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create Binding
        _binding = FragmentEmpFolloupCandBinding.inflate(layoutInflater)
        adapter = RawDataListAdapter(requireContext(),this, empType = tokenManger.getUserType().toString())
        rawDataList = ArrayList()

        val empList: ArrayList<String> = ArrayList()


        empList.add("You")

        val adapters = ArrayAdapter<String>(
            requireContext(),
            select_dialog_item,
            empList
        )
        binding.spinEmpName.adapter = adapters

        if (tokenManger.getUserType().toString() != "Employee")
            getData()
        else
            binding.linEmpSpin.visibility = View.GONE

        candidateViewModel.getFollowUpList(tokenManger.getUserId()!!.toInt())

        binding.btnFilter.setOnClickListener {
            if(binding.linviewFilter.isVisible)
                binding.linviewFilter.visibility = View.GONE
            else
                binding.linviewFilter.visibility = View.VISIBLE
        }

        binding.txtApply.setOnClickListener {
            if(binding.linviewFilter.isVisible)
                binding.linviewFilter.visibility = View.GONE
            else
                binding.linviewFilter.visibility = View.VISIBLE
            filterData()
        }

        binding.edtSearch.doOnTextChanged { _, _, _, _ ->  filterData()}




        candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
        { it ->
            binding.progressbar.visibility = View.GONE
            binding.swiperefresh.isRefreshing = false

            when(it){
                is NetworkResult.Loading ->{
                    binding.progressbar.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success ->{
                    rawDataList = ArrayList()

                    rawDataList = it.data!! as ArrayList<RawDataList>

                    showData(rawDataList)

                }
            }


        }

        binding.swiperefresh.setOnRefreshListener{
            if (binding.spinEmpName.selectedItem == "You") {
                candidateViewModel.getFollowUpList(tokenManger.getUserId()!!.toInt())
            } else {
                var othersUserId = 0
                userDataList.forEach {
                    if (it.user_name.contains(binding.spinEmpName.selectedItem.toString(), true))
                        othersUserId = it.id
                }
                candidateViewModel.getFollowUpList(othersUserId)

            }

        }




        return binding.root
    }

    private fun filterData() {
        try {
            val dataList: ArrayList<RawDataList> = ArrayList()
            if(binding.edtSearch.text.isNotEmpty())
            {
                val text = binding.edtSearch.text.toString()

                rawDataList.forEach{

                    if(it.prospect_type!!.contains(text,true) || it.candidate_name.contains(text,true) || it.mob_no.contains(text)) {

                        if (binding.chkHot.isChecked && it.prospect_type == "Hot")
                            dataList.add(it)
                        if (binding.chkWarm.isChecked && it.prospect_type == "Warm")
                            dataList.add(it)
                        if (binding.chkCold.isChecked && it.prospect_type == "Cold")
                            dataList.add(it)
                    }

                }


            }
            else
            {
                rawDataList.forEach{
                    if(binding.chkHot.isChecked && it.prospect_type =="Hot")
                        dataList.add(it)
                    if(binding.chkWarm.isChecked && it.prospect_type =="Warm")
                        dataList.add(it)
                    if(binding.chkCold.isChecked && it.prospect_type =="Cold")
                        dataList.add(it)

                }
            }

            showData(dataList)
        }
        catch (e: Exception) {
            Log.d(TAG, "Error in EmpFolloupCandidFragment getData() is " + e.message)
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

                                it.data.forEach { data ->
                                    userDataList.add(data)
                                    if (data.id.toString() != tokenManger.getUserId().toString())
                                        empList.add(data.user_name)
                                }


                                val adapters = ArrayAdapter<String>(
                                    requireContext(),
                                    select_dialog_item,
                                    empList
                                )
                                binding.spinEmpName.adapter = adapters

                                binding.spinEmpName.setOnItemSelectedListener(object :
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        try {
                                            if (binding.spinEmpName.selectedItem == "You") {
                                                candidateViewModel.getFollowUpList(tokenManger.getUserId()!!.toInt())
                                            } else {
                                                var othersUserId = 0
                                                userDataList.forEach {
                                                    if (it.user_name.contains(binding.spinEmpName.selectedItem.toString(), true))
                                                        othersUserId = it.id
                                                }
                                                candidateViewModel.getFollowUpList(othersUserId)

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
            Log.d(TAG, "Error in EmpFolloupCandidFragment getData() is " + e.message)
        }
    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

    }


}