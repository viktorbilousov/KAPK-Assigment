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
): Parcelable{
  fun addVisitedSite(site: Site): Boolean{
    val id = site.id;
    if (!visitedSites.contains(id)) return visitedSites.add(id)
    return false
  }

  fun addFavoriteSite(site: Site) : Boolean{
    val id = site.id;
    if (!favoriteSites.contains(id)) return favoriteSites.add(id)
    return false
  }

}

enum class Gender{
  MAN, GIRL
}


