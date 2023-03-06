package com.venter.regodigital.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.EmployeeMangment.ViewPagerAdapter
import com.venter.regodigital.databinding.ActivityEmpDashboardBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmpDashboard : AppCompatActivity() {
    private var _binding:ActivityEmpDashboardBinding? = null
    private val binding:ActivityEmpDashboardBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    public var rawData: List<RawDataList>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmpDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()


    }

    private fun getData() {
        try{

            candidateViewModel.getEmpRawData()
            candidateViewModel.allrawDataListResLiveData.observe(this)
            {

                when(it){
                    is NetworkResult.Loading ->{}
                    is NetworkResult.Error -> {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success ->{
                        rawData =it.data
                        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
                        binding.tabLayout.setupWithViewPager(binding.viewPager)

                    }
                }
                //candidateViewModel.allrawDataListResLiveData.removeObservers(this)

            }
        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in EmpDashboard.kt getData() is "+e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}