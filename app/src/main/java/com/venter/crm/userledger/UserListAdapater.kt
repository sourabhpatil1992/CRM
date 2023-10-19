package com.venter.crm.userledger

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

import com.venter.crm.databinding.LayoutUserlistBinding

import com.venter.crm.models.UserListRes
import com.venter.crm.utils.Constans

class UserListAdapate(val cnt: Context, val statusUser: statusUser) :
    ListAdapter<UserListRes, UserListAdapate.UserListHolder>(
        ComparatorDiffUtil()
    ) {
    inner class UserListHolder(private val binding: LayoutUserlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserListRes) {
            try {
                binding.txtUserName.text = user.user_name
                binding.txtDesignation.text = user.job_title
                binding.txtUserType.text = user.user_type
                binding.viewActive.isChecked = user.status == "1"
                    //binding.viewActive.setBackgroundColor(cnt.getColor(R.color.deActiveUser))

                Picasso.get()
                    .load(Constans.BASE_URL + "assets/userProfile/" + user.id + ".jpeg")
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(binding.imgProfile)

                /*binding.linUserStatus.setOnClickListener {
                    if (user.status == "1") {
                        user.status = "0"
                        statusUser.changeUserStatus(user.id, 0)
                        binding.viewActive.setBackgroundColor(cnt.getColor(R.color.deActiveUser))

                    } else {
                        user.status = "1"
                        statusUser.changeUserStatus(user.id, 1)
                        binding.viewActive.setBackgroundColor(cnt.getColor(R.color.activeUser))

                    }

                }*/
                binding.viewActive.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked)
                    {
                        user.status = "1"
                        statusUser.changeUserStatus(user.id, 1)
                       // binding.viewActive.setBackgroundColor(cnt.getColor(R.color.activeUser))
                    }
                    else
                    {
                        user.status = "0"
                        statusUser.changeUserStatus(user.id, 0)
                        //binding.viewActive.setBackgroundColor(cnt.getColor(R.color.deActiveUser))

                    }


                }

                binding.linviewUser.setOnClickListener{
                    val intent =Intent(cnt,UserDetActivity::class.java)
                    intent.putExtra("user", user)
                    cnt.startActivity(intent)
                }
            } catch (e: Exception) {
                Log.d(Constans.TAG, "Error in UserListAdapate.kt bind() is " + e.message)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<UserListRes>() {
        override fun areItemsTheSame(oldItem: UserListRes, newItem: UserListRes): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: UserListRes, newItem: UserListRes): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val binding =
            LayoutUserlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }

}

interface statusUser {
    fun changeUserStatus(userId: Int, status: Int)
}

