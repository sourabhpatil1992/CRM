package com.venter.crm.empMang

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.R
import com.venter.crm.databinding.FragmentIncomingLeadsBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.models.UserList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class IncomingLeads : Fragment() {

    private var _binding: FragmentIncomingLeadsBinding? = null
    private val binding: FragmentIncomingLeadsBinding
        get() = _binding!!

    @Inject
    lateinit var tokenManger: TokenManger

    private val candidateViewModel: CandidateViewModel by viewModels()

    var userDataList: ArrayList<UserList> = ArrayList()

    private lateinit var adapter: DataListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIncomingLeadsBinding.inflate(layoutInflater)

        //Set The Adapter
        adapter = DataListAdapter(requireContext())

         spinnerChange()



        setEmployeeSpinner()

        binding.floatingActionButton.setOnClickListener{
            addData()
        }

        return binding.root
    }

    private fun addData() {
        try {

            val builders = AlertDialog.Builder(requireContext())
            builders.setTitle("Incoming Lead")
            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.VERTICAL

            val txtCanName = TextView(context)
            val edtCanName = EditText(context)
            txtCanName.text = "Candidate Name"
            edtCanName.setTextColor(resources.getColor(R.color.black))
            edtCanName.filters = arrayOf(InputFilter.LengthFilter(20))
            txtCanName.setTextColor(resources.getColor(R.color.black))
            edtCanName.hint = "Candidate Name"
            layout.addView(txtCanName)
            layout.addView(edtCanName)

            val txtMobNo = TextView(context)
            val edtMobNo = EditText(context)
            edtMobNo.setTextColor(resources.getColor(R.color.black))
            txtMobNo.setTextColor(resources.getColor(R.color.black))
            edtMobNo.filters = arrayOf(InputFilter.LengthFilter(10))
            edtMobNo.inputType = 2
            txtMobNo.text = "Mobile No"
            edtMobNo.hint = "Mobile No"
            layout.addView(txtMobNo)
            layout.addView(edtMobNo)

            layout.setPadding(40, 10, 40, 0);
            builders.setView(layout)

            builders.setPositiveButton("Create") { dialogInterface, which ->

                if (edtMobNo.text.length == 10 && edtCanName.text.isNotEmpty()) {
                    try {

                        candidateViewModel.setIncomingLead(
                            edtCanName.text.toString(),
                            edtMobNo.text.toString()
                        )
                        candidateViewModel.stringResData.observe(viewLifecycleOwner)
                        {
                            binding.progressbar.visibility = View.GONE
                            when (it) {
                                is NetworkResult.Loading -> binding.progressbar.visibility =
                                    View.VISIBLE
                                is NetworkResult.Error -> Toast.makeText(
                                    context,
                                    it.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                is NetworkResult.Success -> {
                                    Toast.makeText(
                                        context,
                                        "Data Added Successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    refreshData()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in EmpRawDataFragment.kt addData() is " + e.message)
                    }

                } else
                    Toast.makeText(
                        context,
                        "Please fill all data correctly.",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }
            builders.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.crm))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in IncomingLeads.kt addData() is: ${e.message}")
        }
    }

    private fun refreshData() {
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
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in IncomingLeads.kt refreshData() is : ${e.message}")
        }
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

                    val adapters = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.select_dialog_item,
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

            candidateViewModel.getIncomingLeads(userId)

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
