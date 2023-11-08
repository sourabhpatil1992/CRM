package com.venter.crm.hrMangment

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutCanddocBinding


class EmployeeDocAdapter(val cnt:Context) : ListAdapter<Uri, EmployeeDocAdapter.DocHolder>(ComparatorDiffUtil()) {

    inner class DocHolder(private val binding: LayoutCanddocBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doc: Uri) {
            binding.txtDocName.text = getFileNameFromUri(cnt,doc)
            val context = binding.imgDoc.context
            val fileExtension = context.contentResolver.getType(doc)

            if (fileExtension != null && fileExtension.startsWith("image/"))
                binding.imgDoc.setImageURI(doc)

            else
                binding.imgDoc.setImageDrawable(ContextCompat.getDrawable(context,com.venter.crm.R.drawable.pdf_icon))

        }

        fun getFileNameFromUri(context: Context, uri: Uri): String? {
            var fileName: String? = null
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameColumnIndex != -1) {
                        val displayName = it.getString(displayNameColumnIndex)
                        if (!displayName.isNullOrBlank()) {
                            fileName = displayName
                        }
                    }
                }
            }
            return fileName
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Uri>() {

        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocHolder {
        val binding =
            LayoutCanddocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocHolder(binding)
    }

    override fun onBindViewHolder(holder: DocHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }


}