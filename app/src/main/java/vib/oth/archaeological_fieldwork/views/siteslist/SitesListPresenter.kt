package vib.oth.archaeological_fieldwork.views.siteslist

import android.view.View
import android.widget.CheckBox
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class SitesListPresenter(private val siteView: BaseView) : BasePresenter(siteView)  {

  fun loadSites() {
    doAsync {
      val sites = app.sites.findAll()
      uiThread {
        siteView.showSites(sites)
      }
    }
  }

  fun loadFavoriteSites(user: User) {
    doAsync {
      val sites = app.sites.findAll().filter { user.favoriteSites.contains(it.id) }
      uiThread {
        siteView.showSites(sites)
      }
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

  fun doOnSearchClick(view: BaseView) {
    //todo: implement search
  }

  fun doOnAddClick(view: BaseView) {
    view.navigateTo(VIEW.EDIT_SITE)
  }
}