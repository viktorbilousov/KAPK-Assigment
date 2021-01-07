package vib.oth.archaeological_fieldwork.views

import android.content.Intent
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import vib.oth.archaeological_fieldwork.views.login.LoginView
import vib.oth.archaeological_fieldwork.views.singup.SingUpView
import vib.oth.archaeological_fieldwork.views.siteslist.SitesListPresenter
import vib.oth.archaeological_fieldwork.views.siteslist.SitesListView


val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
  LOCATION, PLACEMARK, MAPS, LIST, LOGIN, REGISTER
}

abstract class BaseView() : AppCompatActivity(), AnkoLogger {

  var basePresenter: BasePresenter? = null

  fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
    var intent = Intent(this, LoginView::class.java)
    when (view) {
//      VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
//      VIEW.PLACEMARK -> intent = Intent(this, PlacemarkView::class.java)
//      VIEW.MAPS -> intent = Intent(this, PlacemarkMapView::class.java)
      VIEW.LIST -> intent = Intent(this, SitesListView::class.java)
      VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
      VIEW.REGISTER -> intent = Intent(this, SingUpView::class.java)
    }
    // ?
    if (key != "") {
      intent.putExtra(key, value)
    }
    startActivityForResult(intent, code)
  }


  fun init(toolbar: Toolbar, upEnabled: Boolean) {
    toolbar.title = title
    //https://developer.android.com/reference/android/app/ActionBar
    //Set whether home should be displayed as an "up" affordance.
    setSupportActionBar(toolbar)

    toolbar.setNavigationOnClickListener{
      info("navigation on click event")
      onBackPressed()
    }

    // todo - fix it
    supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)

    toolbar.setNavigationOnClickListener{
      info("navigation on click event")
      onBackPressed()
    }

//    val user = FirebaseAuth.getInstance().currentUser
//    if (user != null) {
//      toolbar.title = "${title}: ${user.email}"
//    }
  }

  fun initPresenter(presenter: BasePresenter): BasePresenter {
    basePresenter = presenter
    return presenter
  }

  fun init(toolbar: Toolbar) {
    toolbar.title = title
    setSupportActionBar(toolbar)
  }

  override fun onDestroy() {
    basePresenter?.onDestroy()
    super.onDestroy()
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      basePresenter?.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
  }

//  open fun showPlacemark(placemark: PlacemarkModel) {}
//  open fun showPlacemarks(placemarks: List<PlacemarkModel>) {}
//  open fun showLocation(location : Location) {}
  open fun showProgress() {}
  open fun hideProgress() {}
}