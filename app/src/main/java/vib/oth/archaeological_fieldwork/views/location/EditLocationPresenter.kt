package vib.oth.archaeological_fieldwork.views.location

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import java.io.IOException


class EditLocationPresenter(view: BaseView) : BasePresenter(view), AnkoLogger {

  var location = Location()
  lateinit var map: GoogleMap

  init {
    location = view.intent.extras?.getParcelable("location")!!
  }

  fun doConfigureMap(map: GoogleMap) {
    val loc = LatLng(location.lat, location.lng)
    val options = MarkerOptions()
      .title("Site")
      .snippet("GPS : " + loc.toString())
      .draggable(true)
      .position(loc)
    map.addMarker(options)
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
    val loc = LatLng(location.lat, location.lng)
    marker.setSnippet("GPS : " + loc.toString())
  }

  fun moveTo(latLng: LatLng, title: String) {
    info("move to $latLng")
    map.addMarker(MarkerOptions().position(latLng).title(title))
    map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
  }

  fun doOnMapSearch(locationSearch: String?) {
    info("name = $locationSearch")
    var addressList: List<Address>? = null
    if (locationSearch.isNullOrEmpty()) return

    val geocoder = Geocoder(view?.baseContext)
    try {
      addressList = geocoder.getFromLocationName(locationSearch, 1)
    } catch (e: IOException) {
      error(e)
      return
    }
    if (addressList!!.size == 0) return

    val address: Address = addressList[0]
    moveTo(LatLng(address.latitude, address.longitude), locationSearch)
    doUpdateLocation(address.latitude, address.longitude)
  }
}


