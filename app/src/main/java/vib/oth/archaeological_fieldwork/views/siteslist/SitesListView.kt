package vib.oth.archaeological_fieldwork.views.siteslist

import android.os.Bundle
import android.view.View
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.views.BaseView

class SitesListView : BaseView()  {

    lateinit var presenter: SitesListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list)
        //init(toolbar, false)
        //progressBar.visibility = View.GONE

        presenter = initPresenter(SitesListPresenter(this)) as SitesListPresenter


    }
}