package vib.oth.archaeological_fieldwork.views.profile.show

import android.app.AlertDialog
import android.os.Bundle
import android.view.Window
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.popup_enter_password.view.*
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class ProfileView:  BaseView() {

  lateinit var presenter: ProfilePresenter
  lateinit var user : User
  lateinit var passwordDialog: AlertDialog

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)

    presenter = initPresenter(ProfilePresenter(this)) as ProfilePresenter
    user = presenter.user


    super.initBottomToolbar(bottomNavigationView, VIEW.PROFILE)


    val alert = AlertDialog.Builder(this, R.style.AppTheme)

    val layoutInflater = layoutInflater
    val enterPasswordView = layoutInflater.inflate(R.layout.popup_enter_password, null)

    alert.setView(enterPasswordView)
    alert.setCancelable(false)

    passwordDialog = alert.create();
    passwordDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)


    enterPasswordView.btn_cancelDialog.setOnClickListener{
      enterPasswordView.editTextPassword.text.clear()
      hideEnterPasswordDialog()
    }

    enterPasswordView.btn_removeAccountDialog.setOnClickListener {
      presenter.doRemoveUser( enterPasswordView.editTextPassword.text.toString())
//      hideEnterPasswordDialog()
    }

    btn_logout.setOnClickListener {
      presenter.signOut()
    }
    btn_remove_user.setOnClickListener {
//      presenter.doRemoveUser()
      showEnterPasswordDialog()
    }
    btn_edit.setOnClickListener {
      presenter.doEdit()
    }

    showUser(user)
//    showEnterPasswordDialog()

  }

   override fun showUser(user: User) {
     val defaultImage = R.mipmap.avatar
     textEmail.text = user.email
     textName.text = user.name

     val image = if (user.image == "") defaultImage else user.image
     Glide.with(this).load(image).into(avatar);

     totalMarked.text = user.favoriteSites.size.toString()
     textTotalVisited.text = user.visitedSites.size.toString()
     textTotalVoted.text = user.givenRating.filter { it.value > 0 }.size.toString()
     var average =user.givenRating.filter { it.value > 0 }.map { it.value }.average();
     if(average.isNaN()) average = 0.0;
     averageVote.text = "%.2f".format(average)
   }

  fun showEnterPasswordDialog(){
    passwordDialog.show()
  }

  fun hideEnterPasswordDialog(){
    passwordDialog.hide()
  }


}