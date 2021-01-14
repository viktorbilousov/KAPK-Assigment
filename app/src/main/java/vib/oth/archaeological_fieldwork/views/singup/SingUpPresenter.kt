package vib.oth.archaeological_fieldwork.views.singup

import android.content.Intent
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.view.*
import org.jetbrains.anko.toast
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.helpers.showImagePicker
import vib.oth.archaeological_fieldwork.models.Gender
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.store.firebase.SitesFireStore
import vib.oth.archaeological_fieldwork.store.firebase.UsersFireStore
import vib.oth.archaeological_fieldwork.views.BasePresenter
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.IMAGE_REQUEST
import vib.oth.archaeological_fieldwork.views.VIEW

class SingUpPresenter(view: BaseView) : BasePresenter(view) {

  var auth: FirebaseAuth = FirebaseAuth.getInstance()
  var userStore: UsersFireStore? = null
  var sitesStore: SitesFireStore? = null
  val newUser : User = User();
  val singUpView: SingUpView

  init {
    if (app.sites is SitesFireStore) {
      userStore = app.users as UsersFireStore
      sitesStore = app.sites as SitesFireStore
    }
    singUpView = view as SingUpView
  }


  fun doRegisterAndSingUp(name: String, email: String, password: String){
    view?.showProgress()
    newUser.email = email
    newUser.name = name
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(view!!) { task ->
          if (task.isSuccessful) {
            if (sitesStore != null) {
              sitesStore!!.fetch {
                view?.hideProgress()
                if(userStore != null) {
                  userStore!!.fetch {
                    newUser.uid = auth.uid?: ""
                    userStore?.create(newUser);
                    app.setUser(userStore!!.findById(newUser.id)!!, EmailAuthProvider.getCredential(email, password))
                    view?.navigateTo(VIEW.LIST)
                  }
                }else {
                  view?.toast("Sign Up Failed: cannt fetch user")
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

  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        newUser.image = data.data.toString()
        singUpView.updateImage()
      }
    }
  }

  fun setGender(gender: Gender) {
    newUser.gender = gender;
  }

  fun removeAvatar() {
    newUser.image = ""
  }
}