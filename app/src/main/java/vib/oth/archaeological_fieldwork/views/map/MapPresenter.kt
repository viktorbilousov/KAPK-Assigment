package vib.oth.archaeological_fieldwork.views.map

import android.annotation.SuppressLint
import android.location.Address
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import vib.oth.archaeological_fieldwork.helpers.mapSearch
import vib.oth.archaeological_fieldwork.helpers.moveMapTo
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW


class MapPresenter(view: BaseView) : BasePresenter(view) {

  lateinit var sites: List<Site>
  lateinit var map : GoogleMap
  private val markers: MutableList<Marker> = mutableListOf()
  private var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)


  fun doPopulateMap(map: GoogleMap, sites: List<Site>) {
    this.map = map;
    map.uiSettings.isZoomControlsEnabled = true
    sites.forEach {
      val loc = LatLng(it.location.lat, it.location.lng)
      val options = MarkerOptions()
          .title(it.name)
          .position(loc)
      val marker = map.addMarker(options);
      marker.tag = it
      marker.showInfoWindow()
      markers.add(marker)
    }
    moveToCurrentLocation()
  }

  fun doMarkerSelected(marker: Marker) {
    val site = marker.tag as? Site
    marker.showInfoWindow()
    doAsync {
      uiThread {
        if (site != null) view?.showSite(site)
      }
    }
  }

  fun loadSites() {
    doAsync {
      sites = app.sites.findAll()
      uiThread {
        view?.showSites(sites)
      }
    }
  }

  fun doEditSite(shownSite: Site) {
      view?.navigateTo(VIEW.EDIT_SITE, 0, "site_edit", shownSite)
  }

  fun doOnMapSearch(locationSearch: String?) {
    if(locationSearch == null) return

    val site = sites.findLast { it.name.contains(locationSearch) };
    val marker = markers.findLast { it.position == site?.location?.toLanLng() }
    if(marker != null && site != null) {
      moveMapTo(LatLng(site.location.lat, site.location.lng), map)
      doMarkerSelected(marker)
      return
    }
    val address: Address = mapSearch(locationSearch, view!!.baseContext) ?: return
    moveMapTo(LatLng(address.latitude, address.longitude), map)
  }

  @SuppressLint("MissingPermission")
  fun moveToCurrentLocation() {
    locationService.lastLocation.addOnSuccessListener {
      moveMapTo(LatLng(it.latitude, it.longitude), map)
    }
  }
}


