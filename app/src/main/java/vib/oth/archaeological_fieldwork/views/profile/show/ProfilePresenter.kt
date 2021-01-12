package vib.oth.archaeological_fieldwork.views.profile.show

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class ProfilePresenter(view: BaseView) : BasePresenter(view), AnkoLogger {

  val user : User

  init {
    if (view.intent.hasExtra("current_user")) {
      user = view.intent.extras?.getParcelable("current_user")!!
//            view.showSite(site)
    }else{
      error("Error, user not found!")
      signOut()
     // doLogout()
    }
  }

  fun signOut() {
      FirebaseAuth.getInstance().signOut()
      doAsync {
        app.sites.clear()
        app.users.clear()
      }
      view?.navigateTo(VIEW.LOGIN)
  }

  fun doRemoveUser() {
    doAsync {
      app.users.delete(user)
      FirebaseAuth.getInstance().currentUser?.delete()
    }
    signOut()
  }

  fun doEdit() {
    view?.navigateTo(VIEW.EDIT_PROFILE, 0 , "current_user", user)
  }

}