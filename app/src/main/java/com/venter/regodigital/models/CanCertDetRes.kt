package com.venter.regodigital.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.checkerframework.checker.fenum.qual.SwingTitlePosition

@Parcelize
data class CanCertDetRes(
    val first_name: String,
    val middel_name: String,
    val last_name: String,
    val mobile_no: String,
    val email_id: String,
    val address: String,
    val edu_degree: String,
    val passing_year: String,
    val current_job: String,
    val transFee:String,
    val cource_fee: Float,
    val paid_fee: Float,

    val offerletter_date: String?,
    val offerletter_outward: String?,
    val offerStamp:Int,
    val offerVari:Int =0,

    val hikeletter_date: String?,
    val effective_date: String?,
    val newPosition: String?,
    val newPackage: String?,
    val hikeStamp:Int,
    val hikeVari:Int =0,


    val last_date: String?,
    val expletter_date: String?,
    val expStamp:Int,

    val releivingleeter_date:String?,
    val resign_date:String?,
    val releiving_date:String?,
    val relativeStamp:Int,


    val salaryslip_package:String?,
    val salarysllip_jobpos:String?,
    val jobActivity:String?,
    val internletterDate:String?,
    val interOutwardIndex:String?,
    val internStipend:Float,
    val interofferStamp:Int,
    val icertDate:String?,
    val icrelaseDate:String?,
    val intercertStamp:Int



): Parcelable
