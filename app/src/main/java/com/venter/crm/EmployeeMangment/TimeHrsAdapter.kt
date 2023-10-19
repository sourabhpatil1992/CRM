package com.venter.crm.EmployeeMangment

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutWorkinghrsBinding
import com.venter.crm.models.WorkingHrs
import java.util.*

class TimeHrsAdapter(val cnt:Context,val workingShedule:workingSheduleChange): ListAdapter<WorkingHrs, TimeHrsAdapter.DataHolder>(ComparatorDiffUtil())
{
    inner class DataHolder(private val binding: LayoutWorkinghrsBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(shedule:WorkingHrs,cnt: Context){

                binding.weekDay.text = shedule.weekDay
                binding.startTime.text = shedule.startTime
                binding.endTime.text = shedule.endTime

                binding.startTime.setOnClickListener {
                    val mTimePicker: TimePickerDialog
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                    val minute = mcurrentTime.get(Calendar.MINUTE)

                    mTimePicker = TimePickerDialog(cnt,
                        { view, hourOfDay, minute ->
                            binding.startTime.setText(String.format("%02d:%02d:00", hourOfDay, minute))
                            workingShedule.changeWorkingShedule(binding.weekDay.text.toString(),binding.startTime.text.toString(),binding.endTime.text.toString())
                        }, hour, minute, true)
                    mTimePicker.show()

                }
                binding.endTime.setOnClickListener {
                    val mTimePicker: TimePickerDialog
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                    val minute = mcurrentTime.get(Calendar.MINUTE)

                    mTimePicker = TimePickerDialog(cnt,
                        { view, hourOfDay, minute -> binding.endTime.setText(String.format("%02d:%02d:00", hourOfDay, minute))
                            workingShedule.changeWorkingShedule(binding.weekDay.text.toString(),binding.startTime.text.toString(),binding.endTime.text.toString())
                        }, hour, minute, true)
                    mTimePicker.show()

                }


            }
        }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<WorkingHrs>() {
        override fun areItemsTheSame(oldItem: WorkingHrs, newItem: WorkingHrs): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: WorkingHrs, newItem: WorkingHrs): Boolean {
            return true
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val binding =
            LayoutWorkinghrsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataHolder(binding)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp,cnt)
    }
}

interface workingSheduleChange
{
    fun changeWorkingShedule(day:String,startTime:String,endTime:String)
}