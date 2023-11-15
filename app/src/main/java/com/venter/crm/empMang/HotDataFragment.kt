package com.venter.crm.empMang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.R
import com.venter.crm.databinding.FragmentHotDataBinding
import com.venter.crm.models.CampData
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotDataFragment : Fragment(),CampaignInterface {
   private var _binding: FragmentHotDataBinding? = null
    private val binding: FragmentHotDataBinding
        get()  = _binding!!

    private var campData: ArrayList<CampData> = ArrayList()

    private lateinit var act: AdminRawDataActivity

    private lateinit var adapter: DataCampAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentHotDataBinding.inflate(layoutInflater)

            adapter = DataCampAdapter(this)

            act = activity as AdminRawDataActivity

            campData =ArrayList()

            act.campData!!.forEach {
                if (it.dataType == "Hot")
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
            Log.d(TAG,"Error in HotDataFragment.kt is : ${e.message}")
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =  null
    }

    override fun capData(campId: Int) {
        act.showData(campId)
    }

    override fun removeData(campId: Int) {
        act.deleteData(campId)
    }

}