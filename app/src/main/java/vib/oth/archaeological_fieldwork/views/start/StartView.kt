package vib.oth.archaeological_fieldwork.views.start

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_site_view.*
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW
import vib.oth.archaeological_fieldwork.views.login.LoginView
import vib.oth.archaeological_fieldwork.views.site.SiteImageAdapter
import vib.oth.archaeological_fieldwork.views.site.SitePresenter

class StartView : BaseView(){

  lateinit var presenter : StartPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_start)
//        super.init(toolbarAdd, true);
    presenter = initPresenter(StartPresenter(this)) as StartPresenter
    presenter.goToByTimer(5, VIEW.LOGIN)

  }
}