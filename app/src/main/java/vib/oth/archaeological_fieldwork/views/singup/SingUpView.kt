package vib.oth.archaeological_fieldwork.views.singup

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import org.wit.placemark.views.login.LoginPresenter
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import kotlin.random.Random

class SingUpView : BaseView() {

  lateinit var presenter: SingUpPresenter

  val user : User = User();


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    init(toolbar, true)
    progressBar.visibility = View.GONE

    presenter = initPresenter(SingUpPresenter(this)) as SingUpPresenter

    addUserImage.setOnClickListener {
        presenter.doSelectImage()
    }
    email.setText( "${java.util.Random().nextInt(100)}test@test.com")
    name.setText("test@test.com")
    password.setText("12345678")


    register.setOnClickListener {
      val email = email.text.toString()
      val password = password.text.toString();
      val name = name.text.toString();

      if(name == "") toast("Please enter your name")
      else if(email == "") toast("Please enter email")
      else if(password == "") toast("Please enter password")
      else{
        user.name = name;
        user.email = email
        presenter.doRegisterAndSingUp(user, password)
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
