package vib.oth.archaeological_fieldwork.views.profile.edit.password

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_change_password.*
import org.jetbrains.anko.toast
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class EditPasswordView :  BaseView() {

  lateinit var presenter: EditPasswordPresenter
  lateinit var user : User

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_change_password)

    presenter = initPresenter(EditPasswordPresenter(this)) as EditPasswordPresenter
    user = presenter.user

    initBottomToolbar(bottomNavigationView, VIEW.EDIT_PROFILE)

    btn_cancelDialog.setOnClickListener {

      presenter.doChangePassword(
        editTextPassword.text.toString(),
        editTextNewPassword.text.toString(),
        editTextNewPasswordRepeat.text.toString()
      ){
        toast("Password was successfully changed")
        presenter.goBack()
      }

      hideProgress()
      clearAll()
    }
    hideProgress()

  }

  fun clearAll(){
    editTextNewPassword.text.clear()
    editTextNewPasswordRepeat.text.clear()
    editTextPassword.text.clear()
  }

  override fun showProgress() {
    progressBar_profile2.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar_profile2.visibility = View.GONE
  }

}