package com.venter.crm.empMang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.crm.R
import com.venter.crm.databinding.FragmentCampRawDataBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CampRawDataFragment : Fragment() {
    private  var _binding:FragmentCampRawDataBinding? = null
    private val binding:FragmentCampRawDataBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        return try{
            _binding = FragmentCampRawDataBinding.inflate(layoutInflater)


            binding.root

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in CampRawDataFragment.kt is  : ${e.message}")
            return null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}