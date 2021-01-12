package org.wit.placemark.views.login

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.toast
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.store.firebase.SitesFireStore
import vib.oth.archaeological_fieldwork.store.firebase.UsersFireStore
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class LoginPresenter(view: BaseView) : BasePresenter(view), AnkoLogger {

  var auth: FirebaseAuth = FirebaseAuth.getInstance()
  var sitesStore: SitesFireStore? = null
  var usersStore: UsersFireStore? = null

  init {
    sitesStore = app.sites as SitesFireStore
    usersStore = app.users as UsersFireStore
  }

  fun doLogin(email: String, password: String) {
    view?.showProgress()
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        if (sitesStore != null) {
          sitesStore!!.fetch {

            if (usersStore != null) {

              usersStore!!.fetch {
                val user = usersStore!!.findAll().findLast { it.uid == auth.uid }
                if(user == null){
                  view?.toast("User not found!")
                  error("user not found!")
                  view?.hideProgress()
                  return@fetch
                }
                app.setUser(user)
                app.filterNotExistedSites(user, sitesStore!!.findAll())
                view?.hideProgress()
                view?.navigateTo(VIEW.LIST)
              }

            } else {
              view?.toast("Login Failed: cannt fetch user")
            }
          }
        } else {
          view?.hideProgress()
//          view?.navigateTo(VIEW.LIST)
          view?.navigateTo(VIEW.LIST)
        }
      } else {
        view?.hideProgress()
        view?.toast("Sign Up Failed: ${task.exception?.message}")
      }
    }
  }

}