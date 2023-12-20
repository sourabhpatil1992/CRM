package com.venter.crm.integration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.venter.crm.R
import com.venter.crm.databinding.ActivityConfigrationBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationActivity : AppCompatActivity() {
    private var _binding:ActivityConfigrationBinding? = null
    private val binding:ActivityConfigrationBinding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityConfigrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try{

            binding.commentConf.setOnClickListener {
                val intent = Intent(this,CommentConfActivity::class.java)
                startActivity(intent)
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in ConfigurationActivity.kt is : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}