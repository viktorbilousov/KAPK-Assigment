package vib.oth.archaeological_fieldwork.views.profile.show

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.*
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
      user = User()
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

  fun doRemoveUser(password: String) {

    if(password.isEmpty()){
      view?.toast("Empty password!")
      return
    }

    doAsync {
      val userAuth = FirebaseAuth.getInstance().currentUser!!
      userAuth.reauthenticate(EmailAuthProvider.getCredential(user.email, password))
          .addOnSuccessListener {
            userAuth.delete()
            app.users.delete(user)
            signOut()
          }.addOnFailureListener {
            view?.toast(it.message.toString())
            return@addOnFailureListener
          }
    }
  }

  fun doEdit() {
    view?.navigateTo(VIEW.EDIT_PROFILE, 0 , "current_user", user)
  }

}