package vib.oth.archaeological_fieldwork.views.siteslist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_site_list.*
import org.jetbrains.anko.info
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW
import java.util.*

class SitesListView : BaseView(), SiteListener,  SearchListener   {

    lateinit var presenter: SitesListPresenter
    lateinit var currentUser: User
    var isSearched: Boolean = false;
//    var lastScrolledY : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list)
//        setSupportActionBar(toolbar)
//        super.init(toolbar, false)

        presenter = initPresenter(SitesListPresenter(this)) as SitesListPresenter
        currentUser = presenter.app.currentUser;



        if(presenter.isFavorite)         {
            initBottomToolbar(bottomNavigationView, VIEW.FAVORITES)
            bottomNavigationView.selectedItemId = R.id.favorites
        }
        else   {
            initBottomToolbar(bottomNavigationView, VIEW.LIST)
            bottomNavigationView.selectedItemId = R.id.discover
        }



        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        if(presenter.app.TEST && presenter.app.sites.findAll().isEmpty()){
            val db = presenter.app.sites
            db.create(Site(Random().nextLong(),name="test site1", description = "test site description1"))
            db.create(Site(Random().nextLong(),name="test site2", description = "test site description2"))
            db.create(Site(Random().nextLong(),name="test site3", description = "test site description3"))
        }
        presenter.loadSites()


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 10 && bntAdd.visibility == View.VISIBLE) {
                    bntAdd.hide();
                } else if (dy < 0 && bntAdd.visibility != View.VISIBLE) {
                    bntAdd.show();
                }
//                lastScrolledY  += dy;
//                if(lastScrolledY <0 )   lastScrolledY =  0
            }
        })

        bntAdd.setOnClickListener {
            presenter.doOnAddClick(this)
        }


    }


    override fun showSites(sites: List<Site>) {
        recyclerView.adapter = SiteAdapter(sites,  currentUser, this, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onSiteCardClick(site: Site) {
        presenter.doEditSite(site)
    }

    override fun onFavoriteChanged(site: Site, checkBox: CheckBox) {
        presenter.doOnFavoriteChanged(site, checkBox)
    }

    override fun onVisitedChanged(site: Site, checkBox: CheckBox) {
        presenter.doOnVisitedChanged(site, checkBox)
    }

    override fun onDetailsClick(site: Site) {
        presenter.doEditSite(site)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadSites()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCleanSearch() {
        isSearched = false
        presenter.loadSites()
    }

    override fun buttonState(): () -> Boolean = {
       isSearched
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query.isNullOrEmpty()) {
            presenter.loadSites()
        }else {
            presenter.filterBy(query.trim())
        }
        isSearched = !query.isNullOrEmpty()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}