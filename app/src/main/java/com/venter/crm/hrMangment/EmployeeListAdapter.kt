package com.venter.crm.hrMangment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.venter.crm.databinding.LayoutEmpBinding
import com.venter.crm.models.EmployeeList
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG

/**************************************************************
 *
 * EmployeeListAdapter Is created for show the Employee List and on click get the employee details with certificate
 * Created By : Sourabh Patil
 * Create On : 09 Nov 2023
 *
 ***************************************************************/

class EmployeeListAdapter(val empInter:EmployeeListInterface) :ListAdapter<EmployeeList,EmployeeListAdapter.EmpListHolder>(ComparatorDiffUtil())
{
    inner class EmpListHolder(private val binding: LayoutEmpBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emp: EmployeeList) {
            try {
                Picasso.get()
                    .load(Constans.BASE_URL + "assets/profile/Emp_" + emp.id + ".jpeg")
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(binding.imgProfile)

                binding.txtEmpId.text = emp.empId
                val empName = "${emp.fName ?: ""} ${emp.mName ?: ""} ${emp.lName ?: ""}"
                binding.txtUserName.text = empName
                binding.txtDesignation.text = emp.cJob

                binding.linviewUser.setOnClickListener {
                    empInter.empDet(emp.id)
                }
            }
            catch (e:Exception)
            {
                Log.d(TAG,"Error in EmployeeListAdapter.kt is : ${e.message}")
            }

        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<EmployeeList>()
    {
        override fun areItemsTheSame(oldItem: EmployeeList, newItem: EmployeeList): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: EmployeeList, newItem: EmployeeList): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpListHolder {
        val binding= LayoutEmpBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  EmpListHolder(binding)
    }

    override fun onBindViewHolder(holder: EmpListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}

interface EmployeeListInterface
{
    fun empDet(id:Int)
}