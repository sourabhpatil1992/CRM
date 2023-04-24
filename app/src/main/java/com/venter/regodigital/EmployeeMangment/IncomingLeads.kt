package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.R
import com.venter.regodigital.databinding.FragmentIncomingLeadsBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.models.UserList
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class IncomingLeads : Fragment(), chkListner {

    private var _binding: FragmentIncomingLeadsBinding? = null
    private val binding: FragmentIncomingLeadsBinding
        get() = _binding!!

    private lateinit var adapter: RawDataListAdapter

    private val candidateViewModel by viewModels<CandidateViewModel>()


    private var rawDataList: ArrayList<RawDataList> = ArrayList()
    private var userDataList: ArrayList<UserList> = ArrayList()
    private var othersProsDataList: ArrayList<RawDataList> = ArrayList()
    private var showDataList: ArrayList<RawDataList> = ArrayList()


    @Inject
    lateinit var tokenManger: TokenManger


    private fun filterResult() {
        try {
            var srNo = 1
            showDataList.forEach {
                it.srNo = srNo++
            }
            adapter.submitList(showDataList)
            binding.rcCandidate.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcCandidate.adapter = adapter

            binding.linviewFilter.visibility = View.GONE
        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in Incoming Lead.kt filterResult() is " + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentIncomingLeadsBinding.inflate(layoutInflater)
        return try {
            rawDataList = ArrayList()
            userDataList = ArrayList()
            othersProsDataList = ArrayList()
            showDataList = ArrayList()

            adapter = RawDataListAdapter(requireContext(), this)

            binding.floatingRefreshButton.setOnClickListener {
                refreshData()
            }

            binding.floatingActionButton.setOnClickListener {
                addData()
            }


            refreshData()



            binding.root
        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in Incoming Lead createView() is " + e.message)
            null
        }


    }

    private fun showData() {
        try {
            if (showDataList.isNotEmpty()) {
                var srNo = 1
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
            Log.d(Constans.TAG, "Error in EmpProsFragment.kt showData() is " + e.message)
        }
    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

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
            builders.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()

        } catch (e: Exception) {
            Log.d(TAG, "Error in  EmpRawDataFragment.kt addData() is " + e.message)
        }

    }

    /*private fun getData()
    {
        try {

            act = activity as EmpDashboard
            // rawData = act.rawData

            act.rawData!!.forEach {

                if (it.SourceOfApplication == "Incoming Lead")
                    rawDataList.add(it)

            }
            showDataList = rawDataList
            showData()


            binding.txtApply.setOnClickListener {
                filterResult()
            }


        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in IncomingLeads.kt getData() is " + e.message)
        }
    }*/

    private fun refreshData() {
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

                        it.data?.forEach {
                            if (it.SourceOfApplication == "Incoming Lead")
                                rawDataList.add(it)

                        }
                        showDataList = ArrayList()
                        showDataList = rawDataList
                        showData()


                        binding.txtApply.setOnClickListener {
                            filterResult()
                        }


                    }
                }


            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in IncomingLeads.kt refeshData() is " + e.message)
        }
    }


}