package com.venter.crm.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmpDocFiles(
    val imageUri: Uri?
): Parcelable