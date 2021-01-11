package vib.oth.archaeological_fieldwork.views.siteslist

import android.view.View
import android.widget.CheckBox
import kotlinx.android.synthetic.main.activity_site_view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import vib.oth.archaeological_fieldwork.helpers.checkLocationPermissions
import vib.oth.archaeological_fieldwork.models.Rating
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW
import kotlin.random.Random

class SitesListPresenter(private val siteView: BaseView) : BasePresenter(siteView)  {

  var isFavorite: Boolean = false;

  init {
      isFavorite = siteView.intent.hasExtra("isFavorite")
  }


  fun loadSites(sites: List<Site>) {
    doAsync {
      uiThread {
        siteView.showSites(sites)
      }
    }
  }


  fun loadSites() {
    doAsync {
       loadSites(getSites())
    }
  }


  fun doEditSite(site: Site) {
    view?.navigateTo(VIEW.EDIT_SITE, 0, "site_edit", site)
  }

  fun doOnFavoriteChanged(site: Site, checkbox: CheckBox) {
    val isFavorite = checkbox.isChecked;
    if(isFavorite)   app.currentUser.addFavoriteSite(site)
    else app.currentUser.favoriteSites.remove(site.id)
    app.users.update(app.currentUser)
  }

  fun doOnVisitedChanged(site: Site, checkbox: CheckBox) {
    checkbox.isChecked = !checkbox.isChecked
    //disable !
  }


  fun doOnAddClick(view: BaseView) {
    view.navigateTo(VIEW.EDIT_SITE)
  }
  fun getSites(): List<Site>{
    var res = app.sites.findAll()
    if(isFavorite) res = res.filter { app.currentUser.favoriteSites.contains(it.id) }
    return res;
  }

  fun filterBy(s: String) {
    doAsync {
      val list = getSites().filter { it.name.contains(s) };
      loadSites(list)
    }
  }
}