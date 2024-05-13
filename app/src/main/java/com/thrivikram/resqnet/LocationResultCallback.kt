package com.thrivikram.resqnet

import android.location.Location

interface LocationResultCallback {
    fun onLocationResult(location: Location?)
}