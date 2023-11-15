package com.venter.crm.whatsTemp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.crm.R
import com.venter.crm.databinding.ActivityMessagingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MessagingActivity : AppCompatActivity() {
    private var _binding:ActivityMessagingBinding? = null
    private val binding:ActivityMessagingBinding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMessagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.whatsapp.setOnClickListener {
            val intent = Intent(this,WhatsAppAccounts::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}