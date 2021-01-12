package vib.oth.archaeological_fieldwork.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import vib.oth.archaeological_fieldwork.models.Location
import java.io.IOException

fun mapSearch(locationSearch: String?, context: Context): Address? {
  var addressList: List<Address>? = null
  if (locationSearch.isNullOrEmpty()) return null
  val geocoder = Geocoder(context)
  try {
    addressList = geocoder.getFromLocationName(locationSearch, 1)
  } catch (e: IOException) {
    return null
  }
  if(addressList.isEmpty()) return null
  return addressList?.get(0)
}

fun mapSearch(location: LatLng, context: Context): Address? {
  var addressList: List<Address>? = null
  val geocoder = Geocoder(context)
  try {
    addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
  } catch (e: IOException) {
    return null
  }

  return addressList?.get(0)
}

fun moveMapTo(latLng: LatLng, map: GoogleMap) {
  map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
}

fun moveMapTo(location: Location, map: GoogleMap) {
  map.animateCamera(CameraUpdateFactory.newLatLngZoom(location.toLanLng(),location.zoom))
}