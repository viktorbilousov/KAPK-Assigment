package vib.oth.archaeological_fieldwork.views.profile.show

import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.avatar
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW
import kotlin.math.max

class ProfileView:  BaseView() {

  lateinit var presenter: ProfilePresenter
  lateinit var user : User


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)

    presenter = initPresenter(ProfilePresenter(this)) as ProfilePresenter
    user = presenter.user


    super.initBottomToolbar(bottomNavigationView, VIEW.PROFILE)

    btn_logout.setOnClickListener {
      presenter.signOut()
    }
    btn_remove_user.setOnClickListener {
      presenter.doRemoveUser()
    }
    btn_edit.setOnClickListener {
      presenter.doEdit()
    }

    showUser(user)

  }

   override fun showUser(user: User) {
     val defaultImage = R.mipmap.avatar
     textEmail.text = user.email
     textName.text = user.name

     val image = if (user.image == "") defaultImage else user.image
     Glide.with(this).load(image).into(avatar);

     totalMarked.text = user.favoriteSites.size.toString()
     textTotalVisited.text = user.visitedSites.size.toString()
     textTotalVoted.text = user.givenRating.size.toString()
     var average =user.givenRating.filter { it.value > 0 }.map { it.value }.average();
     if(average.isNaN()) average = 0.0;
     averageVote.text = "%.2f".format(average)
   }



}