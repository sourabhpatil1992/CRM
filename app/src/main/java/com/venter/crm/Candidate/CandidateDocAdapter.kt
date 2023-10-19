package com.venter.crm.Candidate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutCanddocBinding

class CandidateDocAdapter(val cnt:onClick):ListAdapter<String,CandidateDocAdapter.CandidateDocHolder>(ComparatorDiffUtil()) {

    inner class  CandidateDocHolder(private val binding:LayoutCanddocBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(docName:String)
        {
            binding.txtDocName.text = docName
            binding.linDoc.setOnClickListener {
                cnt.docClick(docName)
            }

        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
           return false
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateDocHolder {
        val binding =
            LayoutCanddocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateDocHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateDocHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }


}

interface onClick{
    fun docClick(docName: String)
}