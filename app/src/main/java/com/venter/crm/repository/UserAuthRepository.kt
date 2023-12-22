package com.venter.crm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.venter.crm.api.UserAuthApi
import com.venter.crm.models.*
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import org.json.JSONObject
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
                TAG,
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
            Log.d(TAG, "Error in UserAuthRepository.kt candidateCerDet() is " + e.message)
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
                TAG,
                "Error in UserAuthRepository.kt printCandidateProfile() is " + e.message
            )
        }
    }

    suspend fun removeCandidate(id: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.removeCandidate(id)

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
                TAG,
                "Error in UserAuthRepository.kt removeCandidate() is " + e.message
            )
        }
    }

    suspend fun printOfferLetter(
        candidateId: Int,
        outWard: String,
        letterDate: String,
        stamp: Boolean,
        varAmt: Int
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.printOfferLetter(candidateId.toString(), outWard, letterDate, stamp, varAmt)

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
                TAG,
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
        stamp: Boolean,
        varAmt: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printHikeLetter(
                cId,
                letterDate,
                effectiveDate,
                jobPosition,
                newPackage,
                stamp, varAmt
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
            Log.d(TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }

    suspend fun printExperienceLetter(
        cId: String,
        letterDate: String,
        relaseDate: String,
        stamp: Boolean,
        jobActivity: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.printExperienceLetter(cId, letterDate, relaseDate, stamp, jobActivity)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
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
            Log.d(TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }

    suspend fun printSalarySlip(
        cId: String,
        month: String,
        year: String,
        jobPos: String,
        packages: String,
        lop: String,
        checked: Boolean,
        salaryDays: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.printSalarySlip(
                cId,
                month,
                year,
                jobPos,
                packages,
                lop,
                checked,
                salaryDays
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
            Log.d(TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
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
                TAG,
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
            Log.d(TAG, "Error in UserAuthRepository.kt printHikeLetter() is " + e.message)
        }
    }

    suspend fun printIdCard(cId: String) {
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
            Log.d(TAG, "Error in UserAuthRepository.kt printIdCard() is " + e.message)
        }
    }


    private val _candidateDetResLiveData = MutableLiveData<NetworkResult<CandidetDetails>>()
    val candidateDetResLiveData: LiveData<NetworkResult<CandidetDetails>>
        get() = _candidateDetResLiveData

    suspend fun getCandidateDet(cId: String) {
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
            Log.d(TAG, "Error in UserAuthRepository.kt getCandidateDet() is " + e.message)
        }
    }


    private val _candidateFeeListResLiveData =
        MutableLiveData<NetworkResult<List<CandidateFeeList>>>()
    val candidateFeeListResLiveData: LiveData<NetworkResult<List<CandidateFeeList>>>
        get() = _candidateFeeListResLiveData

    suspend fun getCandidateFeeList() {
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
            Log.d(
                TAG,
                "Error in UserAuthRepository.kt getCandidateFeeList() is " + e.message
            )
        }
    }

    private val _feeLedgerResLiveData = MutableLiveData<NetworkResult<FeeLedgerDet>>()
    val feeLedgerResLiveData: LiveData<NetworkResult<FeeLedgerDet>>
        get() = _feeLedgerResLiveData

    suspend fun getCandidateFeeLedger(cId: String) {
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
            Log.d(
                TAG,
                "Error in UserAuthRepository.kt getCandidateFeeLedger() is " + e.message
            )
        }
    }

    suspend fun submitFeeReceipt(
        cId: String,
        rcptDate: String,
        rcptAmt: String,
        remark: String,
        nextPayDate: String,
        candidateNaem: String,
        rcptId: Int?,
        transType: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.candidateFeeReceipt(
                cId,
                rcptDate,
                rcptAmt,
                remark,
                nextPayDate,
                candidateNaem,
                rcptId,
                transType
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
            Log.d(TAG, "Error in UserAuthRepository.kt submitFeeReceipt() is " + e.message)
        }
    }

    suspend fun expenseReceipt(
        rcptDate: String,
        transCat: String,
        tranType: String,
        traDesc: String,
        rcptAmt: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.expenseReceipt(rcptDate, transCat, tranType, traDesc, rcptAmt)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt expenseReceipt() is " + e.message)
        }
    }


    private val _expenseReportResLiveData = MutableLiveData<NetworkResult<ExpenseReportRes>>()
    val expenseReportResLiveData: LiveData<NetworkResult<ExpenseReportRes>>
        get() = _expenseReportResLiveData

    suspend fun expenseReport() {
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

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt expenseReport() is " + e.message)
        }
    }

    private val _expenseReportPrintResLiveData = MutableLiveData<NetworkResult<List<expensesDet>>>()
    val expenseReportPrintResLiveData: LiveData<NetworkResult<List<expensesDet>>>
        get() = _expenseReportPrintResLiveData

    suspend fun expenseReportPrint(toDate: String, fromData: String) {
        try {
            _expenseReportPrintResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.expenseReportPrint(toDate, fromData)


            if (response.isSuccessful && response.body() != null) {

                _expenseReportPrintResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _expenseReportPrintResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _expenseReportPrintResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt expenseReport() is " + e.message)
        }
    }

    suspend fun createUser(
        userName: String,
        mobNo: String,
        emailId: String,
        designation: String,
        userType: String,
        userId: Int
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.createUser(userName, mobNo, emailId, designation, userType, userId)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt createUser() is " + e.message)
        }
    }

    private val _userListResLiveData = MutableLiveData<NetworkResult<List<UserListRes>>>()
    val userListResLiveData: LiveData<NetworkResult<List<UserListRes>>>
        get() = _userListResLiveData

    suspend fun userList() {
        try {
            _userListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.userList()

            if (response.isSuccessful && response.body() != null) {

                _userListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _userListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _userListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt userList() is " + e.message)
        }
    }

    suspend fun userStatus(userId: Int, status: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.userStatus(userId, status)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt userStatus() is " + e.message)
        }
    }

    suspend fun addMultipleRawData() {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.addMultipleRawData()

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt addRawData() is " + e.message)
        }
    }

    suspend fun deleteMultipleRawData(rawList: ArrayList<RawDataList>) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response =
                userApi.deleteMultipleRawData(rawList)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt addRawData() is " + e.message)
        }
    }

    suspend fun updateCandidateRawData(candidateId: Int, mobNo: String, alterMobNo: String) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response =
                userApi.updateRawCandidateDet(candidateId, mobNo, alterMobNo)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt updateCandidateRawData() is " + e.message)
        }
    }

    suspend fun updateCustumerData(cust: CustUpdateDet) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response =
                userApi.updateCustumerData(cust)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt updateCandidateRawData() is " + e.message)
        }
    }

    private val _allrawDataListResLiveData = MutableLiveData<NetworkResult<List<RawDataList>>>()
    val allrawDataListResLiveData: LiveData<NetworkResult<List<RawDataList>>>
        get() = _allrawDataListResLiveData

    suspend fun getAllRawData(offset: Int) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getAllRawData(offset)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getAllRaw() is " + e.message)
        }
    }

    suspend fun getRawDataOfCamping(campId: Int) {

        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getRawDataOfCamping(campId)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getRawDataOfCamping() is : ${e.message}")
        }
    }

    suspend fun deleteCamping(campId: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.deleteCamping(campId)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt setEmpRawDataComment() is " + e.message)
        }
    }

    suspend fun getEmpRawData() {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpRawData()

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpRaw() is " + e.message)
        }
    }

    suspend fun getEmpCampRawData(campType:String) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpCampRawData(campType)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpRaw() is " + e.message)
        }
    }

    suspend fun getEmpProsData() {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpProsData()

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpRaw() is " + e.message)
        }
    }

    suspend fun getEmpSearchData(serach: String) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpSearchData(serach)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpSearchData() is " + e.message)
        }
    }

    private val _othersrawDataListResLiveData = MutableLiveData<NetworkResult<List<RawDataList>>>()
    val othersrawDataListResLiveData: LiveData<NetworkResult<List<RawDataList>>>
        get() = _othersrawDataListResLiveData

    suspend fun getOthersProsData(userId: Int) {
        try {
            _othersrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getOtherProsData(userId)

            if (response.isSuccessful && response.body() != null) {

                _othersrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _othersrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _othersrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpRaw() is " + e.message)
        }
    }

    private val _userListLiveData = MutableLiveData<NetworkResult<List<UserList>>>()
    val userListLiveData: LiveData<NetworkResult<List<UserList>>>
        get() = _userListLiveData

    suspend fun getEmpListRawData() {
        try {
            _userListLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpListRawData()

            if (response.isSuccessful && response.body() != null) {

                _userListLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _userListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _userListLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpListRawData() is " + e.message)
        }
    }

    suspend fun getIncomingData(userId: Int) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getIncomingData(userId)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpListRawData() is " + e.message)
        }
    }

    suspend fun getStealData(userId: Int) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getStealData(userId)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpListRawData() is " + e.message)
        }
    }

    suspend fun getEmpNotResData(userId: Int) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpNotResData(userId)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpListRawData() is " + e.message)
        }
    }


    private val _rawCandidateDataLiveData = MutableLiveData<NetworkResult<RawCandidateData>>()
    val rawCandidateDataLiveData: LiveData<NetworkResult<RawCandidateData>>
        get() = _rawCandidateDataLiveData

    suspend fun getRawCandidateData(candidateID: Int) {
        try {
            _rawCandidateDataLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getRawCandidateDet(candidateID)

            if (response.isSuccessful && response.body() != null) {

                _rawCandidateDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _rawCandidateDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _rawCandidateDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getRawCandidateData() is " + e.message)
        }
    }


    /*suspend fun setEmpRawDataComment(
        callTime: String,
        prosType: String,
        remark: String,
        folloupDate: String,
        selectedItem: String,
        candiateId: String,
        update: Int, mobNo: String, alternateMob: String, prosLevel: String
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.setEmpRawDataComment(
                    callTime,
                    prosType,
                    remark,
                    folloupDate,
                    selectedItem,
                    candiateId,
                    update,mobNo,alternateMob,prosLevel
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
            Log.d(TAG, "Error in UserAuthRepository.kt setEmpRawDataComment() is " + e.message)
        }
    }*/

    suspend fun setEmpRawDataComment(commentData: RawCommentData) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.setEmpRawDataComment(commentData)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt setEmpRawDataComment() is " + e.message)
        }
    }



    /*private val _intListResLiveData = MutableLiveData<NetworkResult<List<Int>>>()
    val intListResLiveData: LiveData<NetworkResult<List<Int>>>
        get() = _intListResLiveData*/

    suspend fun getFollowUpList(userId: Int, fromDate: String, toDate: String) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getFollowUpList(userId, fromDate, toDate)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getFollowUpList() is " + e.message)
        }
    }

    private val _empReptResLiveData = MutableLiveData<NetworkResult<EmpReport>>()
    val empReptResLiveData: LiveData<NetworkResult<EmpReport>>
        get() = _empReptResLiveData

    suspend fun getEmpReport(fromDate: String, toDate: String, empId: String) {
        try {
            _empReptResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpReport(fromDate, toDate, empId)

            if (response.isSuccessful && response.body() != null) {

                _empReptResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _empReptResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _empReptResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpReport() is " + e.message)
        }
    }

    suspend fun getTeleReport(fromDate: String, toDate: String) {
        try {
            _empReptResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getTeleReport(fromDate, toDate)

            if (response.isSuccessful && response.body() != null) {

                _empReptResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _empReptResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _empReptResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpReport() is " + e.message)
        }
    }


    private val _salarySlipResLiveData = MutableLiveData<NetworkResult<List<SalarySlipDet>>>()
    val salarySlipResLiveData: LiveData<NetworkResult<List<SalarySlipDet>>>
        get() = _salarySlipResLiveData

    suspend fun getSalaryList(cId: Int) {
        try {
            _salarySlipResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getSalaryList(cId)

            if (response.isSuccessful && response.body() != null) {

                _salarySlipResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _salarySlipResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _salarySlipResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getSalaryList() is " + e.message)
        }
    }

    suspend fun setIncomingLead(canName: String, canMob: String) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.setIncomingLead(canName, canMob)

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
                TAG,
                "Error in UserAuthRepository.kt setIncomingLead() is " + e.message
            )
        }

    }

    suspend fun getAdmissionData() {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getAdmissionData()

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getAdmissionData() is " + e.message)
        }
    }


    private val _workHrsResLiveData = MutableLiveData<NetworkResult<List<WorkingHrs>>>()
    val workHrsResLiveData: LiveData<NetworkResult<List<WorkingHrs>>>
        get() = _workHrsResLiveData

    suspend fun getWorkingHrsData() {
        try {
            _workHrsResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getWorkingHrs()

            if (response.isSuccessful && response.body() != null) {

                _workHrsResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _workHrsResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _workHrsResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getWorkingHrsData() is " + e.message)
        }
    }

    suspend fun setWorkingHrsData(shedule: WorkingHrs) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.setWorkingHrs(shedule)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt setWorkingHrsData() is " + e.message)
        }
    }


    private val _todaySheduleResLiveData = MutableLiveData<NetworkResult<userStatus>>()
    val todaySheduleResLiveData: LiveData<NetworkResult<userStatus>>
        get() = _todaySheduleResLiveData

    suspend fun getTodaysShedule() {
        try {
            _todaySheduleResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getTodayShedule()

            if (response.isSuccessful && response.body() != null) {

                _todaySheduleResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _todaySheduleResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _todaySheduleResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getTodaysShedule() is " + e.message)
        }
    }


    private val _msgListResLiveData = MutableLiveData<NetworkResult<List<WhatsappTemplateMsg>>>()
    val msgListResLiveData: LiveData<NetworkResult<List<WhatsappTemplateMsg>>>
        get() = _msgListResLiveData

    suspend fun getMsgList() {
        try {
            _msgListResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getWhatsMsgList()

            if (response.isSuccessful && response.body() != null) {

                _msgListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _msgListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _msgListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(
                TAG,
                "Error in UserAuthRepository.kt getMsgList() is " + e.message
            )
        }
    }

    private val _msgNameListResLiveData = MutableLiveData<NetworkResult<List<WhatsTempNameList>>>()
    val msgNameListResLiveData: LiveData<NetworkResult<List<WhatsTempNameList>>>
        get() = _msgNameListResLiveData

    suspend fun getMsgNameList() {
        try {
            _msgNameListResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getMsgNameList()

            if (response.isSuccessful && response.body() != null) {

                _msgNameListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _msgNameListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _msgNameListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(
                TAG,
                "Error in UserAuthRepository.kt getMsgNameList() is " + e.message
            )
        }
    }


    suspend fun whatsApiTextUpdate(temp: WhatsappTemplateMsg) {
        try {


            userApi.updateWhatsApiTempText(temp)


        } catch (e: Exception) {
            Log.d(TAG, "Error in UserApiRepostitory.kt whatsApiTextUpdate is " + e.message)
        }
    }


    private val _whatsAccListResLiveData = MutableLiveData<NetworkResult<List<WhatsAppAccList>>>()
    val whatsAccListResLiveData: LiveData<NetworkResult<List<WhatsAppAccList>>>
        get() = _whatsAccListResLiveData

    suspend fun getWhatsAppList() {
        try {
            _whatsAccListResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.getWhatsAppList()


            if (response.isSuccessful && response.body() != null) {

                _whatsAccListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _whatsAccListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _whatsAccListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }


        } catch (e: Exception) {
            Log.d(TAG, "Error in UserApiRepostitory.kt getWhatsAppList() is " + e.message)
        }
    }

    suspend fun createWhatsAppAcc() {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.createWhatsAppAcc()

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }


        } catch (e: Exception) {
            Log.d(TAG, "Error in UserApiRepostitory.kt createWhatsAppAcc() is " + e.message)
        }
    }

    suspend fun updateTemp(tempId: String, template: String, header: String, tempName: String) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.updateTemp(tempId, template, header, tempName)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }


        } catch (e: Exception) {
            Log.d(TAG, "Error in UserApiRepostitory.kt updateTemp is " + e.message)
        }
    }


    suspend fun getColdRawData(offset: Int) {
        try {
            _allrawDataListResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.getEmpColdRawData(offset)

            if (response.isSuccessful && response.body() != null) {

                _allrawDataListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _allrawDataListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _allrawDataListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt getEmpRaw() is " + e.message)
        }
    }

    suspend fun updateComment(commentId: Int, comment: String) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response =
                userApi.updateComment(commentId, comment)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.kt updateComment() is " + e.message)

        }

    }

    suspend fun intilWhats(id: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.intilWhats(id)
            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt intilWhats() is " + e.message)

        }

    }

    suspend fun logOutWhats(id: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.logOutWhatsapp(id)
            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt intilWhats() is " + e.message)

        }

    }

    suspend fun updateWhatsAppAcc(id: Int, userId: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.updateWhatsAppAcc(id, userId)
            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt intilWhats() is " + e.message)

        }

    }

    suspend fun dataTransfer(
        userId: Int,
        dataTo: Int,
        cold: Int,
        hot: Int,
        warm: Int,
        notRes: Int,
        admission: Int,
        raw: Int
    ) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response =
                userApi.dataTransfer(userId, dataTo, cold, hot, warm, notRes, admission, raw)
            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt dataTransfer() is " + e.message)

        }
    }

    suspend fun swipeData(dataId: Int, userId: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.swipeData(dataId, userId)
            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt swipeData() is " + e.message)

        }
    }

    private val _userReportListResLiveData = MutableLiveData<NetworkResult<List<UserReportData>>>()
    val userReportListResLiveData: LiveData<NetworkResult<List<UserReportData>>>
        get() = _userReportListResLiveData

    suspend fun getUserReport(userId: Int, toDate: String, fromDate: String) {
        try {
            _userReportListResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.getUserReport(userId, toDate, fromDate)
            if (response.isSuccessful && response.body() != null) {

                _userReportListResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _userReportListResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _userReportListResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt getUserReport() is " + e.message)

        }
    }

    suspend fun delAcc(id: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.delAcc(id)
            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt delAcc() is " + e.message)

        }
    }

    suspend fun resetDevice(id: Int) {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.resetDevice(id)

            if (response.isSuccessful && response.body() != null) {

                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "Error in UserAuthRepository.kt delAcc() is " + e.message)

        }
    }

    private val _hikeLettersLiveData = MutableLiveData<NetworkResult<List<HikeLetterDet>>>()
    val hikeLettersLiveData: LiveData<NetworkResult<List<HikeLetterDet>>>
        get() = _hikeLettersLiveData

    suspend fun getHikeLetterList(cId: Int) {
        try {
            _hikeLettersLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getHikeLetterList(cId)
            if (response.isSuccessful && response.body() != null) {
                _hikeLettersLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _hikeLettersLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _hikeLettersLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getHikeLetterList() is ${e.message}")
        }
    }

    suspend fun sendWhatsMsg(temNo: Int, mobNo: String) {

        userApi.sendWhatsMsg(temNo, mobNo)

    }


    private val _commentListLiveData = MutableLiveData<NetworkResult<List<RawDataComment>>>()
    val commentListLiveData: LiveData<NetworkResult<List<RawDataComment>>>
        get() = _commentListLiveData

    suspend fun commentList(commentId: Int) {
        try {
            _commentListLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getCommentList(commentId)
            if (response.isSuccessful && response.body() != null) {
                _commentListLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _commentListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _commentListLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke commentList() is ${e.message}")
        }
    }

    private val _userHierarchyDataLiveData = MutableLiveData<NetworkResult<UserHierarchyData>>()
    val userHierarchyDataLiveData: LiveData<NetworkResult<UserHierarchyData>>
        get() = _userHierarchyDataLiveData

    suspend fun getEmpHierarchyData(id: Int) {
        try {

            _userHierarchyDataLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getEmpHierarchyData(id)
            if (response.isSuccessful && response.body() != null) {
                _userHierarchyDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userHierarchyDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _userHierarchyDataLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke commentList() is ${e.message}")
        }
    }


    private val _subOrdinateDataLiveData = MutableLiveData<NetworkResult<SubOrdinateData>>()
    val subOrdinateDataLiveData: LiveData<NetworkResult<SubOrdinateData>>
        get() = _subOrdinateDataLiveData

    suspend fun getSubOrdinateList(selectedId: Int, userType: String) {
        try {

            _subOrdinateDataLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getSubOrdinateList(selectedId, userType)

            if (response.isSuccessful && response.body() != null) {
                _subOrdinateDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _subOrdinateDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _subOrdinateDataLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getSubOrdinateList() is ${e.message}")
        }
    }

    suspend fun updateUserHierarchy(id: Int?, slId: Int, flId: Int, tlId: Int, sbaId: Int) {
        try {

            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.updateUserHierarchy(id!!, slId, flId, tlId, sbaId)

            if (response.isSuccessful && response.body() != null) {
                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke updateUserHierarchy() is ${e.message}")
        }
    }

    suspend fun createEmployee(empDet: EmpDet) {

        try {

            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.createEmployee(empDet)

            if (response.isSuccessful && response.body() != null) {
                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke createEmployee() is ${e.message}")
        }

    }


    private val _empListLiveData = MutableLiveData<NetworkResult<List<EmployeeList>>>()
    val empListLiveData: LiveData<NetworkResult<List<EmployeeList>>>
        get() = _empListLiveData

    suspend fun getEmpList() {

        try {
            _empListLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getEmpList()

            if (response.isSuccessful && response.body() != null) {
                _empListLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _empListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _empListLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke updateUserHierarchy() is ${e.message}")
        }
    }


    private val _empInfoLiveData = MutableLiveData<NetworkResult<EmpInfoData>>()
    val empInfoLiveData: LiveData<NetworkResult<EmpInfoData>>
        get() = _empInfoLiveData

    suspend fun getEmpInfo(empId: Int) {

        try {
            _empInfoLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getEmpInfo(empId)

            if (response.isSuccessful && response.body() != null) {
                _empInfoLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _empInfoLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _empInfoLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getEmpInfo() is ${e.message}")
        }
    }


    private val _empDetLiveData = MutableLiveData<NetworkResult<EmpDet>>()
    val empDetLiveData: LiveData<NetworkResult<EmpDet>>
        get() = _empDetLiveData

    suspend fun getEmpDet(empId: Int) {

        try {
            _empDetLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getEmpDet(empId)

            if (response.isSuccessful && response.body() != null) {
                _empDetLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _empDetLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _empDetLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getEmpDet() is ${e.message}")
        }

    }

    private val _campDataLiveData = MutableLiveData<NetworkResult<List<CampData>>>()
    val campDataLiveData: LiveData<NetworkResult<List<CampData>>>
        get() = _campDataLiveData
    suspend fun getRawDataCamping() {
        try {
            _campDataLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getRawDataCamping()

            if (response.isSuccessful && response.body() != null) {
                _campDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _campDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _campDataLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getRawDataCamping() is ${e.message}")
        }
    }


    suspend fun updateConfiguration(conf: SystemConf)
    {
        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.updateConfiguration(conf)

            if (response.isSuccessful && response.body() != null) {
                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke updateConfiguration() is ${e.message}")
        }
    }


    private val _systemConfDataLiveData = MutableLiveData<NetworkResult<SystemConf>>()
    val systemConfDataLiveData: LiveData<NetworkResult<SystemConf>>
        get() = _systemConfDataLiveData
    suspend fun getConfiguration()
    {
        try {
            _systemConfDataLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getConfiguration()

            if (response.isSuccessful && response.body() != null) {
                _systemConfDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _systemConfDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _systemConfDataLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getConfiguration() is ${e.message}")
        }
    }

    private val _commentConfDataLiveData = MutableLiveData<NetworkResult<CommentConf>>()
    val commentConfDataLiveData: LiveData<NetworkResult<CommentConf>>
        get() = _commentConfDataLiveData
    suspend fun getCommentConfig() {

        try {
            _commentConfDataLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getCommentConfig()

            if (response.isSuccessful && response.body() != null) {
                _commentConfDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _commentConfDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _commentConfDataLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getCommentConfig() is ${e.message}")
        }
    }

    private val _prosSubTypeDataLiveData = MutableLiveData<NetworkResult<List<ProsSubType>>>()
    val prosSubTypeDataLiveData: LiveData<NetworkResult<List<ProsSubType>>>
        get() = _prosSubTypeDataLiveData
    suspend fun getProsSubConfig() {

        try {
            _prosSubTypeDataLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getProsSubConfig()

            if (response.isSuccessful && response.body() != null) {
                _prosSubTypeDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _prosSubTypeDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _prosSubTypeDataLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getProsSubConfig() is ${e.message}")
        }
    }

    suspend fun configEmail(mailConf: ConfigMailModel) {

        try {
            _stringResLiveData.postValue(NetworkResult.Loading())
            val response = userApi.configEmail(mailConf)

            if (response.isSuccessful && response.body() != null) {
                _stringResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _stringResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _stringResLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke configEmail() is ${e.message}")
        }
    }

    private val _emailListDataLiveData = MutableLiveData<NetworkResult<List<ConfigMailModel>>>()
    val emailListDataLiveData: LiveData<NetworkResult<List<ConfigMailModel>>>
        get() = _emailListDataLiveData
    suspend fun getEmailList() {

        try {
            _emailListDataLiveData.postValue(NetworkResult.Loading())
            val response = userApi.getEmailList()

            if (response.isSuccessful && response.body() != null) {
                _emailListDataLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _emailListDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _emailListDataLiveData.postValue(NetworkResult.Error("Something Went Wrong."))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserAuthRepository.ke getEmailList() is ${e.message}")
        }
    }


}