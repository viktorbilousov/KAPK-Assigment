package vib.oth.archaeological_fieldwork.views.location

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_edit_location.*
import kotlinx.android.synthetic.main.activity_edit_location.view.*
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.views.BaseView

class EditLocationView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

  lateinit var presenter: EditLocationPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_location)
    super.init(toolbar, true)

    presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter

    mapView.onCreate(savedInstanceState);

    mapView.getMapAsync {
      it.setOnMarkerDragListener(this)
      it.setOnMarkerClickListener(this)
      it.setOnMapLongClickListener(this)
      presenter.doConfigureMap(it)
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


//    search_location.setOnClickListener {
//      presenter.doOnMapSearch(it, editText.text.toString())
//    }
  }

  override fun showLocation(location: Location) {
//    lat.setText("%.6f".format(location.lat))
//    lng.setText("%.6f".format(location.lng))
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_edit_location, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.item_save -> {
        presenter.doSave()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onMarkerDragStart(marker: Marker) {}

  override fun onMarkerDrag(marker: Marker) {
    presenter.doUpdateMarker(marker)
  }

  override fun onMarkerDragEnd(marker: Marker) {
    presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
  }

  override fun onMapLongClick(p0: LatLng?) {
    if(p0 == null) return
    presenter.setMarker(p0);
    presenter.doUpdateLocation(p0.latitude, p0.longitude)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doUpdateMarker(marker)
    return false
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
}