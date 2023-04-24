package com.venter.regodigital.EmployeeMangment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.regodigital.databinding.LayoutRawdataBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG

class RawDataListAdapter(val cnt: Context,val chkClick:chkListner,val chkVisible:Boolean=false) :ListAdapter<RawDataList,RawDataListAdapter.RawListHolder>(ComparatorDiffUtil()) {

    inner class RawListHolder(private val binding: LayoutRawdataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: RawDataList) {
            try {
                binding.txtSrNo.text = candidate.srNo.toString()
                binding.txtSrNo.text = candidate.srNo.toString()
                binding.txtName.text = candidate.candidate_name
                binding.txtMobNo.text = candidate.mob_no

                binding.checkBox.isChecked = candidate.selected

                if(chkVisible) {
                    binding.txtSrNo.visibility = View.GONE
                    binding.view.visibility = View.GONE
                    binding.checkBox.visibility = View.VISIBLE
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