package vib.oth.archaeological_fieldwork.views.login

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
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

    register.setOnClickListener {

      navigateTo(VIEW.REGISTER)
//      val email = email.text.toString()
//      val password = password.text.toString()
//      if (email == "" || password == "") {
//        toast("Please provide email + password")
//      }
//      else {
//        presenter.doSignUp(email,password)
//      }
    }

    logIn.setOnClickListener {
      val email = email.text.toString()
      val password = password.text.toString()
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
