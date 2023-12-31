package com.venter.crm.userledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.ActivityUserLedgerBinding
import com.venter.crm.models.UserListRes
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLedgerActivity : AppCompatActivity(), statusUser {
    private var _binding: ActivityUserLedgerBinding? = null
    private val binding: ActivityUserLedgerBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter: UserListAdapate



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserLedgerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        adapter = UserListAdapate(this, this)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, UserAddActivity::class.java)
            startActivity(intent)
        }
        setView()
    }

    private fun setView() {
        try {
            candidateViewModel.userList()
            candidateViewModel.userListResLiveData.observe(this)
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
                        setRcView(it.data)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserLedgerActivity.kt setView() is " + e.message)
        }
    }

    private fun setRcView(data: List<UserListRes>?) {
        adapter.submitList(data)
        binding.rcView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcView.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun changeUserStatus(userId: Int, status: Int) {
        try {
            candidateViewModel.userStatus(userId, status)
            candidateViewModel.stringResData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this, it.message!!, Toast.LENGTH_SHORT)
                        .show()
                    is NetworkResult.Success -> {
                        Toast.makeText(this, "Status Updated Successfully.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserLedgerActivity.kt changeUserStatus() is " + e.message)
        }
    }


}