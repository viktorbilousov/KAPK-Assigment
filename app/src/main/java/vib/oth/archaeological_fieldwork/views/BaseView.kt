package vib.oth.archaeological_fieldwork.views

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_site_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import vib.oth.archaeological_fieldwork.views.location.EditLocationView
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.views.login.LoginView
import vib.oth.archaeological_fieldwork.views.map.MapView
import vib.oth.archaeological_fieldwork.views.singup.SingUpView
import vib.oth.archaeological_fieldwork.views.site.SiteView
import vib.oth.archaeological_fieldwork.views.siteslist.SitesListView


val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
  MAP,  LIST, LOGIN, REGISTER, FAVORITES, PROFILE, LOCATION, EDIT_SITE, EDIT_LOCATION
}

abstract class BaseView() : AppCompatActivity(), AnkoLogger {

  var basePresenter: BasePresenter? = null

  fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
    var intent = Intent(this, LoginView::class.java)
    info("navigate to ${view.name}")
    when (view) {
//      VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
//      VIEW.PLACEMARK -> intent = Intent(this, PlacemarkView::class.java)
      VIEW.MAP -> intent = Intent(this, MapView::class.java)
      VIEW.LIST -> intent = Intent(this, SitesListView::class.java)
      VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
      VIEW.REGISTER -> intent = Intent(this, SingUpView::class.java)
      VIEW.EDIT_SITE -> intent = Intent(this, SiteView::class.java)
      VIEW.EDIT_LOCATION -> intent = Intent(this, EditLocationView::class.java)

    }
    // ?
    if (key != "") {
      intent.putExtra(key, value)
    }
    startActivityForResult(intent, code)
    overridePendingTransition(0, 0) // disable animation
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

  fun initBottomToolbar(bottomNavigationView: BottomNavigationView, currentView: VIEW){
    bottomNavigationView.setOnNavigationItemSelectedListener {
      when(it.toString()){
        getString(R.string.bottom_nav_discover) ->  if(currentView != VIEW.LIST) navigateTo(VIEW.LIST)
        getString(R.string.bottom_nav_map) ->       if(currentView != VIEW.MAP) navigateTo(VIEW.MAP)
        getString(R.string.bottom_nav_profile) ->   if(currentView != VIEW.PROFILE) navigateTo(VIEW.PROFILE)
        getString(R.string.bottom_nav_favorites) -> if(currentView != VIEW.FAVORITES)navigateTo(VIEW.LIST, 0, "isFavorite", null)
        else -> return@setOnNavigationItemSelectedListener false
      }
      return@setOnNavigationItemSelectedListener true

    }
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


  open fun showLocation(location : Location) {}
  open fun showProgress() {}
  open fun hideProgress() {}
  open fun showSites(sites: List<Site>){}
  open fun showSite(site: Site){}
}