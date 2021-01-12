package vib.oth.archaeological_fieldwork.views.start

import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class StartPresenter(siteView: BaseView) : BasePresenter(siteView) {
  fun goToByTimer(sec: Int, view: VIEW) {
    Timer("SettingUp", false).schedule(TimeUnit.SECONDS.toMillis(sec.toLong())) {
       super.view?.navigateTo(view);
    }
  }

}