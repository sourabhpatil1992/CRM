package com.venter.regodigital.whatsTemp

import android.content.Context
import android.content.Intent
import android.net.Uri
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

class WhatsappTempAdapter(val cnt:Context):ListAdapter<WhatsappTemplateMsg,WhatsappTempAdapter.TempHolder>(ComparatorDiffUtil()) {
    inner class TempHolder(private val binding: LayoutWhatsapptempBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(temp: WhatsappTemplateMsg) {

            binding.txtSrNo.text = temp.id.toString()
            binding.txtMsg.setText(temp.tempMsg)

            when (temp.hederType) {
                "IMAGE" -> if (temp.headerPath != null && temp.headerPath != "") {

                    binding.imgWhats.setImageURI(null)
                    Picasso.get()
                        .load(temp.headerPath)
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(binding.imgWhats)
                    binding.imgWhats.visibility = View.VISIBLE
                }

                "VIDEO" -> {
                    binding.imgWhats.setImageURI(null)
                    binding.imgWhats.setImageResource(R.drawable.video_icon)
                    binding.imgWhats.visibility = View.VISIBLE
                    binding.imgWhats.setOnClickListener {
                        if (temp.headerPath != null && temp.headerPath != "") {
                            val builders = AlertDialog.Builder(cnt)
                            //set title for alert dialog
                            val vView = VideoView(cnt)
                            val uri = Uri.parse(temp.headerPath)
                            vView.setVideoURI(uri)
                            val mediaController = MediaController(cnt)
                            mediaController.setAnchorView(vView)

                            mediaController.setMediaPlayer(vView)
                            vView.setMediaController(mediaController)

                            builders.setView(vView)
                            val alertDialog: AlertDialog = builders.create()
                            alertDialog.setCancelable(true)
                            alertDialog.show()
                            vView.start()

                        } else {
                            Toast.makeText(cnt, "Video not found.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                "DOCUMENT" -> {
                    binding.imgWhats.setImageURI(null)
                    binding.imgWhats.setImageResource(R.drawable.doc_icon)
                    binding.imgWhats.visibility = View.VISIBLE
                    binding.imgWhats.setOnClickListener {
                        if (temp.headerPath != "" && temp.headerPath != null) {
                            val urlString = temp.headerPath
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.setPackage("com.android.chrome")
                            cnt.startActivity(intent)
                        } else {
                            Toast.makeText(cnt, "File not found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                else -> {

                }

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