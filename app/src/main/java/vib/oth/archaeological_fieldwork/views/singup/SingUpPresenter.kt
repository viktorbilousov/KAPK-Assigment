package vib.oth.archaeological_fieldwork.views.singup

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast
import org.wit.placemark.helpers.showImagePicker
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

  init {
    if (app.sites is SitesFireStore) {
      userStore = app.users as UsersFireStore
      sitesStore = app.sites as SitesFireStore
    }
  }


  fun doRegisterAndSingUp(user: User, password: String){
    view?.showProgress()
    auth.createUserWithEmailAndPassword(user.email, password)
        .addOnCompleteListener(view!!) { task ->
          if (task.isSuccessful) {
            if (sitesStore != null) {
              sitesStore!!.fetch {
                view?.hideProgress()
                if(userStore != null) {
                  userStore!!.fetch {
                    userStore?.create(user);
                    view?.navigateTo(VIEW.LOGIN)
                  }
                }else {
                  view?.toast("Sign Up Failed: cannt fetch user")
                }
              }
            } else {
              view?.hideProgress()
//          view?.navigateTo(VIEW.LIST)
              view?.navigateTo(VIEW.LOGIN)
            }
          } else {
            view?.hideProgress()
            view?.toast("Sign Up Failed: ${task.exception?.message}")
          }
        }
  }

  fun doSignUp(email: String, password: String) {
    view?.showProgress()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(view!!) { task ->
          if (task.isSuccessful) {
            if (sitesStore != null) {
              sitesStore!!.fetch {
                view?.hideProgress()
//            view?.navigateTo(VIEW.LIST)\
                view?.navigateTo(VIEW.LOGIN)
              }
            } else {
              view?.hideProgress()
//          view?.navigateTo(VIEW.LIST)
              view?.navigateTo(VIEW.LOGIN)
            }
          } else {
            view?.hideProgress()
            view?.toast("Sign Up Failed: ${task.exception?.message}")
          }
        }
  }

  fun addNewUser(user: User) {
    if(userStore != null) {
      userStore!!.fetch {
        userStore?.create(user);
      }
    }else {
      view?.toast("Sign Up Failed: cannt fetch user")
    }
  }

  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }
}