package com.venter.crm.EmployeeMangment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.ActivityWorkingHourseAcivityBinding
import com.venter.crm.models.WorkingHrs
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class WorkingHourseAcivity : AppCompatActivity(),workingSheduleChange
{
    private var _binding:ActivityWorkingHourseAcivityBinding? =null
    private val binding:ActivityWorkingHourseAcivityBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()


    private lateinit var  adapter: TimeHrsAdapter
    //var sheduleList :ArrayList<WorkingHrs> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        _binding = ActivityWorkingHourseAcivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

       try {
            adapter = TimeHrsAdapter(this,this)

            candidateViewModel.getWorkingHrsData()

            candidateViewModel.workHrsResLiveData.observe(this) {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                        .show()
                    is NetworkResult.Success -> {
                        adapter.submitList(it.data)
                        binding.rcView.layoutManager =
                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcView.adapter = adapter
                    }
                }
            }


        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in WorkingHorseActivity.kt is "+e.message)
        }



    }

    override fun changeWorkingShedule(day: String, startTime: String, endTime: String) {

        //Check the End Time is Larger than the Start Time
        val dateFormat = SimpleDateFormat("hh:mm:ss")
        val sTime = dateFormat.parse(startTime);
        val eTime = dateFormat.parse(endTime)
        if(sTime>eTime)
            Toast.makeText(this,"Please check the end time.",Toast.LENGTH_SHORT).show()
        else {
            candidateViewModel.setWorkingHrsData(WorkingHrs(day, startTime, endTime))
            candidateViewModel.stringResData.observe(this){
                binding.progressbar.visibility = View.GONE
                when(it)
                {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> {Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()}
                    is NetworkResult.Success-> Toast.makeText(this,it.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}