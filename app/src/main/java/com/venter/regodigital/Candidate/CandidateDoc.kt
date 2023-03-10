package com.venter.regodigital.Candidate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.venter.regodigital.databinding.ActivityCandidateDocBinding
import com.venter.regodigital.models.CanCertDetRes
import com.venter.regodigital.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CandidateDoc : AppCompatActivity() {
    private var _binding:ActivityCandidateDocBinding?= null
    private val binding:ActivityCandidateDocBinding
    get() =  _binding!!

    lateinit var data: CanCertDetRes

     var candidateId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCandidateDocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        candidateId = intent.getStringExtra("id")
        data = intent.getParcelableExtra<CanCertDetRes>("Doc")!!




        binding.viewPager.adapter = CandidateViewPager(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}