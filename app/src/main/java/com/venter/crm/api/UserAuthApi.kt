package com.venter.crm.api

import com.venter.crm.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAuthApi {

    //Candidate Api
    @POST("/candidateApi/createCandidate")
    suspend fun createCandidate(@Body candidate: CandidetDetails): Response<CandidateCrateRes>

    @Multipart
    @POST("/candidateApi/candidateProfileDp")
    suspend fun createCandidateProfile(
        @Part spic: MultipartBody.Part,
        @Query("cId") cId: String,
        @Query("profileType")profileType:String
    ): Response<String>


    @POST("/candidateApi/candidateList")
    suspend fun candidateList(): Response<List<CandidateList>>

    @POST("/candidateApi/removeCandidate")
    suspend fun removeCandidate(@Query("cId") id: Int): Response<String>

    @POST("/candidateApi/candidateCertDet")
    suspend fun candidateCertDet(@Query("cId") cId: String): Response<CanCertDetRes>

    @POST("/candidateApi/printCandidateProfile")
    suspend fun printCandidateProfile(@Query("cId") cId: String): Response<String>

    @POST("/candidateApi/printOfferLetter")
    suspend fun printOfferLetter(
        @Query("cId") cId: String,
        @Query("outWardId") outwardId: String,
        @Query("letterDate") letterDate: String,
        @Query("stamp") stamp: Boolean,
        @Query("varAmt") varAmt: Int,
    ): Response<String>

    @POST("/candidateApi/printHikeLetter")
    suspend fun printHikeLetter(
        @Query("cId") cId: String,
        @Query("letterDate") letterDate: String,
        @Query("effectiveDate") effectiveDate: String,
        @Query("jobPosition") jobPosition: String,
        @Query("newPackage") newPackage: String,
        @Query("stamp") stamp: Boolean,
        @Query("varAmt") varAmt: String
    ): Response<String>

    @POST("/candidateApi/printExperienceLetter")
    suspend fun printExperienceLetter(
        @Query("cId") cId: String,
        @Query("letterDate") letterDate: String,
        @Query("releaseDate") releaseDate: String,
        @Query("stamp") stamp: Boolean,
        @Query("jobActivity") jobActivity:String
    ): Response<String>

    @POST("/candidateApi/printRelievingLetter")
    suspend fun printRelievingLetter(
        @Query("cId") cId: String,
        @Query("letterDate") letterDate: String,
        @Query("resignDate") resignDate: String,
        @Query("releaseDate") closeDate: String,
        @Query("stamp") stamp: Boolean
    ): Response<String>


    @POST("/candidateApi/printSalarySlip")
    suspend fun printSalarySlip(
        @Query("cId") cId: String,
        @Query("month") month: String,
        @Query("year") year: String,
        @Query("jobPos") jobPos: String,
        @Query("package") packages: String,
        @Query("lop") lop: String,
        @Query("byChq") checked: Boolean,
        @Query("salaryDays")salaryDays: String
    ): Response<String>


    @POST("/candidateApi/printInternshipLetter")
    suspend fun printInternshipLetter(
        @Query("cId") cId: String,
        @Query("outWardId") outwardId: String,
        @Query("letterDate") letterDate: String,
        @Query("stamp") stamp: Boolean,
        @Query("stamped") stamped:String
    ): Response<String>


    @POST("/candidateApi/printInternshipCertificate")
    suspend fun printInternshipCertificate(
        @Query("cId") cId: String,
        @Query("letterDate") letterDate: String,
        @Query("releaseDate") releaseDate: String,
        @Query("stamp") stamp: Boolean
    ): Response<String>

    @POST("/candidateApi/printIdCard")
    suspend fun printIdCard(
        @Query("cId") cId: String
    ): Response<String>

    @POST("/candidateApi/candidateDet")
    suspend fun getCandidateDet(@Query("cId") cId: String) : Response<CandidetDetails>

    @POST("/candidateApi/getSalarySlips")
    suspend fun getSalaryList(@Query("cId")cId: Int): Response<List<SalarySlipDet>>

    @POST("/candidateApi/getHikeLetterList")
    suspend fun getHikeLetterList(@Query("cId")cId: Int):Response<List<HikeLetterDet>>
    //Candidate Fee Api

    @POST("/candidateFeeApi/feeList")
    suspend fun getCandidateFeeDet() : Response<List<CandidateFeeList>>

    @POST("/candidateFeeApi/feeLedger")
    suspend fun getCandidateFeeLedger(@Query("cId") cId: String) : Response<FeeLedgerDet>

    @POST("/candidateFeeApi/candidateFeeReceipt")
    suspend fun candidateFeeReceipt(@Query("cId") cId: String,@Query("rcptDate") rcptDate: String,@Query("rcptAmt") rcptAmt: String,@Query("remark") remark: String,@Query("nextPayDate") nextPayDate: String,@Query("candidateName") candidateName: String,@Query("rcptId")rcptId:Int?,@Query("transType")transType:String) : Response<String>


    //Expense Mangament Api
    @POST("/expenseManagement/expenseReceipt")
    suspend fun expenseReceipt(@Query("rcptDate") rcptDate: String,@Query("transCat") transCat: String,@Query("tranType") tranType: String,@Query("traDesc") traDesc: String,@Query("rcptAmt") rcptAmt: String) : Response<String>

    @POST("/expenseManagement/expenseReport")
    suspend fun expenseReport() : Response<ExpenseReportRes>
    @POST("/expenseManagement/expenseReportPrint")
    suspend fun expenseReportPrint(@Query("toDate") toDate: String,@Query("fromDate") fromDate: String) : Response<List<expensesDet>>


    //User Mangment API
    @POST("/userManagement/createUser")
    suspend fun createUser(
        @Query("userName") userName: String,
        @Query("mobileNo") mobileNo: String,
        @Query("emailId") emailId: String,
        @Query("jobDesignation") jobDesignation: String,
        @Query("userType") userType: String,
        @Query("user_id") userId: Int
    ) : Response<String>


    @POST("/userManagement/userList")
    suspend fun userList() : Response<List<UserListRes>>

    @POST("/userManagement/userStatus")
    suspend fun userStatus(@Query("user_id") userId: Int,@Query("status") status: Int) : Response<String>


    @POST("/userManagement/getEmpHierarchyData")
    suspend fun getEmpHierarchyData(@Query("emp_id")id: Int):Response<UserHierarchyData>


    @POST("/userManagement/getSubOrdinateList")
    suspend fun getSubOrdinateList(@Query("emp_id")selectedId: Int, @Query("userType")userType: String): Response<SubOrdinateData>

    @POST("/userManagement/updateUserHierarchy")
    suspend fun updateUserHierarchy(@Query("id")id: Int, @Query("slId")slId: Int,@Query("flId") flId: Int,
                                    @Query("tlId") tlId: Int, @Query("sbaId")sbaId: Int): Response<String>

    //CandidateRawData Api
    @POST("/candidateRawData/addMultipleRawData")
    suspend fun addMultipleRawData(): Response<String>
    @POST("/candidateRawData/deleteMultipleRawData")
    suspend fun deleteMultipleRawData(@Body rawList: ArrayList<RawDataList> ): Response<String>

    @POST("/candidateRawData/getAllRawData")
    suspend fun getAllRawData(@Query("offset")offset: Int): Response<List<RawDataList>>

    @POST("/candidateRawData/getEmpRawData")
    suspend fun getEmpRawData(): Response<List<RawDataList>>

    @POST("/candidateRawData/getEmpProsData")
    suspend fun getEmpProsData(): Response<List<RawDataList>>

    @POST("/candidateRawData/getEmpSearchData")
    suspend fun getEmpSearchData(@Query("search")search: String): Response<List<RawDataList>>

    @POST("/candidateRawData/getOtherProsData")
    suspend fun getOtherProsData(@Query("otherUserId") userId: Int): Response<List<RawDataList>>

    @POST("/candidateRawData/getEmpListRawData")
    suspend fun getEmpListRawData(): Response<List<UserList>>

    @POST("/candidateRawData/getIncomingData")
    suspend fun getIncomingData(@Query("uId") uId: Int): Response<List<RawDataList>>
    @POST("/candidateRawData/getStealData")
    suspend fun getStealData(@Query("uId") uId: Int): Response<List<RawDataList>>
    @POST("/candidateRawData/getEmpNotResData")
    suspend fun getEmpNotResData(@Query("uId") uId: Int): Response<List<RawDataList>>
    @POST("/candidateRawData/getRawCandidateDet")
    suspend fun getRawCandidateDet(@Query("candidateId") candidateId: Int): Response<RawCandidateData>
    @POST("/candidateRawData/updateRawCandidateDet")
    suspend fun updateRawCandidateDet(@Query("candidateId") candidateId: Int,@Query("mobNo") mobNo: String,@Query("altermobNo") altermobNo: String): Response<String>
    @POST("/candidateRawData/updateCustData")
    suspend fun updateCustumerData(@Body custData:CustUpdateDet): Response<String>


    /*@POST("/candidateRawData/setEmpRawDataComment")
    suspend fun setEmpRawDataComment(
        @Query("callTime") callTime: String,
        @Query("prosType") prosType: String,
        @Query("remark") remark: String,
        @Query("folloupDate") folloupDate: String,
        @Query("selectedItem") selectedItem: String,
        @Query("candidateId") candidateId: String,
        @Query("prosUpdate") update: Int,
        @Query("mobNo") mobNo: String,
        @Query("alterMobNo") alternateMob: String,
        @Query("prosLevel") prosLevel: String
    ): Response<String> */

    @POST("/candidateRawData/setEmpRawDataComment")
    suspend fun setEmpRawDataComment(@Body commentData: RawCommentData): Response<String>


    @POST("/candidateRawData/sendWhatsMsg")
    suspend fun sendWhatsMsg(
        @Query("selectedItem") selectedItem: Int,
        @Query("mobNo") mobNo: String)

    //Get Candidate Id list for today's Follow up
    @POST("/candidateRawData/getFollowUpList")
    suspend fun getFollowUpList(@Query("userId") userId: Int, @Query("folloupDate")folloupDate: String): Response<List<RawDataList>>


    //Get the Employee report for the employee dashboard from today's of manual date
    @POST("/candidateRawData/getEmpReport")
    suspend fun getEmpReport(@Query("fromDate")fromDate: String,@Query("toDate")toDate: String,@Query("Empid")empId: String): Response<EmpReport>

    @POST("/candidateRawData/getTeleReport")
    suspend fun getTeleReport(@Query("fromDate")fromDate: String,@Query("toDate")toDate: String): Response<EmpReport>

    //Get Salary Slip List For the use for The CandidateDocList


    @POST("/candidateRawData/setIncomingLead")
    suspend fun setIncomingLead(@Query("canName")canName: String,@Query("mobNo")mobNo: String): Response<String>

    @POST("/candidateRawData/getAdmissionData")
    suspend fun getAdmissionData(): Response<List<RawDataList>>

    @POST("/candidateRawData/getTimeTable")
    suspend fun getWorkingHrs(): Response<List<WorkingHrs>>

    @POST("/candidateRawData/setTimeTable")
    suspend fun setWorkingHrs(@Body shedule:WorkingHrs): Response<String>

    @POST("/candidateRawData/getTodayShedule")
    suspend fun getTodayShedule(): Response<userStatus>




    //WhatsApp Temp
    @POST("/whatsAppTemp/getMsgList")
    suspend fun getWhatsMsgList():Response<List<WhatsappTemplateMsg>>
    @POST("/whatsAppTemp/getMsgNameList")
    suspend fun getMsgNameList():Response<List<WhatsTempNameList>>


    @POST("/whatsAppTemp/UpdateTempText")
    suspend fun updateWhatsApiTempText(@Body temp:WhatsappTemplateMsg)

    @Multipart
    @POST("/whatsAppTemp/UpdateWhatsApiAttachments")
    suspend fun UpdateWhatsApiAttachments(
        @Part spic: MultipartBody.Part,
        @Query("tempId") tempId: String,
        @Query("template") template: String,
        @Query("tempName")tempName: String
    ) :Response<String>

    @POST("/whatsAppTemp/getWhatsAppList")
    suspend fun getWhatsAppList():Response<List<WhatsAppAccList>>

    @POST("/whatsAppTemp/logOutWhatsapp")
    suspend fun logOutWhatsapp(@Query("accId")id: Int): Response<String>
    @POST("/whatsAppTemp/updateWhatsAppAcc")
    suspend fun updateWhatsAppAcc(@Query("accId")id: Int,@Query("userId")userId: Int): Response<String>
    @POST("/whatsAppTemp/createWhatsAppAcc")
    suspend fun createWhatsAppAcc():Response<String>

    @POST("/whatsAppTemp/updateTemp")
    suspend fun updateTemp(
        @Query("tempId") tempId: String,
        @Query("template") template: String,
        @Query("header") header: String,
        @Query("tempName")tempName: String
    ):Response<String>

     @POST("/candidateRawData/getEmpColdRawData")
    suspend fun getEmpColdRawData(@Query("offset")offset:Int): Response<List<RawDataList>>
    @POST("/candidateRawData/updateComment")
    suspend fun updateComment(@Query("commentId")commentId: Int,@Query("comment") comment: String): Response<String>

    @POST("/candidateRawData/getCommentList")
    suspend fun getCommentList(@Query("commentId")commentId: Int): Response<List<RawDataComment>>

    @POST("/whatsAppTemp/initializeWhats")
    suspend fun intilWhats(@Query("userId")id: Int): Response<String>


    @POST("/userManagement/dataTransfer")
    suspend fun dataTransfer(
        @Query("dataFrom")dataFrom: Int,
        @Query("dataTo")dataTo: Int,
        @Query("cold")cold: Int,
        @Query("hot")hot: Int,
        @Query("warm")warm: Int,
        @Query("notRes")notRes: Int,
        @Query("admission")admission: Int,
        @Query("raw")raw: Int
    ): Response<String>

    @POST("/userManagement/dataSwipe")
    suspend fun swipeData(@Query("dataId")dataId: Int, @Query("userId")userId: Int): Response<String>
    @POST("/userManagement/getUserReport")
    suspend fun getUserReport(@Query("userID")userId: Int, @Query("toDate")toDate: String, @Query("fromDate")fromDate: String):Response<List<UserReportData>>
    @POST("/userManagement/delAccount")
    suspend fun delAcc(@Query("id")id: Int): Response<String>

    @POST("/userManagement/resetDevice")
    suspend fun resetDevice(@Query("id")id: Int): Response<String>


    @Multipart
    @POST("/candidateRawData/addMultipleRawData")
    suspend fun updateDatabase(@Part spic: MultipartBody.Part): Response<String>
    @POST("/hrManagement/createEmployee")
    suspend fun createEmployee(@Body empDet: EmpDet): Response<String>
    @POST("/hrManagement/getEmpList")
    suspend fun getEmpList():Response<List<EmployeeList>>
    @POST("/hrManagement/getEmpInfo")
    suspend fun getEmpInfo(@Query("empId")empId: Int): Response<EmpInfoData>
    @POST("/hrManagement/getEmpDet")
    suspend fun getEmpDet(@Query("empId")empId: Int): Response<EmpDet>

    @Multipart
    @POST("/hrManagement/employeeProfileDp")
    suspend fun employeeProfileDp(
        @Part spic: MultipartBody.Part,
        @Query("empId") empId: String
    ): Response<String>

    @Multipart
    @POST("/hrManagement/employeeDocuments")
    suspend fun employeeDocuments(
        @Part spic:List<MultipartBody.Part>,
        @Query("empId") empId: String
    ): Response<String>

}