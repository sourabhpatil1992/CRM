package com.venter.crm.hrMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.R
import com.venter.crm.databinding.FragmentHrEmpBinding
import com.venter.crm.models.EmployeeList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

/**************************************************************
 *
 * HREmployee Fragment In this SHows list of all Employee
 * Can Create an employee Employee and user are different
 * diff is Emp may be not have an access of app
 * Created By : Sourabh Patil
 * Create On : 08 Nov 2023
 *
 ***************************************************************/

@AndroidEntryPoint
class HrEmpFragment : Fragment(),EmployeeListInterface {
    private var _binding: FragmentHrEmpBinding? = null
    private val binding: FragmentHrEmpBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var empList: ArrayList<EmployeeList>
    private lateinit var adapter: EmployeeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentHrEmpBinding.inflate(layoutInflater)

            empList = ArrayList()
            adapter = EmployeeListAdapter(this)

            binding.floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_hrEmpFragment_to_createEmpFragment)
            }

            getData()

            binding.edtSearch.doOnTextChanged { text, _, _, _ ->

                searchAction(text.toString().trim())
            }

            binding.root

        } catch (e: Exception) {
            Log.d(TAG, "Error in HrEmpFragment.kt is ${e.message}")
            null
        }


    }

    private fun searchAction(text: String) {
        try {
            val filterList: ArrayList<EmployeeList> = ArrayList()
            if (text.isEmpty()) {
                filterList.addAll(empList)
            } else {
                empList.forEach {

                    if (
                        (it.empId?.contains(text, ignoreCase = true) == true) ||
                        (it.cJob?.contains(text, ignoreCase = true) == true) ||
                        (it.fName?.contains(text, ignoreCase = true) == true) ||
                        (it.mName?.contains(text, ignoreCase = true) == true) ||
                        (it.lName?.contains(text, ignoreCase = true) == true)
                    ) {
                        filterList.add(it)
                    }
                }
            }



            setData(filterList)
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in HrEmpFragment.kt searchAction() is :${e.message}")
        }


    }

    private fun getData() {
        candidateViewModel.getEmpList()
        candidateViewModel.empListLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(
                    requireContext(),
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                is NetworkResult.Success -> {
                    empList = it.data as ArrayList<EmployeeList>
                    setData(empList)
                }
            }
        }
    }

    private fun setData(empList: ArrayList<EmployeeList>) {
        adapter.submitList(null)
        adapter.submitList(empList)
        binding.rcEmp.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcEmp.adapter = adapter
    }

    override fun empDet(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id",id)
        findNavController().navigate(R.id.action_hrEmpFragment_to_empDetFragment,bundle)
    }


}