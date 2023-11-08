package com.venter.crm.hrMangment

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.R
import com.venter.crm.databinding.FragmentCreateEmpBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateEmpFragment : Fragment() {

    private var _binding: FragmentCreateEmpBinding? = null
    private val binding: FragmentCreateEmpBinding
        get() = _binding!!

    private var profileUri: Uri? = null
    private var documentUri: ArrayList<Uri?> = ArrayList()

    private lateinit var adapter: EmployeeDocAdapter

    private var contract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                profileUri = uri
                binding.imgProfile.setImageURI(null)
                binding.imgProfile.setImageURI(profileUri)
            }
        }
    private var contractDocument =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uri: List<Uri>? ->
            if (uri != null) {
                uri.forEach {
                    val mimeType = getMimeType(requireContext().contentResolver, it)
                    if (mimeType.startsWith("image/") || mimeType == "application/pdf") {
                        documentUri.add(it)
                    }
                }


                setDocumentRc()
            }
        }

    private fun getMimeType(contentResolver: ContentResolver, uri: Uri): String {
        return contentResolver.getType(uri) ?: ""
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentCreateEmpBinding.inflate(layoutInflater)

            adapter = EmployeeDocAdapter(requireContext())
            cascadingView()

            binding.btnDp.setOnClickListener {
               contract.launch("image/*")
            }
            binding.btnAddDoc.setOnClickListener {
                contractDocument.launch(arrayOf("*/*"))

            }


            binding.root

        } catch (e: Exception) {
            Log.d(TAG, "Error in CreateEmpFragment.kt is : ${e.message}")
            null
        }
    }

    private fun cascadingView() {

        binding.relCandidateDet.setOnClickListener {

            if (binding.linCandidateDet.visibility == View.VISIBLE) {
                binding.linCandidateDet.visibility = View.GONE
                binding.btnEmpDet.text = getString(R.string.plus)
            } else {
                binding.linCandidateDet.visibility = View.VISIBLE
                binding.btnEmpDet.text = getString(R.string.minus)
            }
        }
        binding.relJoinJob.setOnClickListener {

            if (binding.linJoiningJob.visibility == View.VISIBLE) {
                binding.linJoiningJob.visibility = View.GONE
                binding.btnJoiningDet.text = getString(R.string.plus)
            } else {
                binding.linJoiningJob.visibility = View.VISIBLE
                binding.btnJoiningDet.text = getString(R.string.minus)
            }
        }

        binding.relCurrentJob.setOnClickListener {

            if (binding.linCurrentJobDet.visibility == View.VISIBLE) {
                binding.linCurrentJobDet.visibility = View.GONE
                binding.btnCurrentJob.text = getString(R.string.plus)

            } else {
                binding.linCurrentJobDet.visibility =  View.VISIBLE
                binding.btnCurrentJob.text = getString(R.string.minus)
            }
        }
        binding.relDocuments.setOnClickListener {

            if (binding.linDocumets.visibility == View.VISIBLE) {
                binding.linDocumets.visibility = View.GONE
                binding.btnDoc.text = getString(R.string.plus)

            } else {
                binding.linDocumets.visibility =  View.VISIBLE
                binding.btnDoc.text = getString(R.string.minus)
            }
        }

        binding.chkAppAccess.setOnCheckedChangeListener { _, isChecked ->

            binding.linAppAccess.visibility =
                if (isChecked)
                    View.VISIBLE
                else
                    View.GONE
        }

    }

    private fun setDocumentRc() {
        try {
            adapter.submitList(documentUri)
            binding.rcView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            binding.rcView.adapter = adapter

            binding.rcView.visibility = if(documentUri.isNotEmpty())
                 View.VISIBLE
            else
                 View.GONE

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in CreateEmpFragment.kt setDocumentRc() is : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}