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
  lateinit var currentUser: User


  final val TEST : Boolean = true;

  fun setUser(user: User){
    info("Set current user $user")
    currentUser = user
  }

  fun filterNotExistedSites(user: User, sites : List<Site>) {
    val idsString = sites.map { it.id.toString() }
    val idsLong = sites.map { it.id }
    user.favoriteSites = user.favoriteSites.filter { idsLong.contains(it) }.toMutableList()
    user.givenRating = user.givenRating.filter { idsString.contains(it.key) }.toMutableMap()
    user.notes = user.notes.filter { idsString.contains(it.key) }.toMutableMap()
  }


  override fun onCreate() {
    super.onCreate()
    sites = SitesFireStore(applicationContext)
    users = UsersFireStore(applicationContext)

    info("App started")
//    setContentView(R.layout.activity_main)
  }
}