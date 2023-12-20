package com.venter.crm.empMang

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.R
import com.venter.crm.databinding.LayoutDatalistBinding
import com.venter.crm.models.ProsSubType
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans.TAG

class DataListAdapter(val cnt: Context, val prosSubType: List<ProsSubType>? = null) :
    ListAdapter<RawDataList, DataListAdapter.RawListHolder>(ComparatorDiffUtil()) {

    inner class RawListHolder(private val binding: LayoutDatalistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: RawDataList) {
            try {
                //Log.d(TAG, prosSubType.toString())

                binding.txtSrNo.text = candidate.srNo.toString()
                binding.txtSrNo.text = candidate.srNo.toString()
                binding.txtName.text = candidate.candidate_name
                binding.txtMobNo.text = candidate.mob_no


                binding.txtUpdate.text = candidate.update_on

                val dynamicColor = prosSubType?.firstOrNull { it.subType == candidate.prosLevel }?.color?.toInt()
                    ?: getColor(cnt, R.color.whats_back)

                val drawable = ContextCompat.getDrawable(cnt, R.drawable.card_shadow_temp)
                if (drawable is LayerDrawable) {
                    (drawable.getDrawable(0) as? GradientDrawable)?.setColor(dynamicColor)
                }

                binding.linRawData.background = drawable


                /* if(candidate.prosLevel != null) {
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









                 }*/




                binding.linRawDataLin.setOnClickListener {
                    try {
                        val intent = Intent(cnt, RawDataDetActivity::class.java)
                        intent.putExtra("rawDataId", candidate.id)
                        cnt.startActivity(intent)
                    } catch (e: Exception) {
                        Log.d(TAG, "Error in DataListAdapter.kt bind() is " + e.message)
                    }
                }


            } catch (e: Exception) {
                Log.d(TAG, "Error in DataListAdapter.kt bind() is " + e.message)
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
            LayoutDatalistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RawListHolder(binding)
    }

    override fun onBindViewHolder(holder: RawListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)

    }

}



