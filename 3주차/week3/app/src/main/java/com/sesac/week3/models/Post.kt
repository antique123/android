package com.sesac.week3.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val category: String,
    val categorySignature: String,
    val body: String,
    val uris: MutableList<String>
): Parcelable