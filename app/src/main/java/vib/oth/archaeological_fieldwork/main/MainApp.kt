package vib.oth.archaeological_fieldwork.main

import android.app.Application
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
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
  lateinit var credential : AuthCredential


  final val TEST : Boolean = false;

  fun setUser(user: User, credential : AuthCredential ){
    info("Set current user $user")
    currentUser = user
    this.credential = credential;
  }

  fun filterNotExistedSites(user: User, sites: List<Site>) {
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