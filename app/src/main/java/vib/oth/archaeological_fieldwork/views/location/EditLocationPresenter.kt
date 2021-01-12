package vib.oth.archaeological_fieldwork.views.location

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.*
import vib.oth.archaeological_fieldwork.helpers.createDefaultLocationRequest
import vib.oth.archaeological_fieldwork.helpers.mapSearch
import vib.oth.archaeological_fieldwork.helpers.moveMapTo
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import java.io.IOException


class EditLocationPresenter(view: BaseView) : BasePresenter(view), AnkoLogger {

  var location = Location()
  lateinit var map: GoogleMap
  private var marker: Marker? = null;

  init {
    location = view.intent.extras?.getParcelable("location")!!
  }

  fun doConfigureMap(map: GoogleMap) {
    map.uiSettings.isZoomControlsEnabled = true
    val loc = LatLng(location.lat, location.lng)
    val options = MarkerOptions()
        .title("")
//      .title("Site")
//      .snippet("GPS : " + loc.toString())
      .draggable(true)
      .position(loc)
    map.addMarker(options).showInfoWindow()
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    view?.showLocation(location);
    this.map = map
  }

  fun doUpdateLocation(lat: Double, lng: Double) {
    location.lat = lat
    location.lng = lng
  }

  fun doSave() {
    val resultIntent = Intent()
    info("save to $location")
    resultIntent.putExtra("location", location)
    view?.setResult(0, resultIntent)
    view?.finish()
  }

  fun doUpdateMarker(marker: Marker) {
//    doAsync {
//      uiThread {
//        val address: Address? = mapSearch(marker.position, view!!.baseContext)
//        if (address != null) {
//          marker.title = address.featureName
//          marker.snippet = "${address.locality}, ${address.adminArea}, ${address.countryName}"
//        }
//      }
//    }
  }

  fun doOnMapSearch(locationSearch: String?) {
    val address: Address = mapSearch(locationSearch, view!!.baseContext) ?: return
    moveMapTo(LatLng(address.latitude, address.longitude), map)
    doUpdateLocation(address.latitude, address.longitude)
    setMarker(LatLng(address.latitude, address.longitude))
  }

  fun setMarker(p0: LatLng) {
    marker?.remove()
    val options = MarkerOptions()
        .draggable(true)
        .position(p0)
    marker = map.addMarker(options)
  }
}


