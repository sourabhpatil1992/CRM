package com.venter.regodigital.api

import com.venter.regodigital.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAuthApi {

    @POST("/candidateApi/createCandidate")
    suspend fun createCandidate(@Body candidate: CandidetDetails): Response<CandidateCrateRes>

    @Multipart
    @POST("/candidateApi/candidateProfileDp")
    suspend fun createCandidateProfile(
        @Part spic: MultipartBody.Part,
        @Query("cId") cId: String
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
        @Query("stamp") stamp: Boolean
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
        @Query("package") packages: String
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

    @POST("/candidateFeeApi/feeList")
    suspend fun getCandidateFeeDet() : Response<List<CandidateFeeList>>

    @POST("/candidateFeeApi/feeLedger")
    suspend fun getCandidateFeeLedger(@Query("cId") cId: String) : Response<List<FeeLedger>>

    @POST("/candidateFeeApi/candidateFeeReceipt")
    suspend fun candidateFeeReceipt(@Query("cId") cId: String,@Query("rcptDate") rcptDate: String,@Query("rcptAmt") rcptAmt: String,@Query("remark") remark: String,@Query("nextPayDate") nextPayDate: String,@Query("candidateName") candidateName: String) : Response<String>

    @POST("/expenseManagement/expenseReceipt")
    suspend fun expenseReceipt(@Query("rcptDate") rcptDate: String,@Query("transCat") transCat: String,@Query("tranType") tranType: String,@Query("traDesc") traDesc: String,@Query("rcptAmt") rcptAmt: String) : Response<String>

    @POST("/expenseManagement/expenseReport")
    suspend fun expenseReport() : Response<ExpenseReportRes>




}