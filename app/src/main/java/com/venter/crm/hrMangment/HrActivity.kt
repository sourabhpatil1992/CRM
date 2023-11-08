package com.venter.crm.hrMangment


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.venter.crm.R

import com.venter.crm.databinding.ActivityHrBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HrActivity : AppCompatActivity() {

    private var _binding:ActivityHrBinding? = null
    private val binding:ActivityHrBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHrBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try{
            binding.bottomNavi.itemIconTintList = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hrNavHostFragment) as NavHostFragment
        val navController = findNavController(R.id.hrNavHostFragment)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_hr)
        navController.graph = graph
        val bNavController = navHostFragment.findNavController()
        binding.bottomNavi.setupWithNavController(bNavController)

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in HrActivity.kt is  : ${e.message}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =  null
    }

}
