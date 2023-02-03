package com.venter.regodigital.Candidate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.databinding.ActivityCandidateMangBinding
import com.venter.regodigital.models.CandidateList
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CandidateMang : AppCompatActivity() {
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
        adapter = CandidateListAdapter(this as Context, "Certificate")
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
        adapter.submitList(cList)
        binding.rcCandidate.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcCandidate.adapter = adapter


    }

    private fun setContentView(candidateList: List<CandidateList>) {
        adapter.submitList(candidateList)
        binding.rcCandidate.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcCandidate.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}