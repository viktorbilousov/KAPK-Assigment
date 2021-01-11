package vib.oth.archaeological_fieldwork.views.map

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_map.bottomNavigationView
import kotlinx.android.synthetic.main.activity_map.mapView
import kotlinx.android.synthetic.main.activity_map.search_location
import kotlinx.android.synthetic.main.activity_map.star1
import kotlinx.android.synthetic.main.activity_map.star2
import kotlinx.android.synthetic.main.activity_map.star3
import kotlinx.android.synthetic.main.activity_map.star4
import kotlinx.android.synthetic.main.activity_map.star5
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class MapView : BaseView(), GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

  lateinit var presenter: MapPresenter
  lateinit var map : GoogleMap
  lateinit var shownSite : Site;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_map)
    super.initBottomToolbar(bottomNavigationView, VIEW.MAP)

    presenter = initPresenter (MapPresenter(this)) as MapPresenter

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      map = it
      map.setOnMarkerClickListener(this)
      map.setOnMapClickListener(this)
      presenter.loadSites()
    }

    cardView.setOnClickListener {
      presenter.doEditSite(shownSite)
    }

    search_location.setOnQueryTextListener(
        object : SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query: String?): Boolean {
            presenter.doOnMapSearch(query)
            return true
          }

          override fun onQueryTextChange(newText: String?): Boolean {
            return true
          }
        }
    )

    search_location.setOnClickListener{
      search_location.isIconified = false
    }

    setCardVisibility(false)
  }

  override fun showSite(site: Site) {
    setCardVisibility(true)
    shownSite = site;
    val rating = site.raiting.raiting.toInt()
    val isVisitedFlag = presenter.app.currentUser.visitedSites.contains(site.id)
    val image = site.getHeadImage() ?: R.mipmap.no_image

    textCurrentName.text = site.name
    showStars(rating)
    isVisited.isChecked = isVisitedFlag

    Glide.with(this).load(image).into(currentImage);
  }

  private fun showStars(number: Int){
   arrayOf(star1, star2, star3, star4, star5).forEachIndexed { i, checkBox ->
      checkBox.isChecked = i <= number-1
    }
  }

  fun setCardVisibility(isVisible: Boolean){
      if(isVisible)     cardView.visibility = View.VISIBLE
      else  cardView.visibility = View.GONE
  }

  override fun showSites(sites: List<Site>) {
    presenter.doPopulateMap(map, sites)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    setCardVisibility(true)
    presenter.doMarkerSelected(marker)
    return true
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }

  override fun onMapClick(p0: LatLng?) {
    //hide card if no marker selected
    if( presenter.sites.map { LatLng(it.location.lat, it.location.lng) }.findLast { it == p0 } == null) setCardVisibility(false)
  }
}

