package vib.oth.archaeological_fieldwork.views.siteslist

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_site_list.*
import org.jetbrains.anko.info
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW
import java.util.*

class FavoritesSitesListView : BaseView(), SiteListener  {

    lateinit var presenter: SitesListPresenter
    lateinit var currentUser: User



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_site_list)
//        setSupportActionBar(toolbar)
//        super.init(toolbar, false)
//
        presenter = initPresenter(SitesListPresenter(this)) as SitesListPresenter
        currentUser = presenter.app.currentUser;

        initBottomToolbar(bottomNavigationView, VIEW.FAVORITES)
        bottomNavigationView.selectedItemId = R.id.favorites
//
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        presenter.loadFavoriteSites(currentUser)

    }


    override fun showSites(sites: List<Site>) {
        recyclerView.adapter = SiteAdapter(sites,  currentUser, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onSiteCardClick(site: Site) {
        presenter.doEditSite(site)
    }

    override fun onFavoriteChanged(site: Site, checkBox: CheckBox) {
        presenter.doOnFavoriteChanged(site, checkBox)
        presenter.loadFavoriteSites(currentUser)
    }

    override fun onVisitedChanged(site: Site, checkBox: CheckBox) {
        presenter.doOnVisitedChanged(site, checkBox)
    }

    override fun onDetailsClick(site: Site) {
        presenter.doEditSite(site)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadFavoriteSites(currentUser)
        super.onActivityResult(requestCode, resultCode, data)
    }

}