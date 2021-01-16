package vib.oth.archaeological_fieldwork.views.profile.edit.profile

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import vib.oth.archaeological_fieldwork.helpers.showImagePicker
import vib.oth.archaeological_fieldwork.models.Gender
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.IMAGE_REQUEST
import vib.oth.archaeological_fieldwork.views.VIEW

class ProfileEditPresenter(val editView: ProfileEditView) : BasePresenter(editView), AnkoLogger {


  val user : User


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

  fun setGender(gender: Gender) {
    user.gender = gender
  }

  fun removeAvatar() {
    user.image = ""
  }

  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        user.image = data.data.toString()
        editView.updateImage()
//        editView.updateImage()
      }
    }
  }

  fun doEdit() {
    view?.showProgress()
    doAsync {
      uiThread {
        updateEmail()
        app.users.update(user)
        app.currentUser = app.users.findById(user.id)!!
        view?.hideProgress()
        view?.navigateTo(VIEW.PROFILE, 0, "current_user", user)
      }
    }
  }

  fun doChangePassword() {
    view?.navigateTo(VIEW.EDIT_PASSWORD, 0, "current_user", user)
  }

  fun updateEmail(){
      FirebaseAuth.getInstance().currentUser?.updateEmail(user.email)
  }
}