package com.venter.regodigital.userledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.venter.regodigital.databinding.ActivityUserDetBinding
import com.venter.regodigital.models.UserListRes
import com.venter.regodigital.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetActivity : AppCompatActivity()
{
    private var _binding:ActivityUserDetBinding? =  null
    private val binding:ActivityUserDetBinding
        get() = _binding!!
    private lateinit var user: UserListRes




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra("user")!!

        binding.whatsButton.setOnClickListener {
            whatsAppInitialization()
        }
        binding.dataSwapping.setOnClickListener {
            val intent = Intent(this, DataTransActivity::class.java)
            intent.putExtra("userId",user.id)
            intent.putExtra("userName",user.user_name)
            startActivity(intent)
        }
        binding.userReport.setOnClickListener {
            val intent = Intent(this, UserReport::class.java)
            intent.putExtra("userId",user.id)
            intent.putExtra("userName",user.user_name)
            startActivity(intent)
        }

    }

    private fun whatsAppInitialization() {
        try {
            val intent = Intent(this,WhatsAppActivity::class.java)
            intent.putExtra("userId",user.id)
            intent.putExtra("userName",user.user_name)
            startActivity(intent)


        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in UserDetActivity.kt whatsAppInitialization() is "+e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}