package com.venter.crm.empMang

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.FragmentStalDataBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.models.UserList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StalDataFragment : Fragment() {
    private var _binding:FragmentStalDataBinding? = null
    private val binding:FragmentStalDataBinding
        get() = _binding!!



    private val candidateViewModel: CandidateViewModel by viewModels()

    var userDataList: ArrayList<UserList> = ArrayList()

    private lateinit var adapter: DataListAdapter

    @Inject
    lateinit var tokenManger: TokenManger



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStalDataBinding.inflate(layoutInflater)

        try {
            adapter = DataListAdapter(requireContext())

            spinnerChange()



            setEmployeeSpinner()
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in StalDataFragment.kt is : ${e.message}")
        }


        return binding.root
    }


    private fun setEmployeeSpinner() {
        candidateViewModel.getEmpListRawData()

        candidateViewModel.userListLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Success -> {
                    binding.progressbar.visibility = View.GONE
                    val empList: ArrayList<String> = ArrayList()
                    empList.add("You")
                    userDataList.clear()

                    result.data?.forEach { data ->
                        userDataList.add(data)
                        if (data.id.toString() != tokenManger.getUserId().toString()) {
                            empList.add(data.user_name)
                        }
                    }

                    val adapters = ArrayAdapter(
                        requireContext(),
                        R.layout.select_dialog_item,
                        empList
                    )
                    binding.spinEmpName.adapter = adapters

                }
            }
        }
    }

    private fun spinnerChange() {

        binding.spinEmpName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {

                    if (binding.spinEmpName.selectedItem == "You") {
                        getLeadData(tokenManger.getUserId()!!.toInt())
                    } else {
                        var othersUserId = 0
                        userDataList.forEach {
                            if (it.user_name.equals(
                                    binding.spinEmpName.selectedItem.toString(),
                                    ignoreCase = true
                                )
                            )
                                othersUserId = it.id
                        }
                        getLeadData(othersUserId)
                    }
                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "Error in IncomingLeads setEmployeeSpinner() onItemSelectedListener is : ${e.message}"
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


    }

    private fun getLeadData(userId: Int) {
        try {


            candidateViewModel.getStealData(userId)

            candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }

                    is NetworkResult.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    is NetworkResult.Success -> {

                        setData(it.data!!)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in IncomingLeads.kt getLeadData() is: ${e.message}")
        }
    }

    private fun setData(data: List<RawDataList>) {
        try {
            if (data.isNotEmpty()) {
                var srNo = 1
                data.forEach {
                    it.srNo = srNo++
                }
            }
            adapter.submitList(null)
            adapter.notifyDataSetChanged()
            adapter.submitList(data)
            binding.rcCandidate.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcCandidate.adapter = adapter
        } catch (e: Exception) {
            Log.d(TAG, "Error in RawDataFragment.kt setData() is : ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}