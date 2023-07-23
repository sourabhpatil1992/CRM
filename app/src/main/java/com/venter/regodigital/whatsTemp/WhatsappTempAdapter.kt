package com.venter.regodigital.whatsTemp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.regodigital.R
import com.venter.regodigital.databinding.LayoutWhatsapptempBinding
import com.venter.regodigital.models.WhatsappTemplateMsg
import com.venter.regodigital.utils.Constans.BASE_URL
import com.venter.regodigital.utils.Constans.TAG

class WhatsappTempAdapter(val cnt:Context):ListAdapter<WhatsappTemplateMsg,WhatsappTempAdapter.TempHolder>(ComparatorDiffUtil()) {
    inner class TempHolder(private val binding: LayoutWhatsapptempBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(temp: WhatsappTemplateMsg) {

            binding.txtSrNo.text = temp.id.toString()
            binding.txtMsg.setText(temp.tempMsg)


            if(temp.header_name != null && temp.header_name !="")
            {
                binding.imgWhats.setImageURI(null)
                val filetype = temp.header_name.toString().split("\\.".toRegex())
                when(filetype[1])
                {
                    "pdf" ->{
                        binding.imgWhats.setImageResource(R.drawable.doc_icon)
                    }
                    "mp4" ->{
                        binding.imgWhats.setImageResource(R.drawable.video_icon)
                    }
                    else ->{
                        Picasso.get()
                            .load(BASE_URL+"assets/whatstemp/"+temp.header_name)
                            .fit()
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(binding.imgWhats)
                    }

                }

                binding.imgWhats.visibility = View.VISIBLE
            }
            binding.linWhatsTemp.setOnClickListener {
                val intent = Intent(cnt, EditWhatsTemp::class.java)
                intent.putExtra("temp",temp)
                cnt.startActivity(intent)
            }
            binding.cardWhatsTemp.setOnClickListener {
                val intent = Intent(cnt, EditWhatsTemp::class.java)
                intent.putExtra("temp",temp)
                cnt.startActivity(intent)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<WhatsappTemplateMsg>()
    {
        override fun areItemsTheSame(
            oldItem: WhatsappTemplateMsg,
            newItem: WhatsappTemplateMsg
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: WhatsappTemplateMsg,
            newItem: WhatsappTemplateMsg
        ): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempHolder {
        val binding = LayoutWhatsapptempBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TempHolder(binding)
    }

    override fun onBindViewHolder(holder: TempHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}