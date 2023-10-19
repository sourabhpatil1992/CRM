package com.venter.crm.Candidate

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutCandidatelistBinding
import com.venter.crm.models.CandidateList
import com.venter.crm.utils.Constans.TAG

class CandidateListAdapter(val cnt: Context, val act: String,val can:CandidateClickInterface):
    ListAdapter<CandidateList, CandidateListAdapter.CandidateListHolder>(ComparatorDiffUtil()) {

    inner class CandidateListHolder(private val binding: LayoutCandidatelistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: CandidateList) {
            try {
                binding.candidateName.text = candidate.first_name+" "+candidate.middel_name+" "+candidate.last_name
                binding.mobNo.text = candidate.mobile_no
                binding.candidate.setOnClickListener {

                    var intent = Intent(cnt, CandidateCertificate::class.java)
                    intent.putExtra("id",candidate.id)
                    cnt.startActivity(intent)
                }

                binding.candidate.setOnLongClickListener{
                    can.delCandidate(candidate.id)
                    true
                }
            }catch (e:Exception)
            {
                Log.d(TAG,"Error in CandidateListAdapter.kt bind() is "+e.message)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<CandidateList>() {
        override fun areItemsTheSame(oldItem: CandidateList, newItem: CandidateList): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: CandidateList, newItem: CandidateList): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateListHolder {
        val binding =
            LayoutCandidatelistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateListHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}

interface CandidateClickInterface
{
    fun delCandidate(id:Int)
}