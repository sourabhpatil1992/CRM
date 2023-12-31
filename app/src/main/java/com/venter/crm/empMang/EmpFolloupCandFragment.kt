package com.venter.crm.empMang

import android.R.layout.select_dialog_item
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.crm.Dashboard.EmployeeDash
import com.venter.crm.databinding.FragmentEmpFolloupCandBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.models.UserList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@AndroidEntryPoint
class EmpFolloupCandFragment : Fragment() {

    private var _binding: FragmentEmpFolloupCandBinding? = null
    private val binding: FragmentEmpFolloupCandBinding
        get() = _binding!!

    private lateinit var adapter: DataListAdapter


    private lateinit var rawDataList: ArrayList<RawDataList>

    var userDataList: ArrayList<UserList> = ArrayList()


    private val candidateViewModel by viewModels<CandidateViewModel>()

    @Inject
    lateinit var tokenManger: TokenManger

    private fun showData(dataList: ArrayList<RawDataList>) {
        try {
            var cnt = 1
            dataList.forEach {
                it.srNo = cnt++

            }
            adapter.submitList(null)
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
        val employeeDash = activity as? EmployeeDash
        val prosSubType = employeeDash?.prosSubType

        adapter = DataListAdapter(requireContext(),prosSubType)
        adapter = DataListAdapter(requireContext(), prosSubType)
        rawDataList = ArrayList()
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        binding.toDateView.text = date
        binding.fromDate.text = date


        val empList: ArrayList<String> = ArrayList()


        empList.add("You")

        val adapters = ArrayAdapter(
            requireContext(),
            select_dialog_item,
            empList
        )

        binding.spinEmpName.adapter = adapters


        getData()


        candidateViewModel.getFollowUpList(
            tokenManger.getUserId()!!.toInt(),
            binding.fromDate.text.toString(),
            binding.toDateView.text.toString()
        )



        binding.edtSearch.doOnTextChanged { _, _, _, _ -> filterData() }




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

                    rawDataList = it.data!! as ArrayList<RawDataList>

                    showData(rawDataList)

                }
            }


        }


        binding.fromDate.doOnTextChanged { _, _, _, _ ->
           dateChange()
        }
        binding.toDateView.doOnTextChanged { _, _, _, _ ->
           dateChange()
        }

        binding.fromDate.setOnClickListener {
            setDate(binding.fromDate)

        }
        binding.toDateView.setOnClickListener {
            setDate(binding.toDateView)

        }


        return binding.root
    }

    private fun setDate(dateView: TextView) {
        try {
            val materialDateBuilder: MaterialDatePicker.Builder<*> =
                MaterialDatePicker.Builder.datePicker()

            materialDateBuilder.setTitleText("SELECT A DATE")


            val materialDatePicker = materialDateBuilder.build()


            materialDatePicker.show(childFragmentManager, "MATERIAL_DATE_PICKER")

            materialDatePicker.addOnPositiveButtonClickListener {

                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it as Long
                val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

                val formatted = format.format(utc.time).toString()
                dateView.text = formatted


            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpFollowupCandFragment.kt setDate() is : ${e.message}")
        }
    }

    private fun filterData() {
        try {
            val dataList: ArrayList<RawDataList> = ArrayList()
            if (binding.edtSearch.text.isNotEmpty()) {
                val text = binding.edtSearch.text.toString()

                rawDataList.forEach {

                    if (it.prospect_type!!.contains(text, true) || it.candidate_name.contains(
                            text,
                            true
                        ) || it.mob_no.contains(text)
                    ) {


                        dataList.add(it)
                    }

                }


            } else {
                rawDataList.forEach {

                    dataList.add(it)

                }
            }

            showData(dataList)
        } catch (e: Exception) {
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


                                val adapters = ArrayAdapter(
                                    requireContext(),
                                    select_dialog_item,
                                    empList
                                )
                                binding.spinEmpName.adapter = adapters

                                binding.spinEmpName.onItemSelectedListener = object :
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        try {
                                            if (binding.spinEmpName.selectedItem == "You") {
                                                candidateViewModel.getFollowUpList(
                                                    tokenManger.getUserId()!!.toInt(),
                                                    binding.fromDate.text.toString(),
                                                    binding.toDateView.text.toString()
                                                )
                                            } else {
                                                var othersUserId = 0
                                                userDataList.forEach {
                                                    if (it.user_name.contains(
                                                            binding.spinEmpName.selectedItem.toString(),
                                                            true
                                                        )
                                                    )
                                                        othersUserId = it.id
                                                }
                                                candidateViewModel.getFollowUpList(
                                                    othersUserId,
                                                    binding.fromDate.text.toString(),
                                                    binding.toDateView.text.toString()
                                                )

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
                                }


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

    private fun dateChange()
    {
        try {
            if (binding.spinEmpName.selectedItem == "You") {
                candidateViewModel.getFollowUpList(
                    tokenManger.getUserId()!!.toInt(),
                    binding.fromDate.text.toString(),
                    binding.toDateView.text.toString()
                )
            }
            else {
                var othersUserId = 0
                userDataList.forEach {
                    if (it.user_name.contains(binding.spinEmpName.selectedItem.toString(), true))
                        othersUserId = it.id
                }
                candidateViewModel.getFollowUpList(
                    othersUserId,
                    binding.fromDate.text.toString(),
                    binding.toDateView.text.toString()
                )

            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in EmpFollowupCandFragment.kt dateChange() is : ${e.message}")
        }

    }


}