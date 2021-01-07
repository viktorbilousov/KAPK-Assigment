package vib.oth.archaeological_fieldwork.views.login

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_login.register
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
      email.setText("test@test.com")
      password.setText("12345678")
    }


    register.setOnClickListener {
      navigateTo(VIEW.REGISTER)
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
