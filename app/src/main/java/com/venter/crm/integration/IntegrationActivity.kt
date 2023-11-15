package com.venter.crm.integration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.venter.crm.R
import com.venter.crm.bot.BotActivity
import com.venter.crm.databinding.ActivityIntegrationBinding
import com.venter.crm.whatsTemp.MessagingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrationActivity : AppCompatActivity()
{
    private var _binding:ActivityIntegrationBinding? = null
    private val binding:ActivityIntegrationBinding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIntegrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.marketing.setOnClickListener {
            Toast.makeText(
                this,
                "This feature is not available in your plan. For use this service please upgrade your plan.",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.bot.setOnClickListener {
            val intent = Intent(this, BotActivity::class.java)
            startActivity(intent)
        }

        binding.mPlatform.setOnClickListener {
            val intent = Intent(this, MessagingActivity::class.java)
            startActivity(intent)
        }
        binding.kyc.setOnClickListener {
            Toast.makeText(
                this,
                "This feature is not available in your plan. For use this service please upgrade your plan.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}