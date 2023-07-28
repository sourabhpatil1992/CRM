package com.venter.regodigital.whatsTemp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.databinding.ActivityWhatsappTemplatesBinding
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhatsappTemplates : AppCompatActivity() {

    private var _binding:ActivityWhatsappTemplatesBinding? = null
    private val binding:ActivityWhatsappTemplatesBinding
    get() = _binding!!

    private lateinit var adapter:WhatsappTempAdapter

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        try {
            _binding = ActivityWhatsappTemplatesBinding.inflate(layoutInflater)
            setContentView(binding.root)

            adapter = WhatsappTempAdapter(this)

            candidateViewModel.getWhatsMsgList()
            candidateViewModel.msgListResLiveData.observe(this) {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT)
                    }

                    is NetworkResult.Success -> {
                        adapter.submitList(it.data!!)
                        binding.rview.layoutManager =
                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                        binding.rview.adapter = adapter
                    }
                }
            }

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in WhatsappTemplate.kt is "+e.message)
        }
    }
}