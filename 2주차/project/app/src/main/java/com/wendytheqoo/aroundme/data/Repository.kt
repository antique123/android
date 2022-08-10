package com.wendytheqoo.aroundme.data

import com.wendytheqoo.aroundme.retrofit.ServiceInterface

class Repository(private val serviceInterface: ServiceInterface) {
    suspend fun getAddressByCoordinate(clientId: String, clientSecret: String, coords: String, output: String)
        = serviceInterface.getAddressByCoordinate(clientId, clientSecret, coords, output)
}