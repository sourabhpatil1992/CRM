package com.venter.crm.integration

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.R
import com.venter.crm.databinding.LayoutEmaillistBinding
import com.venter.crm.models.ConfigMailModel
import com.venter.crm.models.ProsSubType

class EmailListAdapter(val cnt: Context,val emailConf:EmailConfInterFace) : ListAdapter<ConfigMailModel,EmailListAdapter.EmailListHolder>(ComparatorDiffUtil()) {
    inner class EmailListHolder (private val binding:LayoutEmaillistBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(emailList : ConfigMailModel)
        {
            binding.email.text = emailList.emailId
              if (emailList.status == 1)
              {
                  binding.accStatus.text ="Active"
                  binding.accStatus.setTextColor(ContextCompat.getColor(cnt,R.color.activeUser))
              } else
              {
                  binding.accStatus.text = "Inactive"
                  binding.accStatus.setTextColor(ContextCompat.getColor(cnt,R.color.deActiveUser))
              }
            binding.linAcc.setOnLongClickListener {
                emailConf.updateEmailConf(emailList)
                true
            }
        }
    }
    class ComparatorDiffUtil : DiffUtil.ItemCallback<ConfigMailModel>()
    {
        override fun areItemsTheSame(oldItem: ConfigMailModel, newItem: ConfigMailModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ConfigMailModel,
            newItem: ConfigMailModel
        ): Boolean {
            return oldItem == newItem
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailListHolder {
        val binding = LayoutEmaillistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EmailListHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}

interface EmailConfInterFace{
    fun updateEmailConf(data:ConfigMailModel)
}