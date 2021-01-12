package vib.oth.archaeological_fieldwork.views.profile.edit.password

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.*
import vib.oth.archaeological_fieldwork.helpers.showImagePicker
import vib.oth.archaeological_fieldwork.models.Gender
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.IMAGE_REQUEST
import vib.oth.archaeological_fieldwork.views.VIEW
import java.lang.Exception

class EditPasswordPresenter(val editView: EditPasswordView) : BasePresenter(editView), AnkoLogger {


  lateinit var user : User

  init {
    if (editView.intent.hasExtra("current_user")) {
      user = editView.intent.extras?.getParcelable("current_user")!!
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

  fun doChangePassword(password: String, newPassword: String, repeatNewPassword: String, onSuccess: () -> Unit) {
    view?.showProgress()
    if (newPassword != repeatNewPassword) {
      view?.toast("New passwords not equals!")
      return
    }
    try {
      val authUser = FirebaseAuth.getInstance().currentUser!!
      FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, password)
        .addOnSuccessListener {
          authUser.updatePassword(newPassword)
          onSuccess()
        }.addOnFailureListener {
          info(it.message)
          view?.toast(it.message.toString())
          view?.hideProgress()
        }
    } catch (e: Exception) {
      error(e)
      view?.toast(e.message.toString())
      view?.hideProgress()
    }
  }

  fun goBack() {
    view?.navigateTo(VIEW.EDIT_PROFILE, 0 , "current_user", user)
  }

}