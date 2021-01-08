package vib.oth.archaeological_fieldwork.views.singup

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Gender
import vib.oth.archaeological_fieldwork.views.BaseView


class SingUpView : BaseView() {

  lateinit var presenter: SingUpPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    init(toolbar, true)
    progressBar.visibility = View.GONE

    presenter = initPresenter(SingUpPresenter(this)) as SingUpPresenter

    avatar.setImageResource(R.mipmap.avatar);

    avatar.setOnClickListener {
      presenter.doSelectImage()
      info("end select")
    }

    if(presenter.app.TEST) {
      email.setText("${java.util.Random().nextInt(100)}test@test.com")
      textDescription.setText("test@test.com")
      password.setText("12345678")
    }

    register.setOnClickListener {
      val email = email.text.toString()
      val password = password.text.toString();
      val name = textDescription.text.toString();

      if(name == "") toast("Please enter your name")
      else if(email == "") toast("Please enter email")
      else if(password == "") toast("Please enter password")
      else{
        presenter.doRegisterAndSingUp(name, email, password)
      }
    }

    toggle.check(gender_man.id)

    gender_girl.setOnClickListener {
      presenter.setGender(Gender.GIRL);
    }
    gender_man.setOnClickListener {
      presenter.setGender(Gender.MAN);
    }



  }

  override fun showProgress() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar.visibility = View.GONE
  }

  fun updateImage(){
        Glide.with(this).load(presenter.newUser.image).into(avatar);
  }

}
