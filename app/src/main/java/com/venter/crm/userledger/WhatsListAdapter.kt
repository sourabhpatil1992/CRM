package com.venter.crm.userledger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.R
import com.venter.crm.databinding.LayoutWhatsapplistBinding
import com.venter.crm.models.WhatsAppAccList


class WhatsListAdapter(val whatInterface: WhatsListInterface, val whatsAcc: Int) :
    ListAdapter<WhatsAppAccList, WhatsListAdapter.WhatsHolder>(ComparatorDiffUtils()) {
    inner class WhatsHolder(val binding: LayoutWhatsapplistBinding, val cnt: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(acc: WhatsAppAccList) {

            binding.mobNo.text = acc.mob_no
            if (acc.acc_status == 0) {
                binding.accStatus.text = "Inactive"

            } else {
                binding.accStatus.text = "Active"

            }
            binding.linButtons.visibility = View.GONE


            val tColor = if (acc.acc_status == 0) ContextCompat.getColor(
                cnt,
                R.color.red
            ) else ContextCompat.getColor(cnt, R.color.green)

            binding.accStatus.setTextColor(tColor)

            if(acc.id == whatsAcc)
            {
                binding.linAcc.setBackgroundColor(ContextCompat.getColor(
                    cnt,
                    R.color.gray
                ))
            }





            binding.accView.setOnClickListener {
                whatInterface.accLongClick(acc)

            }


        }
    }

    class ComparatorDiffUtils : DiffUtil.ItemCallback<WhatsAppAccList>() {
        override fun areItemsTheSame(oldItem: WhatsAppAccList, newItem: WhatsAppAccList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WhatsAppAccList,
            newItem: WhatsAppAccList
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhatsHolder {
        val binding =
            LayoutWhatsapplistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WhatsHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: WhatsHolder, position: Int) {
        val acc = getItem(position)
        holder.bind(acc)
    }
}

interface WhatsListInterface {
    fun accLongClick(acc: WhatsAppAccList)

}
