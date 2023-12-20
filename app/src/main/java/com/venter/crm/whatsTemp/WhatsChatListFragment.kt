package com.venter.crm.whatsTemp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.crm.R
import com.venter.crm.databinding.FragmentWhatsChatListBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


class WhatsChatListFragment : Fragment() {
   private var _binding:FragmentWhatsChatListBinding? = null
    private val binding:FragmentWhatsChatListBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return try {
            _binding = FragmentWhatsChatListBinding.inflate(layoutInflater)

            binding.root
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in WhatsChatListFragment.kt is : ${e.message}")
            null
        }
    }

}