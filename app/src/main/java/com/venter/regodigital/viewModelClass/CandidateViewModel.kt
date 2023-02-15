package com.venter.regodigital.viewModelClass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venter.regodigital.models.*
import com.venter.regodigital.repository.UserAuthRepository
import com.venter.regodigital.repository.UserRepository
import com.venter.regodigital.userledger.UserAddActivity
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor(private val userRepository: UserAuthRepository) :
    ViewModel() {

    val createCandidateResData: LiveData<NetworkResult<CandidateCrateRes>>
        get() = userRepository.candidateCreateReqResLiveData

    fun createCandidate(candidate: CandidetDetails) {
        try {
            viewModelScope.launch {
                userRepository.createCandidateRepo(candidate)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt createCandidate() is " + e.message)
        }


    }

    val candidateListResData: LiveData<NetworkResult<List<CandidateList>>>
        get() = userRepository.candidateListLiveData

    fun candidateList() {
        try {
            viewModelScope.launch {
                userRepository.candidateList()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt candidateList() is " + e.message)
        }

    }

    val candidateCertDetResData: LiveData<NetworkResult<CanCertDetRes>>
        get() = userRepository.candidateCertLiveData

    fun candidateCertDet(cId: Int) {
        try {
            viewModelScope.launch {
                userRepository.candidateCerDet(cId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt candidateList() is " + e.message)
        }

    }

    val stringResData: LiveData<NetworkResult<String>>
        get() = userRepository.stringResLiveData

    fun printCandidateProfile(cId: Int) {
        try {
            viewModelScope.launch {
                userRepository.printCandidateProfile(cId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt candidateList() is " + e.message)
        }

    }

    fun printOfferLetter(cId: Int, outWard: String, letterDate: String, stamp: Boolean) {
        try {
            viewModelScope.launch {
                userRepository.printOfferLetter(cId, outWard, letterDate, stamp)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt candidateList() is " + e.message)
        }

    }

    fun printHikeLetter(
        cId: String,
        letterDate: String,
        effectiveDate: String,
        jobPosition: String,
        newPackage: String,
        stamp: Boolean
    ) {
        try {
            viewModelScope.launch {
                userRepository.printHikeLetter(
                    cId,
                    letterDate,
                    effectiveDate,
                    jobPosition,
                    newPackage,
                    stamp
                )
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt candidateList() is " + e.message)
        }

    }

    fun printExperienceLetter(
        cId: String,
        letterDate: String,
        releaseDate: String,
        jobActivity: String,
        stamp: Boolean
    ) {
        try {
            viewModelScope.launch {
                userRepository.printExperienceLetter(cId, letterDate, releaseDate, stamp,jobActivity)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt printExperienceLetter() is " + e.message)
        }

    }

    fun printRelievingLetter(cId: String, letterDate: String, resignDate: String,relaseDate: String, stamp:Boolean)
    {
        try {
            viewModelScope.launch {
                userRepository.printRelievingLetter(cId, letterDate, resignDate,relaseDate, stamp)
            }
        }
        catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt printExperienceLetter() is " + e.message)
        }
    }
    fun printSalarySlip( cId: String, month: String,year: String,jobPos: String,packages: String)
    {
        try {
            viewModelScope.launch {
                userRepository.printSalarySlip(cId, month,year,jobPos,packages)
            }
        }
        catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt printSalarySlip() is " + e.message)
        }
    }

    fun printInternshipLetter(cId: Int, outWard: String, letterDate: String, stamp: Boolean,stamped:String) {
        try {
            viewModelScope.launch {
                userRepository.printInternshipLetter(cId, outWard, letterDate, stamp,stamped)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt candidateList() is " + e.message)
        }

    }

    fun printInternshipCertificate(
        cId: String,
        letterDate: String,
        releaseDate: String,
        stamp: Boolean
    ) {
        try {
            viewModelScope.launch {
                userRepository.printInternshipCertificate(cId, letterDate, releaseDate, stamp)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt printExperienceLetter() is " + e.message)
        }

    }
    fun printIdCard(cId: String)
    {
        try {
            viewModelScope.launch {
                userRepository.printIdCard(cId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt printIdCard() is " + e.message)
        }

    }


    val CandidateDetResData: LiveData<NetworkResult<CandidetDetails>>
        get() = userRepository.candidateDetResLiveData

    fun getCandidateDet(cId: String)
    {
        try {
            viewModelScope.launch {
                userRepository.getCandidateDet(cId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getCandidateDet() is " + e.message)
        }

    }


    val candidateFeeListResLiveData: LiveData<NetworkResult<List<CandidateFeeList>>>
        get() = userRepository.candidateFeeListResLiveData

    fun getCandidateFeeList()
    {
        try {
            viewModelScope.launch {
                userRepository.getCandidateFeeList()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getCandidateFeeList() is " + e.message)
        }

    }

    val feeLedgerResLiveData: LiveData<NetworkResult<FeeLedgerDet>>
        get() = userRepository.feeLedgerResLiveData

    fun getCandidateFeeLedger(cId:String)
    {
        try {
            viewModelScope.launch {
                userRepository.getCandidateFeeLedger(cId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getCandidateFeeLedger() is " + e.message)
        }

    }

    fun candidateFeeReceipt( cId: String,rcptDate: String,rcptAmt: String, remark: String, nextPayDate: String,candidateName:String,rcptId:Int?)
    {
        try {
            viewModelScope.launch {
                userRepository.submitFeeReceipt(cId,rcptDate,rcptAmt, remark, nextPayDate,candidateName,rcptId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getCandidateFeeLedger() is " + e.message)
        }

    }
    fun expensesReceipt( rcptDate: String,transCat: String, tranType: String,traDesc: String,rcptAmt: String)
    {
        try {
            viewModelScope.launch {
                userRepository.expenseReceipt(rcptDate,transCat,tranType,traDesc,rcptAmt)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt expensesReceipt() is " + e.message)
        }

    }
    val expenseReportResLiveData: LiveData<NetworkResult<ExpenseReportRes>>
        get() = userRepository.expenseReportResLiveData

    fun expensesReport()
    {
        try {
            viewModelScope.launch {
                userRepository.expenseReport()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt expensesReport() is " + e.message)
        }

    }

    val expenseReportPrintResLiveData: LiveData<NetworkResult<List<expensesDet>>>
        get() = userRepository.expenseReportPrintResLiveData
    fun expensesReportPrint(toDate:String,fromDate:String)
    {
        try {
            viewModelScope.launch {
                userRepository.expenseReportPrint(toDate,fromDate)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt expensesReportPrint() is " + e.message)
        }

    }
    fun createUser(userName:String,mobNo:String,emailId:String,designation:String,userType:String,userId:Int)
    {
        try {
            viewModelScope.launch {
                userRepository.createUser(userName,mobNo,emailId,designation,userType,userId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt createUser() is " + e.message)
        }

    }

    val userListResLiveData: LiveData<NetworkResult<List<UserListRes>>>
        get() = userRepository.userListResLiveData
    fun userList()
    {
        try {
            viewModelScope.launch {
                userRepository.userList()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt userList() is " + e.message)
        }

    }
    fun addRawData(spic:MultipartBody.Part)
    {
        try {
            viewModelScope.launch {
                userRepository.addRawData(spic)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt userList() is " + e.message)
        }

    }
}