package com.venter.crm.Candidate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.crm.databinding.ActivityCandidateDocBinding
import com.venter.crm.models.CanCertDetRes
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