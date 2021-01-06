package vib.oth.archaeological_fieldwork.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
data class User(
    var id: Long = 0,
    var fbId : String = "",
    var name: String = "",
    var email: String = "",
    var image: String = "",
    var gender : Gender = Gender.MAN,

    // Long -> id of SiteModel
    var visitedSites : MutableList<Long> = mutableListOf(),
    var favoriteSites : MutableList<Long> = mutableListOf(),
    var givenRating : MutableMap<Long, Int> = mutableMapOf(),
    var notes       : MutableMap<Long, String> = mutableMapOf()
): Parcelable

enum class Gender{
  MAN, GIRL
}


