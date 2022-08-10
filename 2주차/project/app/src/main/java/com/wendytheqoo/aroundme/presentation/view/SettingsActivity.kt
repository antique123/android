package com.wendytheqoo.aroundme.presentation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.wendytheqoo.aroundme.R
import com.wendytheqoo.aroundme.presentation.viewmodel.SettingsViewModel
import com.wendytheqoo.aroundme.databinding.ActivitySettingsBinding
import com.wendytheqoo.aroundme.utils.Constant
import com.wendytheqoo.aroundme.utils.SystemUtility

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this).get(SettingsViewModel::class.java) }
    private lateinit var resolutionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {
        SystemUtility(window).setLightStatusBar()

        initViews()
        initObservers()
        createLauncher()

    }

    private fun initViews() {
        binding.getMyLocationButton.setOnClickListener {
            requestPermissions()
        }
    }

    private fun initObservers() {
        viewModel.currentAddress.observe(this) {
            val address = it.body()?.results?.get(0)
            val currentAddress = "${address?.region?.area1?.name} ${address?.region?.area2?.name} ${address?.region?.area3?.name} ${address?.region?.area4?.name}"
            Log.d("currentAddress", currentAddress)

            //TODO 임시 코드
            // 추후에 Firebase 에 저장하도록 하여 MainActivity 로 latitude, longitude 를 넘기지 않도록 해야 한다
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("latitude", currentLocation.latitude)
            intent.putExtra("longitude", currentLocation.longitude)

            startActivity(intent)
            finish()
        }
    }

    private fun createLauncher() {
        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ result ->
            val response = result.entries.filter {
                it.key == android.Manifest.permission.ACCESS_FINE_LOCATION
                        || it.key == android.Manifest.permission.ACCESS_COARSE_LOCATION
            }

            when(response.filter { it.value == true }.size) {
                Constant.ALL_LOCATION_PERMISSIONS_GRANTED -> {
                    // request gps
                    requestGPS()
                }
                Constant.ONLY_COARSE_LOCATION_PERMISSION_GRANTED -> {
                    AlertDialog.Builder(this)
                        .setTitle("NOTICE")
                        .setMessage("You have only granted approximate location permissions. With that permission, you don't know the exact location.")
                        .setPositiveButton("닫기") {_, _ ->
                            //request gps
                            requestGPS()
                        }
                        .create().show()
                }
                else -> {
                    Toast.makeText(this, "위치 요청 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        resolutionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {
            if(it.resultCode == RESULT_OK) {
                //사용자가 GPS 요청 다이얼로그에서 허가한 경우 -> start get my location
                getCurrentLocation()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("deny GPS activate")
                    .setMessage("GPS 활성화가 거부되었습니다.")
                    .setNegativeButton("닫기", null)
                    .create().show()

            }
        }
    }

    private fun requestPermissions() {
        if(ContextCompat.checkSelfPermission(this, Constant.REQUEST_PERMISSIONS[0]) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(this, Constant.REQUEST_PERMISSIONS[1]) == PackageManager.PERMISSION_DENIED) {
            requestPermissionsLauncher.launch(Constant.REQUEST_PERMISSIONS)
        } else {
            //permissions are already granted -> request gps
            requestGPS()
        }
    }

    private fun requestGPS() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationSettingsRequestBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequestBuilder.build()).run {
            addOnSuccessListener {
                //gps is already activated -> get my location
                getCurrentLocation()
            }
            addOnFailureListener { e ->
                //gps is not activated
                if(e is ResolvableApiException) {
                    try {
                        //prepare system dialog
                        val intentSenderRequest = IntentSenderRequest.Builder(e.resolution).build()
                        resolutionLauncher.launch(intentSenderRequest)
                    } catch (sendEx: IntentSender.SendIntentException) { }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                for(location in locationResult.locations) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    fusedLocationProviderClient.removeLocationUpdates(this)
                    Log.d("getCurrentLocation", "${currentLocation.latitude} + ${currentLocation.longitude}")


                    val clientId: String = getString(R.string.client_id)
                    val clientSecret: String = getString(R.string.client_secret)
                    val coords = "${currentLocation.longitude},${currentLocation.latitude}"
                    val output: String = "json"

                    viewModel.getAddressByCoordinate(clientId, clientSecret, coords, output)
                }
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
    }
    /*
    companion object {
        val REQUEST_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        const val ALL_LOCATION_PERMISSIONS_GRANTED = 2;
        const val ONLY_COARSE_LOCATION_PERMISSION_GRANTED = 1
    }

     */
}