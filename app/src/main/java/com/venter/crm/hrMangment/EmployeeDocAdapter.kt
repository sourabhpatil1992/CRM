package com.venter.crm.hrMangment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.crm.databinding.LayoutCanddocBinding
import com.venter.crm.models.DocumentUri
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG


class EmployeeDocAdapter(val cnt: Context) :
    ListAdapter<DocumentUri, EmployeeDocAdapter.DocHolder>(ComparatorDiffUtil()) {

    inner class DocHolder(private val binding: LayoutCanddocBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doc: DocumentUri) {
            val uri = if (doc.src == "Local") Uri.parse(doc.uri)
            else Uri.parse("${Constans.BASE_URL}assets/documents/employee/${doc.uri}")

            binding.txtDocName.text = if (doc.src == "Local") getFileNameFromUri(cnt, uri) else doc.uri

            val context = binding.imgDoc.context
            val fileExtension = context.contentResolver.getType(uri)

            val isPdf: Boolean = {
                if (doc.src == "Local") {
                    fileExtension?.startsWith("application/pdf") == true
                } else {
                    doc.uri.split(".")[1] == "pdf"
                }
            }()

            if (isPdf) {
                binding.imgDoc.setImageDrawable(ContextCompat.getDrawable(cnt, com.venter.crm.R.drawable.pdf_icon))
            } else {
                Picasso.get().load(uri).fit().centerCrop().into(binding.imgDoc)
            }

            binding.linDoc.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, if (isPdf) "application/pdf" else "image/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                try {
                    cnt.startActivity(intent)
                } catch (e: Exception) {
                    Log.d(TAG, "Error in EmployeeDocAdapter.kt bind() binding.linDoc.setOnClickListener: ${e.message}")
                }
            }
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

    class ComparatorDiffUtil : DiffUtil.ItemCallback<DocumentUri>() {

        override fun areItemsTheSame(oldItem: DocumentUri, newItem: DocumentUri): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: DocumentUri, newItem: DocumentUri): Boolean {
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