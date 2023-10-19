package com.venter.crm.Candidate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.FragmentCandidateCertDocBinding
import com.venter.crm.models.CanCertDetRes
import com.venter.crm.models.HikeLetterDet
import com.venter.crm.models.SalarySlipDet
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CandidateCertDoc : Fragment(), onClick {

    private var _binding: FragmentCandidateCertDocBinding? = null
    private val binding: FragmentCandidateCertDocBinding
        get() = _binding!!

    private lateinit var adapter: CandidateDocAdapter

    lateinit var data: CanCertDetRes

    private lateinit var act: CandidateDoc

    private lateinit var salaryList: ArrayList<SalarySlipDet>
    private lateinit var hikeLetterList: ArrayList<HikeLetterDet>
    private var candidateId: String? = null

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as CandidateDoc
        data = act.data
        candidateId = act.candidateId

        setView()
    }

    private fun setView() {
        try {

            var docArray: ArrayList<String> = ArrayList()
            docArray.add("${data.first_name} ${data.last_name} Profile")
            docArray.add("${data.first_name} ${data.last_name} Id Card")

            if (data.releivingleeter_date != null)
                docArray.add("${data.first_name} ${data.last_name} Reliving Letter")
            if (data.offerletter_date != null)
                docArray.add("${data.first_name} ${data.last_name} Offer Letter")
            if (data.expletter_date != null)
                docArray.add("${data.first_name} ${data.last_name} Experience Letter")
           // if (data.hikeletter_date != null)
             //   docArray.add("${data.first_name} ${data.last_name} Hike Letter")
            if (data.internletterDate != null)
                docArray.add("${data.first_name} ${data.last_name} Internship Letter")
            if (data.icertDate != null)
                docArray.add("${data.first_name} ${data.last_name} Internship Cert")


            candidateViewModel.getSalaryList(candidateId!!.toInt())
            candidateViewModel.salarySlipResLiveData.observe(viewLifecycleOwner)
            {
                when (it) {
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        salaryList = ArrayList()
                        salaryList = it.data as ArrayList<SalarySlipDet>
                        salaryList.forEach {
                            docArray.add("${data.first_name} ${data.last_name} ${it.salaryMonth.toString()} ${it.salaryYear}")
                        }
                        adapter.submitList(docArray)
                        binding.rcView.layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcView.adapter = adapter
                    }
                }
            }

            candidateViewModel.getHikeLetterList(candidateId!!.toInt())
            candidateViewModel.hikeLetterListResLiveData.observe(viewLifecycleOwner)
            { it ->
                when(it)
                {
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()}
                    is NetworkResult.Success -> {
                        hikeLetterList = ArrayList()
                        hikeLetterList = (it.data as ArrayList<HikeLetterDet>?)!!
                        val hikeList = hikeLetterList.map {"${data.first_name} ${data.last_name}  ${it.newPosition} Hike Letter" }
                        hikeList.forEach{list->
                            docArray.add(list)
                        }
                        adapter.submitList(docArray)
                        binding.rcView.layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcView.adapter = adapter
                    }
                }
            }


            adapter.submitList(docArray)
            binding.rcView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding.rcView.adapter = adapter


        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertDoc.kt setView() is " + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCandidateCertDocBinding.inflate(layoutInflater)
        adapter = CandidateDocAdapter(this)
        salaryList = ArrayList()
        return binding.root
    }

    override fun docClick(docName: String) {
        //Here we got the doc Name We need to Split Name and Surname of Candidate From It
        val canName = "${data.first_name} ${data.last_name} "
        var doc = docName.replace(canName, "")

        when (doc) {
            "Profile" -> {
                serverRes("Profile")
            }
            "Id Card" -> {
                candidateViewModel.printIdCard(candidateId.toString())

                serverRes("IdCard")
            }
            "Reliving Letter" -> {
                val stamp = data.relativeStamp == 1
                candidateViewModel.printRelievingLetter(
                    candidateId.toString(),
                    data.releivingleeter_date.toString(),
                    data.resign_date.toString(),
                    data.releiving_date.toString(),
                    stamp
                )
                serverRes("Reliving")
            }
            "Offer Letter" -> {
                val stamp = data.offerStamp == 1
                candidateViewModel.printOfferLetter(
                    candidateId!!.toInt(), data.offerletter_outward.toString(),
                    data.offerletter_date.toString(), stamp, data.offerVari
                )
                serverRes("Offer")
            }
            "Experience Letter" -> {
                val stamp = data.expStamp == 1
                candidateViewModel.printExperienceLetter(
                    candidateId.toString(),
                    data.expletter_date.toString(),
                    data.last_date.toString(),
                    data.jobActivity.toString(),
                    stamp
                )

                serverRes("Experience")
            }
            "Hike Letter" -> {
                val stamp = data.hikeStamp == 1
                candidateViewModel.printHikeLetter(
                    candidateId.toString(),
                    data.hikeletter_date.toString(),
                    data.effective_date.toString(),
                    data.newPosition.toString(),
                    data.newPackage.toString(),
                    stamp,
                    "0"
                )
                serverRes("Hike")
            }
            "Internship Letter" -> {
                val stamp = data.interofferStamp == 1
                candidateViewModel.printInternshipLetter(candidateId!!.toInt(),data.interOutwardIndex.toString(),data.internletterDate.toString(),stamp,data.internStipend.toString())
                serverRes("Internship")
            }
            "Internship Cert" -> {
                val stamp = data.intercertStamp == 1
                candidateViewModel.printInternshipCertificate(
                    candidateId.toString(),data.icertDate.toString(),data.icrelaseDate.toString(),stamp)

                serverRes("InternshipCert")
            }
            else -> {
                //Here we got the Salary Slip with Month and Year
                val salary = doc.split("\\s".toRegex())
                salaryList.forEach {
                    if (it.salaryYear == salary[1] && it.salaryMonth == salary[0]) {
                        candidateViewModel.printSalarySlip(
                            candidateId.toString(),
                            it.salaryMonth,
                            it.salaryYear,
                            it.jobPosition,
                            it.packages,
                            checked = false,
                            //salaryDays = salaryDays.text.toString()
                        )
                        serverRes("SalarySlip", it.salaryMonth, it.salaryYear)
                    }
                }
            }


        }
    }

    private fun serverRes(action: String, month: String = "", year: String = "") {
        candidateViewModel.stringResData.observe(this) {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                    .show()
                is NetworkResult.Success -> {
                    candidateViewModel.stringResData.removeObservers(this)
                    binding.progressbar.visibility = View.VISIBLE
                    Toast.makeText(
                        context,
                        "File is preparing for Preview Please wait.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler().postDelayed({
                        binding.progressbar.visibility = View.GONE
                        download_share(action, month, year)
                    }, 4000)

                }
            }
        }
    }


    private fun download_share(action: String, month: String, year: String) {
        try {

            /* val urlString = when (action) {
                 "Profile" -> BASE_URL + "assets/documents/candidateProfile.pdf"
                 "Offer" -> BASE_URL + "assets/documents/offerletter.pdf"
                 "Hike" -> BASE_URL + "assets/documents/candidateHikeLetter.pdf"
                 //"Experience" -> BASE_URL + "assets/documents/exprianceLetter.pdf"
                 "Experience" -> BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Experience Letter.pdf"
                 "Reliving" -> BASE_URL + "assets/documents/relievingLetter.pdf"
                 "SalarySlip" ->BASE_URL + "assets/documents/candidateSalarySlip.pdf"
                 "Internship" -> BASE_URL + "assets/documents/candidateIntershipLetter.pdf"
                 "InternshipCert" -> BASE_URL + "assets/documents/candidateInternshipLetter.pdf"
                 "IdCard" -> BASE_URL + "assets/documents/IdCard.pdf"

                 else -> ""
             }*/
            val urlString = when (action) {
                "Profile" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Profile.pdf"
                "Offer" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Offer Letter.pdf"
                "Hike" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Hike Letter.pdf"
                //"Experience" -> BASE_URL + "assets/documents/exprianceLetter.pdf"
                "Experience" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Experience Letter.pdf"
                "Reliving" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Reliving Letter.pdf"
                "SalarySlip" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} ${month.toString()} $year.pdf"
                "Internship" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Internship Letter.pdf"
                "InternshipCert" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Internship Cert.pdf"
                "IdCard" -> Constans.BASE_URL + "assets/documents/${data.first_name} ${data.last_name} Id Card.pdf"

                else -> ""
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.apps.docs")

            startActivity(intent)

        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateCertificate.kt download&shar() is " + e.message)
        }
    }


}