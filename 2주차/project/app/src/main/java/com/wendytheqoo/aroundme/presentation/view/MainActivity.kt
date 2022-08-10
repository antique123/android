package com.wendytheqoo.aroundme.presentation.view

import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.wendytheqoo.aroundme.R
import com.wendytheqoo.aroundme.databinding.ActivityMainBinding
import com.wendytheqoo.aroundme.utils.Constant
import com.wendytheqoo.aroundme.utils.SystemUtility

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var naverMap: NaverMap
    private lateinit var deliveryManLocation: LatLng
    private var customerLocation = LatLng(37.5064312,126.885321)
    private var restaurantLocation = LatLng(37.5169837,126.8993953)

    private lateinit var resolutionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var isGranted = false

    private lateinit var deliveryManMarker: Marker

    private var isAlreadyInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {
        SystemUtility(window).setLightStatusBar()
        initViews()
        createLauncher()
        Log.d("PermissionsTest", "inner")

        requestPermissions()
    }

    private fun initViews() {
        Glide.with(binding.bottomSheet.deliveryManNameProfileImageView)
            .load("https://static.vecteezy.com/system/resources/thumbnails/002/534/006/small/social-media-chatting-online-blank-profile-picture-head-and-body-icon-people-standing-icon-grey-background-free-vector.jpg")
            .circleCrop()
            .into(binding.bottomSheet.deliveryManNameProfileImageView)
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
                    finish()
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
        isGranted = true
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

                if(!isAlreadyInit) {
                    val location = locationResult.locations[0]
                    deliveryManLocation = LatLng(location.latitude, location.longitude)
                    fusedLocationProviderClient?.removeLocationUpdates(this)
                    initMaps()

                } else {
                    val location = locationResult.locations[0]
                    deliveryManLocation = LatLng(location.latitude, location.longitude)
                    updateCamera()
                }
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
    }

    private fun initMaps() {
        val fragmentManager = supportFragmentManager
        val mapFragment = fragmentManager.findFragmentById(R.id.map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.map_view, it).commitAllowingStateLoss()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        initCamera()
    }

    private fun initCamera() {
        deliveryManMarker = Marker()
        deliveryManMarker.position = deliveryManLocation
        deliveryManMarker.map = naverMap
        deliveryManMarker.icon = OverlayImage.fromResource(R.drawable.deliveryman)


        val restaurantMarker = Marker()
        restaurantMarker.position = restaurantLocation
        restaurantMarker.map = naverMap
        restaurantMarker.icon = OverlayImage.fromResource(R.drawable.restaurant)

        val customerMarker = Marker()
        customerMarker.position = customerLocation
        customerMarker.map = naverMap
        customerMarker.icon = OverlayImage.fromResource(R.drawable.customer)

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        locationOverlay.position = deliveryManLocation


        val cameraUpdate = CameraUpdate.scrollTo(deliveryManLocation)
        naverMap.moveCamera(cameraUpdate)

        naverMap.minZoom = 5.0
        naverMap.maxZoom = 18.0

        isAlreadyInit = true

        initializeLocationProviderClient()
    }

    private fun updateCamera() {
        deliveryManMarker.map = null
        deliveryManMarker.position = deliveryManLocation
        deliveryManMarker.icon = OverlayImage.fromResource(R.drawable.deliveryman)
        deliveryManMarker.map = naverMap


        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        locationOverlay.position = deliveryManLocation

        Log.d("LifecycleTest", "delivery ${deliveryManLocation.latitude} ${deliveryManLocation.longitude}")

    }

    @SuppressLint("MissingPermission")
    private fun initializeLocationProviderClient() {
        getCurrentLocation()
    }

    private fun releaseLocationProviderClient() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        fusedLocationProviderClient = null
    }

    override fun onStart() {
        super.onStart()

        if(Build.VERSION.SDK_INT >= 24 && isGranted) {
            Log.d("LifecycleTest", "onStart:: GPS 업데이트를 재개합니다")
            initializeLocationProviderClient()
        }
    }

    /*
    * API Level 24 이상인 경우 멀티스크린이 지원되기 때문에
    * API Level 24 미만인 경우 onResume 콜백에서 GPS Tracking 시작
    * */
    override fun onResume() {
        super.onResume()

        if(Build.VERSION.SDK_INT < 24 && isGranted) {
            Log.d("LifecycleTest", "onResume:: GPS 업데이트를 재개합니다")
            initializeLocationProviderClient()
        }
    }


    override fun onPause() {
        super.onPause()

        if(Build.VERSION.SDK_INT < 24 && isGranted) {
            Log.d("LifecycleTest", "onPause:: GPS 업데이트를 중단합니다")
            releaseLocationProviderClient()
        }
    }

    /*
        * API Level 24 이상인 경우 멀티스크린이 지원되기 때문에
        * API Level 24 미만인 경우 onPause 콜백에서 GPS Tracking 종료
        *
        * 앱이 멀티스크린으로 동작하는 경우에는 앱의 액티비티가 정상적으로 모두 보이는 경우에도
        * 다른 앱의 액티비티에 포커스가 이동하는 경우 현재 앱의 액티비티는 PAUSE 상태로 진입한다
        * 이럴 경우 사용자 입장에서는 앱이 모두 보이는 상태인데 앱이 업데이트 되지 않는 것으로 보이기 때문에
        * 사용자에게 좋지 않은 경험을 줄 수 있다. 그렇기 때문에 멀티스크린이 지원되는 API Level 이상에서는
        * 액티비티가 전부 가려졌을 경우(onStop 콜백 호출)에만 GPS 트래킹을 종료하도록 한다.
        * */
    override fun onStop() {
        super.onStop()

        if(Build.VERSION.SDK_INT >= 24 && isGranted) {
            Log.d("LifecycleTest", "onStop:: GPS 업데이트를 중단합니다")
            releaseLocationProviderClient()
        }
    }
}