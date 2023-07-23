package com.venter.regodigital.userledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.venter.regodigital.R
import com.venter.regodigital.databinding.ActivityUserDetBinding
import com.venter.regodigital.models.UserListRes
import com.venter.regodigital.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetActivity : AppCompatActivity() {
    private var _binding:ActivityUserDetBinding? =  null
    private val binding:ActivityUserDetBinding
        get() = _binding!!
    private lateinit var user: UserListRes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra("user")!!
        Log.d(TAG,"---"+user.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}