package com.venter.crm.viewModelClass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venter.crm.models.*
import com.venter.crm.repository.UserAuthRepository
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun removeCandidate(id: Int) {
        try {
            viewModelScope.launch {
                userRepository.removeCandidate(id)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt removeCandidate() is " + e.message)
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

    fun printOfferLetter(
        cId: Int,
        outWard: String,
        letterDate: String,
        stamp: Boolean,
        varAmt: Int
    ) {
        try {
            viewModelScope.launch {
                userRepository.printOfferLetter(cId, outWard, letterDate, stamp,varAmt)
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
        stamp: Boolean,
        varAmt: String
    ) {
        try {
            viewModelScope.launch {
                userRepository.printHikeLetter(
                    cId,
                    letterDate,
                    effectiveDate,
                    jobPosition,
                    newPackage,
                    stamp,varAmt
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
    fun printSalarySlip(
        cId: String,
        month: String,
        year: String,
        jobPos: String,
        packages: String,
        lop: String = "0",
        checked: Boolean,
        salaryDays: String = "0"
    )
    {
        try {
            viewModelScope.launch {
                userRepository.printSalarySlip(cId, month,year,jobPos,packages,lop,checked,salaryDays)
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

    fun candidateFeeReceipt( cId: String,rcptDate: String,rcptAmt: String, remark: String, nextPayDate: String,candidateName:String,rcptId:Int?,transType:String)
    {
        try {
            viewModelScope.launch {
                userRepository.submitFeeReceipt(cId,rcptDate,rcptAmt, remark, nextPayDate,candidateName,rcptId,transType)
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
    fun createUser(
        userName: String,
        mobNo: String,
        emailId: String,
        designation: String,
        userType: String,
        userId: Int
    )
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

    fun userStatus(userId: Int,status:Int)
    {
        try {
            viewModelScope.launch {
                userRepository.userStatus(userId,status)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt userList() is " + e.message)
        }

    }
    fun addMultipleRawData()
    {
        try {
            viewModelScope.launch {
                userRepository.addMultipleRawData()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt userList() is " + e.message)
        }

    }

    fun deleteMultipleRawData(rawList: ArrayList<RawDataList>)
    {
        try {
            viewModelScope.launch {
                userRepository.deleteMultipleRawData(rawList)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt deleteMultipleRawData() is " + e.message)
        }

    }

    val allrawDataListResLiveData: LiveData<NetworkResult<List<RawDataList>>>
        get() = userRepository.allrawDataListResLiveData

    fun getAllRawData(offset: Int)
    {
        try {
            viewModelScope.launch {
                userRepository.getAllRawData(offset)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getAllRawData() is " + e.message)
        }

    }



    fun getEmpRawData()
    {
        try {
            viewModelScope.launch {
                userRepository.getEmpRawData()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getEmpRawData() is " + e.message)
        }

    }

    fun getEmpProsData()
    {
        try {
            viewModelScope.launch {
                userRepository.getEmpProsData()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getEmpProsData() is " + e.message)
        }

    }

    fun getEmpSearchData(serach:String) {
        try {
            viewModelScope.launch {
                userRepository.getEmpSearchData(serach)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getEmpSearchData() is " + e.message)
        }

    }

    val othersrawDataListResLiveData: LiveData<NetworkResult<List<RawDataList>>>
        get() = userRepository.othersrawDataListResLiveData
    fun getOthersProsData(userId: Int)
    {
        try {
            viewModelScope.launch {
                userRepository.getOthersProsData(userId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getEmpRawData() is " + e.message)
        }

    }

    val userListLiveData: LiveData<NetworkResult<List<UserList>>>
        get() = userRepository.userListLiveData
    fun getEmpListRawData()
    {
        try {
            viewModelScope.launch {
                userRepository.getEmpListRawData()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getEmpListRawData() is " + e.message)
        }

    }


    val rawCandidateDataLiveData: LiveData<NetworkResult<RawCandidateData>>
        get() = userRepository.rawCandidateDataLiveData

    fun getRawCandidateData(Id: Int)
    {
        try {
            viewModelScope.launch {
                userRepository.getRawCandidateData(Id)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getRawCandidateData() is " + e.message)
        }

    }

    fun getIncomingLeads(userId: Int) {
        try {
            viewModelScope.launch {
                userRepository.getIncomingData(userId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getRawCandidateData() is " + e.message)
        }
    }
    fun getStealData(userId: Int) {
        try {
            viewModelScope.launch {

                userRepository.getStealData(userId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getRawCandidateData() is " + e.message)
        }
    }
    fun getEmpNotResData(userId: Int) {
        try {
            viewModelScope.launch {
                userRepository.getEmpNotResData(userId)

            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getEmpNotResData() is " + e.message)
        }
    }

    fun updateRawCandidateData(Id: Int,mobNo: String,alterMobNo:String)
    {
        try {
            viewModelScope.launch {
                userRepository.updateCandidateRawData(Id,mobNo,alterMobNo)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt updateRawCandidateData() is " + e.message)
        }

    }

    fun setEmpRawDataComment(
        callTime: String,
        prosType: String,
        remark: String,
        folloupDate: String,
        selectedItem: String,
        candidateId: String,
        update: Int, mobNo: String, alternateMob: String, prosLevel: String
    ) {
        try {
            viewModelScope.launch {
                userRepository.setEmpRawDataComment(callTime,prosType, remark, folloupDate, selectedItem,
                    candidateId,update,mobNo,alternateMob,prosLevel)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getRawCandidateData() is " + e.message)
        }
    }

    fun sendWhatsMsg(temNo: Int, mobNo: String) {
        try {
            viewModelScope.launch {
                userRepository.sendWhatsMsg(temNo, mobNo)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt sendWhatsMsg() is " + e.message)
        }
    }


//    val intListResData: LiveData<NetworkResult<List<RawDataList>>>
//        get() = userRepository.allrawDataListResLiveData
    fun getFollowUpList(userId: Int, folloupDate: String){
        try {
            viewModelScope.launch {
                userRepository.getFollowUpList(userId,folloupDate)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getFollowUpList() is " + e.message)
        }
    }

    val empReptResLiveData: LiveData<NetworkResult<EmpReport>>
        get() = userRepository.empReptResLiveData

    fun getEmpReport(fromDate:String,toDate: String,empId: String){
        try {
            viewModelScope.launch {
                userRepository.getEmpReport(fromDate,toDate,empId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getFollowUpList()() is " + e.message)
        }
    }

    fun getTelReport(fromDate:String,toDate: String){
        try {
            viewModelScope.launch {
                userRepository.getTeleReport(fromDate,toDate)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getFollowUpList()() is " + e.message)
        }
    }


    val salarySlipResLiveData: LiveData<NetworkResult<List<SalarySlipDet>>>
        get() = userRepository.salarySlipResLiveData
    fun getSalaryList(cId:Int){
        try {
            viewModelScope.launch {
                userRepository.getSalaryList(cId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getSalaryList() is " + e.message)
        }
    }

    fun setIncomingLead(canName:String,canMob:String){
        try {
            viewModelScope.launch {
                userRepository.setIncomingLead(canName,canMob)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt setIncomingLead() is " + e.message)
        }
    }

    fun getAddmissionData(){
        try {
            viewModelScope.launch {
                userRepository.getAdmissionData()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getAddmissionData() is " + e.message)
        }
    }

    val workHrsResLiveData : LiveData<NetworkResult<List<WorkingHrs>>>
        get() = userRepository.workHrsResLiveData
    fun getWorkingHrsData(){
        try {
            viewModelScope.launch {
                userRepository.getWorkingHrsData()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getWorkingHrsData() is " + e.message)
        }
    }

    fun setWorkingHrsData(shedule:WorkingHrs){
        try {
            viewModelScope.launch {
                userRepository.setWorkingHrsData(shedule)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt setWorkingHrsData() is " + e.message)
        }
    }


    val todaySheduleResLiveData : LiveData<NetworkResult<userStatus>>
        get() = userRepository.todaySheduleResLiveData
    fun getTodaysShedule(){
        try {
            viewModelScope.launch {
                userRepository.getTodaysShedule()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getTodaysShedule() is " + e.message)
        }
    }

    val msgListResLiveData : LiveData<NetworkResult<List<WhatsappTemplateMsg>>>
        get() = userRepository.msgListResLiveData

    fun getWhatsMsgList(){
        try {
            viewModelScope.launch {
                userRepository.getMsgList()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getWhatsMsgList() is " + e.message)
        }
    }

    fun whatApiTemplateTextUpdate(temp:WhatsappTemplateMsg)
    {
        try {
            viewModelScope.launch {
                userRepository.whatsApiTextUpdate(temp)
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in MsgViewModel.kt whatApiTemplateStatusUpdate() is "+e.message)
        }
    }

    fun updateTemp(tempId:String, template:String, header:String)
    {
        try {
            viewModelScope.launch {
                userRepository.updateTemp(tempId, template, header)
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in MsgViewModel.kt whatApiTemplateUpdate() is "+e.message)
        }
    }

    fun getColdData(offset:Int) {
        try {
            viewModelScope.launch {
                userRepository.getColdRawData(offset)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getColdData() is " + e.message)
        }
    }

    fun updateComment(commentId: Int, comment: String) {
        try {
            viewModelScope.launch {
                userRepository.updateComment(commentId, comment)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt updateComment() is " + e.message)
        }
    }

    fun intilWhats(id: Int) {
        try {
            viewModelScope.launch {
                userRepository.intilWhats(id)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt intilWhats() is " + e.message)
        }

    }

    fun dataTransfer(userId: Int, dataTo: Int, cold: Int, hot: Int, warm: Int, notRes: Int, admission: Int, raw: Int) {
        try {
            viewModelScope.launch {
                userRepository.dataTransfer(userId, dataTo, cold, hot, warm, notRes, admission, raw)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt dataTransfer() is " + e.message)
        }

    }

    fun swipeData(dataId: Int, userId: Int) {
        try {
            viewModelScope.launch {
                userRepository.swipeData(dataId,userId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt dataTransfer() is " + e.message)
        }
    }

    val userReportListResLiveData : LiveData<NetworkResult<List<UserReportData>>>
        get() = userRepository.userReportListResLiveData
    fun getUserReport(userId: Int, toDate: String, fromDate: String) {
        try {
            viewModelScope.launch {
                userRepository.getUserReport(userId, toDate, fromDate)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getUserReport() is " + e.message)
        }

    }

    fun delAcc(id: Int) {
        try {
            viewModelScope.launch {
                userRepository.delAcc(id)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt delAcc() is " + e.message)
        }
    }


    val hikeLetterListResLiveData : LiveData<NetworkResult<List<HikeLetterDet>>>
        get() = userRepository.hikeLettersLiveData
    fun getHikeLetterList(cId: Int) {
        try {
            viewModelScope.launch {
                userRepository.getHikeLetterList(cId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt getHikeLetterList() is " + e.message)
        }
    }

    val commentListResLiveData : LiveData<NetworkResult<List<RawDataComment>>>
        get() = userRepository.commentListLiveData
    fun commentList(commentId: Int) {
        try {
            viewModelScope.launch {
                userRepository.commentList(commentId)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateViewModel.kt commentList() is " + e.message)
        }

    }




}