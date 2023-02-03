package com.venter.regodigital.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.venter.regodigital.api.UserAuthApi
import com.venter.regodigital.models.*
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import org.json.JSONObject
import retrofit2.http.Query
import javax.inject.Inject

class UserAuthRepository @Inject constructor(private val userApi: UserAuthApi) {

    private val _candidateCreateReqResLiveData = MutableLiveData<NetworkResult<CandidateCrateRes>>()
    val candidateCreateReqResLiveData: LiveData<NetworkResult<CandidateCrateRes>>
        get() = _candidateCreateReqResLiveData

    suspend fun createCandidateRepo(candidate: CandidetDetails) {
        try {

            _candidateCreateReqResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.createCandidate(candidate)

            if (response.isSuccessful && response.body() != null) {

                _candidateCreateReqResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _candidateCreateReqResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _candidateCreateReqResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(
                Constans.TAG,
                "Error in UserAuthRepository.kt createCandidateRepo() is " + e.message
            )
        }
    }


    private val _candidateListLiveData = MutableLiveData<NetworkResult<List<CandidateList>>>()
    val candidateListLiveData: LiveData<NetworkResult<List<CandidateList>>>
        get() = _candidateListLiveData

    suspend fun candidateList() {
        try {
            _candidateListLiveData.postValue(NetworkResult.Loading())
            val response = userApi.candidateList()
            if (response.isSuccessful && response.body() != null) {

                _candidateListLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _candidateListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _candidateListLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt candidateList() is " + e.message)
        }
    }


    private val _candidateCertLiveData = MutableLiveData<NetworkResult<CanCertDetRes>>()
    val candidateCertLiveData: LiveData<NetworkResult<CanCertDetRes>>
        get() = _candidateCertLiveData

    suspend fun candidateCerDet(candidateId: Int) {
        try {
            _candidateCreateReqResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.candidateCertDet(candidateId.toString())

            if (response.isSuccessful && response.body() != null) {

                _candidateCertLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _candidateCertLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _candidateCertLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt candidateCerDet() is " + e.message)
        }
    }


    private val _stringResLiveData = MutableLiveData<NetworkResult<String>>()
    val stringResLiveData: LiveData<NetworkResult<String>>
        get() = _stringResLiveData

    suspend fun printCandidateProfile(candidateId: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printCandidateProfile(candidateId.toString())

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(
                Constans.TAG,
                "Error in UserAuthRepository.kt printCandidateProfile() is " + e.message
            )
        }
    }

    suspend fun printOfferLetter(
        candidateId: Int,
        outWard: String,
        letterDate: String,
        stamp: Boolean
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.printOfferLetter(candidateId.toString(), outWard, letterDate, stamp)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(
                Constans.TAG,
                "Error in UserAuthRepository.kt printCandidateProfile() is " + e.message
            )
        }
    }


    suspend fun printHikeLetter(
        cId: String,
        letterDate: String,
        effectiveDate: String,
        jobPosition: String,
        newPackage: String,
        stamp: Boolean
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printHikeLetter(
                cId,
                letterDate,
                effectiveDate,
                jobPosition,
                newPackage,
                stamp
            )

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }

    suspend fun printExperienceLetter(
        cId: String,
        letterDate: String,
        relaseDate: String,
        stamp: Boolean
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printExperienceLetter(cId, letterDate, relaseDate, stamp)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }


    suspend fun printRelievingLetter(
        cId: String,
        letterDate: String,
        resignDate: String,
        relaseDate: String,
        stamp: Boolean
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.printRelievingLetter(cId, letterDate, resignDate, relaseDate, stamp)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }

    suspend fun printSalarySlip(
        cId: String,
        month: String,
        year: String,
        jobPos: String,
        packages: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printSalarySlip(cId, month, year, jobPos, packages)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }

    suspend fun printInternshipLetter(
        candidateId: Int,
        outWard: String,
        letterDate: String,
        stamp: Boolean,
        stamped: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printInternshipLetter(
                candidateId.toString(),
                outWard,
                letterDate,
                stamp,
                stamped
            )

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(
                Constans.TAG,
                "Error in UserAuthRepository.kt printCandidateProfile() is " + e.message
            )
        }
    }

    suspend fun printInternshipCertificate(
        cId: String,
        letterDate: String,
        relaseDate: String,
        stamp: Boolean
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printInternshipCertificate(cId, letterDate, relaseDate, stamp)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }

    suspend fun printIdCard(cId: String)
    {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printIdCard(cId)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt printIdCard() is " + e.message)
        }
    }


    private val _candidateDetResLiveData = MutableLiveData<NetworkResult<CandidetDetails>>()
    val candidateDetResLiveData: LiveData<NetworkResult<CandidetDetails>>
        get() = _candidateDetResLiveData

    suspend fun getCandidateDet(cId: String)
    {
        try {
            _candidateDetResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getCandidateDet(cId)

            if (response.isSuccessful && response.body() != null) {

                _candidateDetResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _candidateDetResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _candidateDetResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt getCandidateDet() is " + e.message)
        }
    }


    private val _candidateFeeListResLiveData = MutableLiveData<NetworkResult<List<CandidateFeeList>>>()
    val candidateFeeListResLiveData: LiveData<NetworkResult<List<CandidateFeeList>>>
        get() = _candidateFeeListResLiveData
    suspend fun getCandidateFeeList()
    {
        try {
            _candidateFeeListResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getCandidateFeeDet()

            if (response.isSuccessful && response.body() != null) {

                _candidateFeeListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _candidateFeeListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _candidateFeeListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt getCandidateFeeList() is " + e.message)
        }
    }

    private val _feeLedgerResLiveData = MutableLiveData<NetworkResult<List<FeeLedger>>>()
    val feeLedgerResLiveData: LiveData<NetworkResult<List<FeeLedger>>>
        get() = _feeLedgerResLiveData
    suspend fun getCandidateFeeLedger(cId: String)
    {
        try {
            _feeLedgerResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getCandidateFeeLedger(cId)

            if (response.isSuccessful && response.body() != null) {

                _feeLedgerResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _feeLedgerResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _feeLedgerResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in UserAuthRepository.kt getCandidateFeeLedger() is " + e.message)
        }
    }

    suspend fun submitFeeReceipt( cId: String,rcptDate: String,rcptAmt: String, remark: String, nextPayDate: String,candidateNaem:String)
    {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.candidateFeeReceipt(cId,rcptDate,rcptAmt, remark, nextPayDate,candidateNaem)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt submitFeeReceipt() is " + e.message)
        }
    }

    suspend fun expenseReceipt(rcptDate: String,transCat: String, tranType: String,traDesc: String,rcptAmt: String)
    {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.expenseReceipt(rcptDate,transCat,tranType,traDesc,rcptAmt)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt expenseReceipt() is " + e.message)
        }
    }


    private val _expenseReportResLiveData = MutableLiveData<NetworkResult<ExpenseReportRes>>()
    val expenseReportResLiveData: LiveData<NetworkResult<ExpenseReportRes>>
        get() = _expenseReportResLiveData
    suspend fun expenseReport()
    {
        try {
            _expenseReportResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.expenseReport()
            

            if (response.isSuccessful && response.body() != null) {

                _expenseReportResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _expenseReportResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _expenseReportResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt expenseReport() is " + e.message)
        }
    }


}