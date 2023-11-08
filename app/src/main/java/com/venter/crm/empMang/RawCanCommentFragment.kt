package com.venter.crm.empMang


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.R
import com.venter.crm.databinding.FragmentRawCanCommentBinding
import com.venter.crm.models.RawCandidateData
import com.venter.crm.models.RawDataComment
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RawCanCommentFragment : Fragment(), CommentUpdate {

    @Inject
    lateinit var tokenManger: TokenManger

    private var _binding:FragmentRawCanCommentBinding? =null
    private val binding:FragmentRawCanCommentBinding
    get()  = _binding!!
    private lateinit var act: RawDataDetActivity

    private lateinit var adapter : rawDataCommentAdapter

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as RawDataDetActivity
        if(act.data != null)
        setView(act.data!!)
    }

    private fun setView(data: RawCandidateData) {
        adapter.submitList(data.commentList)
        binding.rcViewComments.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcViewComments.adapter = adapter


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentRawCanCommentBinding.inflate(layoutInflater)

        adapter = rawDataCommentAdapter(
            tokenManger.getUserId().toString(),this,requireContext())
        return binding.root
    }

    override fun updateComment(comment: RawDataComment) {
        try {

            //Message Box After Click on Comment Button
            val builders = AlertDialog.Builder(requireContext())
            builders.setTitle("Candidate Comment")
            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(20, 20, 20, 20)




            val commentList = arrayOf(
                "Not Responding",
                "Call Busy",
                "Out of Coverage area",
                "Switch off",
                "Invalid Number",
                "Not Interested",
                "Not ready to pay expecting salary",
                "Interested for demo.\nNeed a Reminder call for Demo.",
                "He/She will discuss with family and let you knows.",
                "Interested Ready to visit the office location",
                "Will join from ",
                "Shared location and details",
                "Traveling,Need to call back after some time. ",
                "Interested for only placement not for course"
            )


            //Remark
            val RemarkText = TextView(requireContext())
            RemarkText.text = "Remark"
            RemarkText.textSize = 20F
            RemarkText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            layout.addView(RemarkText)
            val RemarkEditText = AutoCompleteTextView(requireContext())
            RemarkEditText.setHint("Remark")
            RemarkEditText.textSize = 20F
            RemarkEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, commentList)
            RemarkEditText.threshold = 1
            RemarkEditText.setAdapter(adapter)
            RemarkEditText.setText(comment.remark)
            layout.addView(RemarkEditText)









            builders.setPositiveButton("Submit") { dialogInterface, which ->

                try {
                    if (RemarkEditText.text.isNotEmpty()) {


                        candidateViewModel.updateComment(comment.commentId,RemarkEditText.text.toString(),)
                        candidateViewModel.stringResData.observe(viewLifecycleOwner){
                            when(it) {
                                is NetworkResult.Loading -> {}
                                is NetworkResult.Error ->{
                                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                                    requireActivity().finish()
                                }
                                is NetworkResult.Success ->{
                                    Toast.makeText(requireContext(),it.data.toString(),Toast.LENGTH_SHORT).show()
                                    requireActivity().finish()
                                }
                            }

                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG,"Error in RawCommentFragment.kt updateComment() submitAction is "+e.message)
                }
            }
            builders.setNeutralButton("Cancel") { dialogInterface, which ->

            }

            builders.setView(layout)
            builders.setIcon(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.crm
                )
            )
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()





        } catch (e: Exception) {
            Log.d(TAG, "Error in RawCanCommentFragment.kt updateComment() is " + e.message)
        }


    }

    override fun commentList(comment: RawDataComment) {
        try {
            candidateViewModel.commentList(comment.commentId)
            candidateViewModel.commentListResLiveData.observe(viewLifecycleOwner)
            {
                when(it)
                {
                    is NetworkResult.Success -> {
                        adapter.submitList(it.data)
                        binding.rcViewComments.layoutManager =
                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcViewComments.adapter = adapter
                    }
                    is NetworkResult.Error ->{
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading ->{

                    }
                }
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in RawCanCommentFragment.kt commentList() is ${e.message}")
        }
    }


}