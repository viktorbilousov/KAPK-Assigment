package vib.oth.archaeological_fieldwork.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.store.firebase.BaseStore
import vib.oth.archaeological_fieldwork.store.firebase.SitesFireStore
import vib.oth.archaeological_fieldwork.store.firebase.UsersFireStore

class MainApp : Application(), AnkoLogger {

  lateinit var sites: BaseStore<Site>
  lateinit var users: BaseStore<User>

  override fun onCreate() {
    super.onCreate()
    sites = SitesFireStore(applicationContext)
    users = UsersFireStore(applicationContext)
    info("App started")
//    setContentView(R.layout.activity_main)
  }
}