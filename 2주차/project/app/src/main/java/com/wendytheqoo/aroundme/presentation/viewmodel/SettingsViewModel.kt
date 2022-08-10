package com.wendytheqoo.aroundme.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wendytheqoo.aroundme.data.model.CurrentAddress
import com.wendytheqoo.aroundme.data.Repository
import com.wendytheqoo.aroundme.retrofit.RetrofitInstance
import com.wendytheqoo.aroundme.retrofit.ServiceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SettingsViewModel : ViewModel() {
    private val serviceInterface = RetrofitInstance.getInstance().create(ServiceInterface::class.java)
    private val repository = Repository(serviceInterface)

    var currentAddress = MutableLiveData<Response<CurrentAddress>>()

    fun getAddressByCoordinate(clientId: String, clientSecret: String, coords: String, output: String) {
        viewModelScope.launch {
            var response: Response<CurrentAddress>
            withContext(Dispatchers.IO) {
                response = repository.getAddressByCoordinate(clientId, clientSecret, coords, output)
            }
            currentAddress.value = response
        }
    }
}