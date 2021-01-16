package vib.oth.archaeological_fieldwork.views.profile.edit.profile

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile_edit.*
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Gender
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class ProfileEditView :  BaseView() {

  lateinit var presenter: ProfileEditPresenter
  lateinit var user : User
  val defaultImage = R.mipmap.avatar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile_edit)

    presenter = initPresenter(ProfileEditPresenter(this)) as ProfileEditPresenter
    user = presenter.user

    initBottomToolbar(bottomNavigationView, VIEW.EDIT_PROFILE)

    if(user.gender == Gender.MAN){
      toggle.check(gender_man.id)
    }else{
      toggle.check(gender_girl.id)
    }

    gender_girl.setOnClickListener {
      presenter.setGender(Gender.GIRL);
    }
    gender_man.setOnClickListener {
      presenter.setGender(Gender.MAN);
    }


    btnRemoveAvatar.setOnClickListener {
      presenter.removeAvatar()
      updateImage()
    }

    btnUpdate_profile.setOnClickListener {
      cacheUser()
      presenter.doEdit()
    }

    btn_ChangePassword.setOnClickListener {
      presenter.doChangePassword()
    }

    avatar.setOnClickListener {
      presenter.doSelectImage()
    }

    hideProgress()
    showUser(user)

  }

  override fun showUser(user: User) {
    textEditEmail.setText(user.email)
    textEditName.setText(user.name)
    updateImage()
  }


  fun updateImage(){
    if(user.image == "") {
      Glide.with(this).load(defaultImage).into(avatar);
      btnRemoveAvatar.hide()
    }else {
      Glide.with(this).load(user.image).into(avatar);
      btnRemoveAvatar.show()
    }
  }

  fun cacheUser(){
    user.name = textEditName.text.toString()
    val newEmail = textEditEmail.text.toString().trim()
    user.email = newEmail
  }

  override fun showProgress() {
    progressBar_profile.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar_profile.visibility = View.GONE
  }

}