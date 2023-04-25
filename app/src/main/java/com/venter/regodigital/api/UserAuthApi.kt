package com.venter.regodigital.api

import com.venter.regodigital.models.*
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

    @POST("/candidateApi/candidateCertDet")
    suspend fun candidateCertDet(@Query("cId") cId: String): Response<CanCertDetRes>

    @POST("/candidateApi/printCandidateProfile")
    suspend fun printCandidateProfile(@Query("cId") cId: String): Response<String>

    @POST("/candidateApi/printOfferLetter")
    suspend fun printOfferLetter(
        @Query("cId") cId: String,
        @Query("outWardId") outwardId: String,
        @Query("letterDate") letterDate: String,
        @Query("stamp") stamp: Boolean
    ): Response<String>

    @POST("/candidateApi/printHikeLetter")
    suspend fun printHikeLetter(
        @Query("cId") cId: String,
        @Query("letterDate") letterDate: String,
        @Query("effectiveDate") effectiveDate: String,
        @Query("jobPosition") jobPosition: String,
        @Query("newPackage") newPackage: String,
        @Query("stamp") stamp: Boolean
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
        @Query("lop") lop:String
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
    suspend fun createUser(@Query("userName") userName: String,@Query("mobileNo") mobileNo: String,@Query("emailId") emailId: String,@Query("jobDesignation") jobDesignation: String,@Query("userType") userType: String,@Query("user_id") userId: Int) : Response<String>


    @POST("/userManagement/userList")
    suspend fun userList() : Response<List<UserListRes>>

    @POST("/userManagement/userStatus")
    suspend fun userStatus(@Query("user_id") userId: Int,@Query("status") status: Int) : Response<String>


    //CandidateRawData Api
    @POST("/candidateRawData/addMultipleRawData")
    suspend fun addMultipleRawData(): Response<String>
    @POST("/candidateRawData/deleteMultipleRawData")
    suspend fun deleteMultipleRawData(@Body rawList: ArrayList<RawDataList> ): Response<String>

    @POST("/candidateRawData/getAllRawData")
    suspend fun getAllRawData(): Response<List<RawDataList>>

    @POST("/candidateRawData/getEmpRawData")
    suspend fun getEmpRawData(): Response<List<RawDataList>>

    @POST("/candidateRawData/getOtherProsData")
    suspend fun getOtherProsData(@Query("otherUserId") userId: Int): Response<List<RawDataList>>

    @POST("/candidateRawData/getEmpListRawData")
    suspend fun getEmpListRawData(): Response<List<UserList>>
    @POST("/candidateRawData/getRawCandidateDet")
    suspend fun getRawCandidateDet(@Query("candidateId") candidateId: Int): Response<RawCandidateData>
    @POST("/candidateRawData/updateRawCandidateDet")
    suspend fun updateRawCandidateDet(@Query("candidateId") candidateId: Int,@Query("mobNo") mobNo: String,@Query("altermobNo") altermobNo: String): Response<String>


    @POST("/candidateRawData/setEmpRawDataComment")
    suspend fun setEmpRawDataComment(
        @Query("callTime") callTime: String,
        @Query("prosType") prosType: String,
        @Query("remark") remark: String,
        @Query("folloupDate") folloupDate: String,
        @Query("selectedItem") selectedItem: String,
        @Query("candidateId") candidateId: String,
        @Query("prosUpdate")update: Int,
        @Query("mobNo")mobNo:String,
        @Query("alterMobNo")alternateMob:String
    ): Response<String>

    //Get Candidate Id list for today's Follow up
    @POST("/candidateRawData/getFollowUpList")
    suspend fun getFollowUpList(): Response<List<Int>>


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


    @POST("/whatsAppTemp/UpdateTempText")
    suspend fun updateWhatsApiTempText(@Body temp:WhatsappTemplateMsg)

    @Multipart
    @POST("/whatsAppTemp/UpdateWhatsApiAttachments")
    suspend fun UpdateWhatsApiAttachments(@Part spic: MultipartBody.Part,
                                          @Query("tempId")tempId:String,
                                          @Query("header") header: String,
                                          @Query("template") template:String,
                                          @Query("headerType") headerType:String,
    ) :Response<String>

    @POST("/whatsAppTemp/updateTempWithoutHeader")
    suspend fun updateTempWithoutImage(@Body temp:WhatsappTemplateMsg)

    @Multipart
    @POST("/whatsAppTemp/ChangeWhatsApiAttachments")
    suspend fun ChangeWhatsApiAttachments(@Part spic: MultipartBody.Part, @Query("tempId")tempId:String,@Query("headerType")headerType: String) :Response<String>



}