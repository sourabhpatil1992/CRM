package com.venter.crm.EmployeeMangment

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.empMang.DatabaseUploadService
import com.venter.crm.databinding.ActivityRawDataBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RawDataActivity : AppCompatActivity(),chkListner {
    private var _binding:ActivityRawDataBinding? = null
    private val binding:ActivityRawDataBinding
    get() = _binding!!
    private lateinit var adapter: RawDataListAdapter

    lateinit var rawList:ArrayList<RawDataList>

    @Inject
    lateinit var tokenManger: TokenManger

    private var srNo = 1
    private var offset = 0
    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1

    private var DataUri: Uri? = null


    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            val contentResolver = contentResolver
            val cursor = contentResolver.query(selectedUri, null, null, null, null)

            cursor?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                val fileName = cursor.getString(nameIndex)

                if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                    DataUri = selectedUri

                    val builder = AlertDialog.Builder(this)

                    builder.setTitle("Confirmation")
                    builder.setMessage("Are you sure you want to upload this data?")

                    // Add "Yes" button and its click listener
                    builder.setPositiveButton("Yes") { dialog, which ->
                        // Perform the action you want to execute when the user clicks "Yes"
                        // For example, you can upload the data here
                        uploadData()
                        dialog.dismiss()
                    }

                    // Add "No" button and its click listener
                    builder.setNegativeButton("No") { dialog, which ->
                        DataUri = null
                        dialog.dismiss()
                    }

                    // Create and display the dialog
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    Toast.makeText(this@RawDataActivity,"Please Select Excel File gor upload data.",Toast.LENGTH_SHORT).show()

                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRawDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rawList = ArrayList()


        adapter = RawDataListAdapter(this,this,true, empType = tokenManger.getUserType().toString())

        adapter.submitList(null)
        val layoutManager = LinearLayoutManager(this)
        binding.rcCandidate.layoutManager = layoutManager
//                        binding.rcCandidate.layoutManager =
//                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcCandidate.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
           //addData()
            filePicker.launch("*/*")
        }

        candidateViewModel.getAllRawData(offset)
        showData()

        binding.floatingDelButton.setOnClickListener {
            if(rawList.size >0)
            {
                deleteRawData()
            }
        }




    }

    private fun setRescyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rcCandidate.layoutManager = layoutManager
        binding.rcCandidate.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()


                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {

                        // Load more data here
                        candidateViewModel.getAllRawData(offset)
                        showData()
                    }
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    
    fun addData()
    {
        candidateViewModel.addMultipleRawData()
        candidateViewModel.stringResData.observe(this){
            binding.progressbar.visibility = View.GONE
            when(it){
                is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                is NetworkResult.Success ->{
                    Toast.makeText(this,"Data Added successfully.",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    
    private fun showData()
    {
        try{


            candidateViewModel.allrawDataListResLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE

                when(it){
                    is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    is NetworkResult.Success ->{
                        if(it.data!!.size >0 ) {
                            val firstVisiblePosition =
                                (binding.rcCandidate.layoutManager as LinearLayoutManager)
                                    .findFirstVisibleItemPosition()
                            val rList = (it.data as ArrayList<RawDataList>)
                            offset += 100
                            var srNo = 1
                            val oldItemCount = rawList.size
                            rList.forEach { can ->
                                can.srNo = srNo++

                            }
                            rawList.clear()
                            rawList.addAll(rList)



                            adapter.submitList(null)
                            adapter.notifyDataSetChanged()
                            adapter.submitList(rawList)
                            //adapter.notifyDataSetChanged()
                            adapter.notifyItemRangeInserted(oldItemCount, rawList.size)
                            binding.rcCandidate.layoutManager?.scrollToPosition(firstVisiblePosition)

                            isLoading = false
                            setRescyclerView()
                        }
                        else
                            isLastPage = true
                    }
                }



            }

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in RawData.kt showData() is "+e.message)
        }
    }

    private fun deleteRawData()
    {
        try {
            var deleteList :ArrayList<RawDataList> = ArrayList()
            rawList.forEach{
                if(it.selected)
                    deleteList.add(it)

            }

            if(deleteList.size > 0) {
                candidateViewModel.deleteMultipleRawData(deleteList)
                candidateViewModel.stringResData.observe(this) {
                    binding.progressbar.visibility = View.GONE
                    when (it) {
                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> {
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                            //showData()
                        }
                        is NetworkResult.Success -> {
                            Toast.makeText(this, "Data Removed successfully.", Toast.LENGTH_SHORT)
                                .show()
                            //showData()
                        }
                    }
                }
            }

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in The RawDataActivity.kt deleteRawData() is "+e.message)
        }
    }
    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

        rawList[(rawList.indexOf(candidate))].selected = checked



        adapter.submitList(rawList)
        binding.rcCandidate.adapter = adapter

        if(rawList.singleOrNull{ it.selected }!!.selected)
        binding.floatingDelButton.visibility = View.VISIBLE
        else
            binding.floatingDelButton.visibility = View.GONE
    }


    private fun uploadData()
    {
        val intent = Intent(this, DatabaseUploadService::class.java)


        intent.putExtra("FileUri", DataUri.toString())


        startForegroundService(intent)

        Toast.makeText(this,"Uploading file may take longer.", Toast.LENGTH_SHORT).show()


    }
}