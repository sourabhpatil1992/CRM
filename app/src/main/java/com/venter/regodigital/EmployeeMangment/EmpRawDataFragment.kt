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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.R
import com.venter.regodigital.databinding.FragmentEmpRawDataBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EmpRawDataFragment : Fragment(),chkListner {

    private var _binding: FragmentEmpRawDataBinding? = null
    private val binding: FragmentEmpRawDataBinding
        get() = _binding!!

    private lateinit var adapter: RawDataListAdapter


    var rawDataList: ArrayList<RawDataList> =ArrayList()

    private val candidateViewModel by viewModels<CandidateViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmpRawDataBinding.inflate(layoutInflater)

        rawDataList =ArrayList()


        adapter = RawDataListAdapter(requireContext(),this)

         binding.floatingActionButton.setOnClickListener {
             addData()
         }


       // candidateViewModel.getEmpRawData()
        candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            when(it){
                is NetworkResult.Loading ->{
                    binding.progressbar.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success ->{
                    Log.d(TAG,"Check Resume")
                    rawDataList =ArrayList()
                    it.data?.forEach{
                        if(it.prospect_type.isNullOrEmpty())
                            rawDataList.add(it)

                    }
                    showData()
                    binding.edtSearch.doOnTextChanged { _, _, _, _ ->
                        searchList()
                    }


                }
            }


        }


        return binding.root
    }

    private fun searchList() {
        try {
            if (rawDataList.isNotEmpty() && !binding.edtSearch.text.isNullOrEmpty()) {
                val text = binding.edtSearch.text.toString()
                var rawData: ArrayList<RawDataList> = ArrayList()
                rawDataList.forEach { it ->
                    if (it.candidate_name.contains(
                            text,
                            true
                        ) || it.mob_no.contains(text) || it.prospect_type!!.contains(text, true)
                    )
                        rawData.add(it)
                }
                adapter.submitList(rawData)
                binding.rcCandidate.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                binding.rcCandidate.adapter = adapter
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in EmpRawDataFrgament.kt searchlist() is "+e.message)
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun showData() {
       // if (rawDataList.isNotEmpty()) {

            adapter.submitList(rawDataList)
            binding.rcCandidate.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcCandidate.adapter = adapter
        /*}
        else{
            Toast.makeText(context,"Data not found!!!",Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun addData() {
        try{

            val builders = AlertDialog.Builder(requireContext())
            builders.setTitle("Experience Letter")
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
            val edtMobNo  = EditText(context)
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

                if (edtMobNo.text.length==10 && edtCanName.text.isNotEmpty()) {
                    try {

                        candidateViewModel.setIncomingLead(edtCanName.text.toString(),edtMobNo.text.toString())
                    }
                    catch (e: Exception) { Log.d(TAG, "Error in EmpRawDataFragment.kt addData() is " + e.message) }

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

        }
        catch (e:Exception){
            Log.d(TAG,"Error in  EmpRawDataFragment.kt addData() is "+e.message)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"Check Resume")
        candidateViewModel.getEmpRawData()
    }

}