package com.wendytheqoo.aroundme.utils

class Constant {
    companion object {
        val REQUEST_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        const val ALL_LOCATION_PERMISSIONS_GRANTED = 2;
        const val ONLY_COARSE_LOCATION_PERMISSION_GRANTED = 1
    }
}