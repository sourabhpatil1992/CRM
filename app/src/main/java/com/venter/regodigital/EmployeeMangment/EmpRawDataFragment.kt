package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    //This is used for the Checking the Current Position of the Recyclerview
    private var currentPosition = 0

    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // this function is called before text is edited
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


        }

        override fun afterTextChanged(s: Editable) {
            val text =  s.toString()

            var rawData: ArrayList<RawDataList> = ArrayList()
            if(text.isNotEmpty()) {
                if (rawDataList.isNotEmpty()) {
                    var sr_no = 1
                    rawDataList.forEach { it ->
                        if (it.candidate_name.contains(text, true) || it.mob_no.contains(text)) {
                            it.srNo = sr_no++

                            rawData.add(it)
                        }
                    }
                }
                adapter.submitList(rawData)
            }
            else
            {
                var sr_no = 1
                rawDataList.forEach{
                   it.srNo = sr_no++
                }
                adapter.submitList(rawDataList)
            }


            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rcCandidate.setLayoutManager(linearLayoutManager)

            binding.rcCandidate.adapter = adapter

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmpRawDataBinding.inflate(layoutInflater)

        rawDataList =ArrayList()


        adapter = RawDataListAdapter(requireContext(),this)

         binding.floatingActionButton.setOnClickListener {
             candidateViewModel.getEmpRawData()
         }

        binding.rcCandidate.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                try {
                     val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                      currentPosition = layoutManager!!.findFirstVisibleItemPosition()


                }
                catch (e:Exception)
                {
                    Log.d(TAG,"Error in ..."+e.message)
                }
            }
        })



       // candidateViewModel.getEmpRawData()
        candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
        {
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

                    rawDataList =ArrayList()
                    it.data?.forEach{
                        if(it.prospect_type.isNullOrEmpty())
                            rawDataList.add(it)

                    }
                    showData()



                }
            }


        }

        binding.edtSearch.addTextChangedListener(textWatcher)

        binding.swiperefresh.setOnRefreshListener{
            candidateViewModel.getEmpRawData()
           // binding.swiperefresh.isRefreshing = false
        }


        return binding.root
    }

    private fun searchList() {
        try {
            val text = binding.edtSearch.text.toString()
            if (rawDataList.isNotEmpty() && text.isNotEmpty())
            {

                var rawData: ArrayList<RawDataList> = ArrayList()
                var srNo = 1
                rawDataList.forEach { it ->
                    if (it.candidate_name.contains(
                            text,
                            true
                        ) || it.mob_no.contains(text) || it.prospect_type!!.contains(text, true)
                    ){
                        it.srNo = srNo++
                        rawData.add(it)
                    }
                }
                adapter.submitList(rawData)

                /*binding.rcCandidate.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)*/

                val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rcCandidate.setLayoutManager(linearLayoutManager)

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
        var srNo = 1
        rawDataList.forEach{
            it.srNo = srNo++
        }

            adapter.submitList(rawDataList)
           /* binding.rcCandidate.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)*/

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcCandidate.setLayoutManager(linearLayoutManager)

            binding.rcCandidate.adapter = adapter
        binding.rcCandidate.layoutManager?.scrollToPosition(currentPosition)




    }

    /*private fun addData() {
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
*/
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

    }

    override fun onResume() {
        super.onResume()
        
        candidateViewModel.getEmpRawData()
        binding.rcCandidate.layoutManager?.scrollToPosition(currentPosition)
    }

}