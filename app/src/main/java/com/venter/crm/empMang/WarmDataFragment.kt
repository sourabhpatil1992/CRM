package com.venter.crm.empMang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.R
import com.venter.crm.databinding.FragmentWarmDataBinding
import com.venter.crm.models.CampData
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WarmDataFragment : Fragment() {
private var _binding:FragmentWarmDataBinding? = null
    private val binding:FragmentWarmDataBinding
        get() = _binding!!


    private var campData: ArrayList<CampData> = ArrayList()

    private lateinit var act: AdminRawDataActivity

    private lateinit var adapter: DataCampAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentWarmDataBinding.inflate(layoutInflater)

            adapter = DataCampAdapter()
            campData =ArrayList()

            act = activity as AdminRawDataActivity

            act.campData!!.forEach {
                if (it.dataType == "Warm")
                    campData.add(it)

            }

            adapter.submitList(campData)
            binding.rcView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcView.adapter = adapter


            binding.root
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in WarmDataFragment.kt is : ${e.message}")
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}