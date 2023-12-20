package com.venter.crm.whatsTemp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.crm.R
import com.venter.crm.databinding.ActivityWhatsAppChatBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WhatsAppChat : AppCompatActivity() {
    private var _binding: ActivityWhatsAppChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWhatsAppChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}