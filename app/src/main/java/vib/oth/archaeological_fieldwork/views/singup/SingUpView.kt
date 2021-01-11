package vib.oth.archaeological_fieldwork.views.singup

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Gender
import vib.oth.archaeological_fieldwork.views.BaseView


class SingUpView : BaseView() {

  lateinit var presenter: SingUpPresenter
  val noAvatarImage = R.mipmap.avatar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    init(toolbar, true)
    progressBar.visibility = View.GONE

    presenter = initPresenter(SingUpPresenter(this)) as SingUpPresenter

    avatar.setImageResource(R.mipmap.avatar);

    avatar.setOnClickListener {
      presenter.doSelectImage()
    }

    if(presenter.app.TEST) {
      textEditEmail.setText("${java.util.Random().nextInt(100)}test@test.com")
      textEditName.setText("test@test.com")
      textEditPassword.setText("12345678")
    }

    register.setOnClickListener {
      val email = textEditEmail.text.toString()
      val password = textEditPassword.text.toString();
      val name = textEditName.text.toString();

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


    btnRemoveAvatar.setOnClickListener {
      presenter.removeAvatar()
      Glide.with(this).load(noAvatarImage).into(avatar);
      btnRemoveAvatar.hide()
    }

    btnRemoveAvatar.hide()

  }

  override fun showProgress() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar.visibility = View.GONE
  }

  fun updateImage(){
        Glide.with(this).load(presenter.newUser.image).into(avatar);
        btnRemoveAvatar.show()
  }

}
