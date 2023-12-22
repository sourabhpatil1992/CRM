package com.venter.crm.integration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutProsSubTypeBinding


import com.venter.crm.models.ProsSubType



class ProspectSubTypeAdapter(private val inter:ProsSubTypeInterface) : ListAdapter<ProsSubType,ProspectSubTypeAdapter.ProspectHolder>(ComparatorDiffUtil()) {

    inner class ProspectHolder(private val binding: LayoutProsSubTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(prosSubType: ProsSubType)
            {
                binding.prosSubType.text = prosSubType.subType
                binding.subColor.setBackgroundColor(prosSubType.color.toString().toInt())
                binding.prospectView.setOnLongClickListener {
                inter.subProcessPress(prosSubType)
                    true
                }
            }
        }
    class ComparatorDiffUtil : DiffUtil.ItemCallback<ProsSubType>()
    {
        override fun areItemsTheSame(oldItem: ProsSubType, newItem: ProsSubType): Boolean {
            return oldItem.subType == newItem.subType
        }

        override fun areContentsTheSame(oldItem: ProsSubType, newItem: ProsSubType): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProspectHolder {
        val binding = LayoutProsSubTypeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProspectHolder(binding)
    }

    override fun onBindViewHolder(holder: ProspectHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}

interface ProsSubTypeInterface
{
    fun subProcessPress(data:ProsSubType)
}