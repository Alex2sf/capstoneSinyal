package com.example.sinyal.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

class LocationConvert {
    companion object {
        fun toLatlng(lat: Double?, lng: Double?): LatLng? {
            println("test")
            // Menggunakan elvis operator untuk menggantikan blok if-else
            return lat?.let { LatLng(it, lng ?: 0.0) }
        }

        fun getStringAddress(
            latlng: LatLng?,
            context: Context
        ): String {
            var fullAddress = "No Location"

            try {
                latlng?.let {
                    val gc = Geocoder(context)
                    val list: List<Address> =
                        gc.getFromLocation(it.latitude, it.longitude, 1) as List<Address>
                    val address = if (list.isNotEmpty()) list[0] else null

                    address?.let {
                        val city = address.locality
                        val state = address.adminArea
                        val country = address.countryName

                        fullAddress = address.getAddressLine(0)
                            ?: buildString {
                                city?.let { append("$it, ") }
                                state?.let { append("$it, ") }
                                country?.let { append(it) }
                            }

                        if (fullAddress.isBlank()) {
                            fullAddress = "Location Name Unknown"
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("ERROR", "ERROR: $e")
            }

            return fullAddress
        }
    }
}
