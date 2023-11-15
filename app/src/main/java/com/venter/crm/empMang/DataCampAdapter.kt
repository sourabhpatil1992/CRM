package com.venter.crm.empMang

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutRawdatacampBinding
import com.venter.crm.models.CampData


class DataCampAdapter(val campInt:CampaignInterface) : ListAdapter<CampData,DataCampAdapter.DataCampHolder>(ComparatorDiffUtil()) {
    inner class DataCampHolder(private val binding: LayoutRawdatacampBinding) :RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: CampData)
        {
            binding.campName.text = data.campName
            binding.campDet.text = data.campDet
            binding.dataCnt.text = data.dataCount.toString()
            binding.intCount.text = data.interestedCount.toString()
            binding.notIntCnt.text = data.notInterestedCount.toString()
            binding.notResCnt.text = data.notRespondingCount.toString()
            binding.paidCnt.text = data.paidCount.toString()


            binding.linRawData.setOnClickListener{
                binding.linButtons.visibility = if(binding.linButtons.visibility == View.GONE)
                    View.VISIBLE
                else
                    View.GONE
            }

            binding.btnShowData.setOnClickListener {
                campInt.capData(data.camp_id)
            }
            binding.btnDeleteCamp.setOnClickListener {
                campInt.removeData(data.camp_id)
            }

        }
    }
    class ComparatorDiffUtil : DiffUtil.ItemCallback<CampData>() {
        override fun areItemsTheSame(oldItem: CampData, newItem: CampData): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: CampData, newItem: CampData): Boolean {
            return false
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataCampHolder {
        val binding =
            LayoutRawdatacampBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataCampHolder(binding)
    }

    override fun onBindViewHolder(holder: DataCampHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}

interface CampaignInterface
{
    fun capData(campId:Int)
    fun removeData(campId:Int)
}