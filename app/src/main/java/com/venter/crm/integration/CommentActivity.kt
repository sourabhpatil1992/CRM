package com.venter.crm.integration


import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.venter.crm.databinding.ActivityCommentBinding
import com.venter.crm.databinding.LayoutAddsubprosBinding
import com.venter.crm.models.ProsSubType
import com.venter.crm.models.SystemConf
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import yuku.ambilwarna.AmbilWarnaDialog


@AndroidEntryPoint
class CommentConfActivity : AppCompatActivity(),ProsSubTypeInterface {

    private var _binding: ActivityCommentBinding? = null
    private val binding: ActivityCommentBinding
        get() = _binding!!


    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var prosSubTypeList:ArrayList<ProsSubType> = ArrayList()


    private lateinit var adapter : ProspectSubTypeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            prosSubTypeList = ArrayList()

            adapter =  ProspectSubTypeAdapter(this)
            getData()

            binding.btnSubmit.setOnClickListener {
                val conf = SystemConf(binding.commentTemplate.text.toString().replaceFirst("\\s+$".toRegex(), ""),prosSubTypeList,binding.prosType.text.toString().replaceFirst("\\s+$".toRegex(), ""))
                candidateViewModel.updateConfiguration(conf)
                 candidateViewModel.stringResData.observe(this) {
                    binding.progressbar.visibility = View.GONE
                    when(it)
                    {
                        is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> {
                            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                            candidateViewModel.stringResData.removeObservers(this)
                        }
                        is NetworkResult.Success ->{
                            Toast.makeText(this,"Configuration Updated Successfully.",Toast.LENGTH_SHORT).show()
                            this.finish()
                        }

                    }
                }
            }

            binding.btnAdd.setOnClickListener {
                addSubProsType()
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in CommentConfActivity.kt is : ${e.message}")
        }
    }

    private fun getData() {
        try {
            candidateViewModel.getConfiguration()
            candidateViewModel.systemConfDataLiveData.observe(this)
            {
                binding.progressbar.visibility =View.GONE
                when(it)
                {
                    is NetworkResult.Loading -> binding.progressbar.visibility =View.VISIBLE
                    is NetworkResult.Error -> {
                        Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                        this.finish()
                    }
                    is NetworkResult.Success ->{
                        //Data Fetch from the Server now set it
                        binding.prosType.setText(it.data!!.prosType.toString())
                        binding.commentTemplate.setText(it.data.commentTemp.toString())

                        prosSubTypeList = ArrayList()
                        prosSubTypeList = it.data.prosSubType as ArrayList<ProsSubType>
                        setRCView()
                    }
                }
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in CommentActivity.kt getData() is : ${e.message}")
        }
    }

    //Here we set the RcView of Prospect Sub Type
    private fun setRCView() {
        adapter.submitList(prosSubTypeList)
        val layoutManager = LinearLayoutManager(this)
        binding.rcView.layoutManager = layoutManager

        binding.rcView.adapter = adapter
    }

    private fun addSubProsType() {
        try {
            val bindingMsg = LayoutAddsubprosBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(this)
            builder.setView(bindingMsg.root)
            builder.setTitle("Prospect Sub-Type")
            builder.setIcon(
                ContextCompat.getDrawable(
                    this,
                    com.venter.crm.R.drawable.crm
                )
            )
            bindingMsg.colorView.setOnClickListener {
                showColorPickerDialog(bindingMsg.colorView)
            }

            builder.setPositiveButton("OK") { _, _ ->
                prosSubTypeList.add(ProsSubType(bindingMsg.subType.text.toString(),(bindingMsg.colorView.background as ColorDrawable).color.toString()))
                adapter.submitList(prosSubTypeList)
                val layoutManager = LinearLayoutManager(this)
                binding.rcView.layoutManager = layoutManager

                binding.rcView.adapter = adapter
            }
            builder.setNegativeButton("Cancel") { _, _ ->
                // Handle Cancel button click if needed
            }
            val alertDialog = builder.create()

            alertDialog.show()


        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in CommentActivity.kt addSubProsType() is : ${e.message}")
        }
    }


    private fun showColorPickerDialog(colorView: View) {
        val initialColor = (colorView.background as ColorDrawable).color

        AmbilWarnaDialog(this, initialColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog?) {
            }

            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                colorView.setBackgroundColor(color)
            }
        }).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun subProcessPress(data: ProsSubType) {
        prosSubTypeList.remove(data)
        adapter.submitList(prosSubTypeList)
        val layoutManager = LinearLayoutManager(this)
        binding.rcView.layoutManager = layoutManager

        binding.rcView.adapter = adapter

    }
}