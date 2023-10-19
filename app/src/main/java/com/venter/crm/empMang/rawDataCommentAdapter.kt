package com.venter.crm.empMang

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.venter.crm.databinding.LayoutRawdatacommentBinding
import com.venter.crm.models.RawDataComment
import com.venter.crm.utils.Constans
import com.venter.crm.utils.TokenManger


class rawDataCommentAdapter(val empId:String, val update: CommentUpdate, val context: Context):ListAdapter<RawDataComment, rawDataCommentAdapter.RawCommentHolder>(
    ComparatorDiffUtil()
) {

    inner class RawCommentHolder(private val binding: LayoutRawdatacommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: RawDataComment) {
            try {


                binding.txtcommentedBY.text = candidate.empName
                binding.txtcomment.text = candidate.remark
                binding.txtcommentDate.text = candidate.commentDate
                if(empId != candidate.created_by.toString())
                    binding.linComment.visibility = View.VISIBLE
                else
                    binding.linComment.visibility = View.GONE

                if(candidate.edited == 1)
                binding.txtEdited.text = "Edited"
                else
                    binding.txtEdited.text = ""

                binding.linRawDataLin.setOnLongClickListener{
                    if(candidate.edited == 1)
                    {
                        val builder = AlertDialog.Builder(context)

                        // Set the dialog title
                        builder.setTitle("Choose an Option")
                        builder.setIcon(
                            ContextCompat.getDrawable(
                                context,
                                com.venter.crm.R.drawable.crm
                            )
                        )

                        // Set the options
                        builder.setItems(arrayOf("Edit Comment", "Edited Comment List")) { dialog, which ->
                            when (which) {
                                0 -> {
                                    update.updateComment(candidate)
                                }
                                1 -> {
                                    // "Edited Comment List" option selected
                                    // Handle Edited Comment List action here
                                    update.commentList(candidate)
                                }
                            }
                        }

                        // Create and show the dialog
                        val dialog = builder.create()
                        dialog.show()
                    }
                    else
                    {
                        if(empId != candidate.created_by.toString())
                        {
                            Snackbar.make(it, "You can't update this comment.", Snackbar.LENGTH_SHORT).show()
                        }
                        else
                        {
                            update.updateComment(candidate)
                        }

                    }



                    true
                }

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

interface CommentUpdate{
    fun updateComment(commentId: RawDataComment)
    fun commentList(commentId: RawDataComment)
}