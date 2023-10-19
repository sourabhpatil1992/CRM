package com.venter.crm.Candidate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.crm.databinding.ActivityCandidateAccBinding

class CandidateAcc : AppCompatActivity() {
    private var _binding: ActivityCandidateAccBinding? = null
    private val binding:ActivityCandidateAccBinding
    get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCandidateAccBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}