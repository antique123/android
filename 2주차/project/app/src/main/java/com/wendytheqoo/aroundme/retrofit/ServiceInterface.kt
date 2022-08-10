package com.wendytheqoo.aroundme.retrofit

import com.wendytheqoo.aroundme.data.model.CurrentAddress
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ServiceInterface {
    @GET("/map-reversegeocode/v2/gc")
    suspend fun getAddressByCoordinate(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("coords") coords: String,
        @Query("output") output: String
    ): Response<CurrentAddress>
}