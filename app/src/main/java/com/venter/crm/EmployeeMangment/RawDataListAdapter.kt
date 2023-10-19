package com.venter.crm.EmployeeMangment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.R
import com.venter.crm.databinding.LayoutRawdataBinding
import com.venter.crm.empMang.RawDataDetActivity
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG

class RawDataListAdapter(val cnt: Context,val chkClick:chkListner,val chkVisible:Boolean=false,val empType:String) :ListAdapter<RawDataList,RawDataListAdapter.RawListHolder>(ComparatorDiffUtil()) {

    inner class RawListHolder(private val binding: LayoutRawdataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: RawDataList) {
            try {

                binding.txtSrNo.text = candidate.srNo.toString()
                binding.txtSrNo.text = candidate.srNo.toString()
                binding.txtName.text = candidate.candidate_name
                binding.txtMobNo.text = candidate.mob_no

                binding.checkBox.isChecked = candidate.selected

                binding.txtUpdate.text = candidate.update_on





                if(candidate.prosLevel != null) {
                    var desiredColor:Int = 0 //= ContextCompat.getColor(cnt, R.color.your_desired_color)

                    when (candidate.prosLevel) {
                        "Coming for visit" -> {
                            binding.linRawData.background = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp1)

                        }
                        "Visited" -> {
                            binding.linRawData.background = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp2)
                        }
                        "Demo" -> {
                            binding.linRawData.background = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp3)
                        }
                        "Not Interested" -> {
                            binding.linRawData.background = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp4)
                        }
                        "Information on call" -> {
                            binding.linRawData.background = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp5)
                        }
                        "Admission" -> {
                            binding.linRawData.background = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp6)
                        }
                        "Will Join/Inform" ->{
                            binding.linRawData.background = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp7)
                        }
                        else -> {

                        }
                    }









                }
                else  {
                    //binding.linRawData.setBackgroundColor(ContextCompat.getColor(cnt, R.color.white))
                }

                if(chkVisible) {
                  //  binding.txtSrNo.visibility = View.GONE
                  //  binding.view.visibility = View.GONE
                  //  binding.checkBox.visibility = View.VISIBLE
                }

                binding.linRawDataLin.setOnClickListener {
                    try {
                        val intent = Intent(cnt, RawDataDetActivity::class.java)
                        intent.putExtra("rawDataId", candidate.id)
                        cnt.startActivity(intent)
                    }
                    catch (e:Exception)
                    {
                        Log.d(TAG,"Error in RawDataListAdapter.kt bind() is "+e.message)
                    }
                }

                binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    try {

                        chkClick.chkSelect(candidate, binding.checkBox.isChecked)
                    }
                    catch (e:Exception)
                    {
                        //Log.d(TAG,"Error in RawDataListAdapter.kt binding.checkBox.setOnCheckedChangeListener() is "+e.message)
                    }
                }




            }catch (e:Exception)
            {
                Log.d(Constans.TAG,"Error in RawDataListAdapter .kt bind() is "+e.message)
            }
        }




    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<RawDataList>() {
        override fun areItemsTheSame(oldItem: RawDataList, newItem: RawDataList): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: RawDataList, newItem: RawDataList): Boolean {
           return true
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RawListHolder {
        val binding =
            LayoutRawdataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RawListHolder(binding)
    }

    override fun onBindViewHolder(holder: RawListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)

    }

}

interface chkListner{
    fun chkSelect(candidate: RawDataList, checked: Boolean)


}