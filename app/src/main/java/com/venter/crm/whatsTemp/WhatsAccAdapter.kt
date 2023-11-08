package com.venter.crm.whatsTemp

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

class WhatsAccAdapter(val whatInterface: WhatsAccInterface) :
    ListAdapter<WhatsAppAccList, WhatsAccAdapter.WhatsHolder>(ComparatorDiffUtils()) {
    inner class WhatsHolder(val binding: LayoutWhatsapplistBinding, val cnt: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(acc: WhatsAppAccList) {

            binding.mobNo.text = acc.mob_no
            if (acc.acc_status == 0) {
                binding.accStatus.text = "Inactive"
                binding.linButtons.visibility = View.GONE
            } else {
                binding.accStatus.text = "Active"
                binding.linButtons.visibility = View.VISIBLE
            }


            val tColor = if (acc.acc_status == 0) ContextCompat.getColor(
                cnt,
                R.color.red
            ) else ContextCompat.getColor(cnt, R.color.green)
            binding.accStatus.setTextColor(tColor)

            binding.accView.setOnClickListener {
                whatInterface.accLongClick(acc)

            }

            binding.btnLogOut.setOnClickListener {
                whatInterface.logOut(acc)
            }

            binding.btnChat.setOnClickListener {
                whatInterface.chatWhats(acc)
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

interface WhatsAccInterface {
    fun accLongClick(acc: WhatsAppAccList)
    fun logOut(acc: WhatsAppAccList)
    fun chatWhats(acc: WhatsAppAccList)

}