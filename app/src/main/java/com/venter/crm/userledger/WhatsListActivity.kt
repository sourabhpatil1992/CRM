package com.venter.crm.userledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.Dashboard.AdminDashboard
import com.venter.crm.databinding.ActivityWhatsListBinding
import com.venter.crm.models.WhatsAppAccList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhatsListActivity : AppCompatActivity(), WhatsListInterface {
    private var _binding: ActivityWhatsListBinding? = null
    private val binding: ActivityWhatsListBinding
        get() = _binding!!
    private lateinit var adapter: WhatsListAdapter
    private var userId = 0
    private var whatsAcc = 0

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWhatsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            userId = intent.getIntExtra("userId", 0)
            whatsAcc = intent.getIntExtra("whatsAcc", 0)
            adapter = WhatsListAdapter(this, whatsAcc)



            getData()

        } catch (e: Exception) {
            Log.d(TAG, "Error in WhatsListActivity.kt is :${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getData() {
        try {
            candidateViewModel.getWhatsAppList()
            candidateViewModel.whatsAccListResLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        binding.rcView.layoutManager =
                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcView.adapter = adapter
                        adapter.submitList(null)
                        adapter.submitList(it.data)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in WhatAppAccounts.kt getData() is : ${e.message}")
        }
    }

    override fun accLongClick(acc: WhatsAppAccList) {
        try {
            candidateViewModel.updateWhatsAppAcc(acc.id,userId)


            candidateViewModel.stringResData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when(it)
                {
                    is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    is NetworkResult.Success ->{
                        Toast.makeText(this,"Account Updated Successfully.",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,AdminDashboard::class.java))
                        this.finishAffinity()

                    }
                }
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in WHatsListActivity.kt accLongClick() is : ${e.message}")
        }
    }
}