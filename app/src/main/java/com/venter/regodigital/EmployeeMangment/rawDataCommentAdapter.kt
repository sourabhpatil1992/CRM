package com.venter.regodigital.EmployeeMangment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.regodigital.databinding.LayoutRawdataBinding
import com.venter.regodigital.databinding.LayoutRawdatacommentBinding
import com.venter.regodigital.models.RawDataComment
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG


class rawDataCommentAdapter(val empId:String):ListAdapter<RawDataComment,rawDataCommentAdapter.RawCommentHolder>(ComparatorDiffUtil()) {
    inner class RawCommentHolder(private val binding: LayoutRawdatacommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: RawDataComment) {
            try {
                Log.d(TAG,"--"+empId)

                binding.txtcommentedBY.text = candidate.empName
                binding.txtcomment.text = candidate.remark
                binding.txtcommentDate.text = candidate.commentDate
                if(empId != candidate.created_by.toString())
                    binding.linComment.visibility = View.VISIBLE
                else
                    binding.linComment.visibility = View.GONE

//                binding.linRawDataLin.setOnClickListener {
//                    try {
//                        val intent = Intent(cnt, RawDataDetActivity::class.java)
//                        intent.putExtra("rawDataId", candidate.id)
//                        cnt.startActivity(intent)
//                    }
//                    catch (e:Exception)
//                    {
//                        Log.d(Constans.TAG,"Error in RawDataListAdapter.kt bind() is "+e.message)
//                    }
//                }

            }catch (e:Exception)
            {
                Log.d(Constans.TAG,"Error in rawDataCommentAdapter.kt bind() is "+e.message)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<RawDataComment>() {
        override fun areItemsTheSame(oldItem: RawDataComment, newItem: RawDataComment): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: RawDataComment, newItem: RawDataComment): Boolean {
            return false
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RawCommentHolder {
        val binding =
            LayoutRawdatacommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RawCommentHolder(binding)
    }

    override fun onBindViewHolder(holder: RawCommentHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}