package com.venter.crm.bot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.crm.R
import com.venter.crm.databinding.ActivityBotBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BotActivity : AppCompatActivity() {
    private var _binding:ActivityBotBinding? = null
    private val binding:ActivityBotBinding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBotBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}