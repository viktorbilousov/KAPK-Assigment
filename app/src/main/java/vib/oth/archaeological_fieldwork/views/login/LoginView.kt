package vib.oth.archaeological_fieldwork.views.login

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.textEditEmail
import kotlinx.android.synthetic.main.activity_login.textEditPassword
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_login.btnUpdate_profile
import kotlinx.android.synthetic.main.activity_login.toolbar
import org.jetbrains.anko.toast
import org.wit.placemark.views.login.LoginPresenter
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class LoginView : BaseView() {

  lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    init(toolbar, false)
    progressBar.visibility = View.GONE

    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    if(presenter.app.TEST) {
      textEditEmail.setText("test@test.com")
      textEditPassword.setText("12345678")
    }


    btnUpdate_profile.setOnClickListener {
      navigateTo(VIEW.REGISTER)
    }

    logIn.setOnClickListener {
      val email = textEditEmail.text.toString()
      val password = textEditPassword.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      }
      else {
        presenter.doLogin(email,password)
      }
    }
  }

  override fun showProgress() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar.visibility = View.GONE
  }
}
