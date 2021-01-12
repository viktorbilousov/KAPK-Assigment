package vib.oth.archaeological_fieldwork.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
@Entity
data class User(
    var id: Long = Random().nextLong(),
    var uid: String = "",
    var fbId : String = "",
    var name: String = "",
    var email: String = "",
    var image: String = "",
    var gender : Gender = Gender.MAN,

    // Long -> id of SiteModel
    var visitedSites : MutableList<Long> = mutableListOf(),
    var favoriteSites : MutableList<Long> = mutableListOf(),
    var givenRating : MutableMap<String, Int> = mutableMapOf(),
    var notes       : MutableMap<String, String> = mutableMapOf()
): Parcelable{
  fun addVisitedSite(site: Site): Boolean{
    val id = site.id;
    if (!visitedSites.contains(id)) return visitedSites.add(id)
    return false
  }

  fun setUserRating(site: Site, rate: Rating.Companion.Rate){
    val num = rate.number;
    val id = site.id.toString()
    givenRating[id] = num;
  }

  fun removeUserRating(site: Site){
     givenRating.remove(site.id.toString())
  }

  fun setUserNote(site: Site, note: String){
    val id = site.id.toString()
    notes[id] = note;
  }

  fun removeUserNote(site: Site){
    notes.remove(site.id.toString())
  }

  fun addFavoriteSite(site: Site) : Boolean{
    val id = site.id;
    if (!favoriteSites.contains(id)) return favoriteSites.add(id)
    return false
  }

  fun getRating(site: Site) : Int? {
    return givenRating[site.id.toString()]
  }

  fun getNote(site: Site) : String?{
    return notes[site.id.toString()]
  }

  fun removeFavoriteSite(site: Site) {
    favoriteSites.remove(site.id)
  }
  fun removeVisitedSite(site: Site){
    visitedSites.remove(site.id)
  }

}

enum class Gender{
  MAN, GIRL
}


