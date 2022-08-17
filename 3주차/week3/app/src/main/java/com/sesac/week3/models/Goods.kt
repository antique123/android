package com.sesac.week3.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Goods (
    val imageURL: MutableList<String>,
    val explain: String,
    val location: String,
    val price: String,
    var productID: String
) : Parcelable
