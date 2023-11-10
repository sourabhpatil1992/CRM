package com.venter.crm.hrMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.venter.crm.R
import com.venter.crm.databinding.FragmentHrEmpDetBinding
import com.venter.crm.models.EmpInfoData
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HrEmpDetFragment : Fragment() {
    private var _binding: FragmentHrEmpDetBinding? = null
    private val binding: FragmentHrEmpDetBinding
        get() = _binding!!

    private var empId: Int? = 0

    private val candidateViewModel by viewModels<CandidateViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return try {
            _binding = FragmentHrEmpDetBinding.inflate(layoutInflater)
            empId = arguments?.getInt("id").toString().toIntOrNull()
            if (empId!! > 0) {
                candidateViewModel.getEmpInfo(empId!!)
                candidateViewModel.empInfoLiveData.observe(viewLifecycleOwner)
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

                binding.btnEdtEmp.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("id",empId!!)
                   findNavController().navigate(R.id.action_empDetFragment_to_createEmpFragment,
                       bundle
                   )
                }
            }


            binding.root
        } catch (e: Exception) {
            Log.d(TAG, "Error in HrEmpDetFragment.kt is : ${e.message}")
            null
        }
    }

    private fun setData(data: EmpInfoData) {
        try {
            binding.empId.text = data.empId.toString()
            val empName = "${data.salutation ?: ""} ${data.fName ?: ""} ${data.mName ?: ""} ${data.lName ?: ""}"
            binding.empName.text = empName
            binding.mobNo.text = data.mobNo
            binding.email.text = data.emailId
            binding.dept.text = data.dept
            binding.job.text = data.cJob
            binding.btnEdtEmp.visibility = View.VISIBLE
        } catch (e: Exception) {
            Log.d(TAG, "Error in HrEmpDetFragment.kt setData() is : ${e.message}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}