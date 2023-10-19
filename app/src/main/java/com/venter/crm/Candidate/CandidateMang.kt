package com.venter.crm.Candidate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.ActivityCandidateMangBinding
import com.venter.crm.models.CandidateList
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CandidateMang : AppCompatActivity(),CandidateClickInterface {
    private lateinit var adapter: CandidateListAdapter
    private var _binding: ActivityCandidateMangBinding? = null
    private val binding: ActivityCandidateMangBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()
    private lateinit var candidateList: List<CandidateList>

    @Inject
    lateinit var tokenManger: TokenManger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCandidateMangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = CandidateListAdapter(this as Context, "Certificate",this)
        candidateList = ArrayList()

        candidateViewModel.candidateList()



        candidateViewModel.candidateListResData.observe(this)
        {
            binding.progressbar.visibility = View.GONE
            when(it)
            {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> {Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()}
                is NetworkResult.Success -> {
                    candidateList = it.data!!
                    setContentView(candidateList)
                }

                else -> {}
            }
        }

        binding.edtSearch.doOnTextChanged { _, _, _, _ ->
            updateSearchText()
        }


        binding.floatingActionButton.setOnClickListener{
            val intent = Intent(this,CandidateDet::class.java)
            startActivity(intent)
        }

    }

    private fun updateSearchText() {
        var cList: ArrayList<CandidateList> = ArrayList()
        if(binding.edtSearch.text.isEmpty())
        {
            cList = candidateList as ArrayList<CandidateList>
        }
        else
        {
            val text = binding.edtSearch.text.toString()
            candidateList.forEach {
                if(it.first_name.contains(text,true) || it.middel_name.contains(text,true) || it.last_name.contains(text,true) || it.mobile_no.contains(text,true))
                {
                    cList.add(it)
                }
            }

        }
        adapter.submitList(null)
        adapter.notifyDataSetChanged()
        adapter.submitList(cList)
        binding.rcCandidate.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcCandidate.adapter = adapter


    }

    private fun setContentView(candidateList: List<CandidateList>) {
        adapter.submitList(null)
        adapter.notifyDataSetChanged()
        adapter.submitList(candidateList)
        binding.rcCandidate.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcCandidate.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun delCandidate(id: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setMessage("Are you sure you want to remove this candidate?")
            setPositiveButton("Yes") { dialogInterface, _ ->
                candidateViewModel.removeCandidate(id)

                observData()


            }
            setNegativeButton("No") { dialogInterface, _ ->

                dialogInterface.dismiss()
            }
            setCancelable(true)
        }

        // Create and show the alert dialog
        alertDialog.create().show()
    }

    private fun observData() {
        candidateViewModel.stringResData.observe(this)
        {
            binding.progressbar.visibility = View.GONE
            when(it)
            {
                is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                is NetworkResult.Success -> {
                    Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                    candidateViewModel.candidateList()
                }
            }

        }
    }
}