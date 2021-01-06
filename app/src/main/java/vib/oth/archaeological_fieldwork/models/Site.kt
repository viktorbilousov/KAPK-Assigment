package vib.oth.archaeological_fieldwork.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
data class Site(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var fbId : String = "",
    var name: String = "",
    var description: String = "",
    var images: Array<String?> = arrayOfNulls(4),

    @Embedded var raiting : Rating = Rating(),
    @Embedded var location : Location = Location()
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Site

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

@Parcelize
data class Rating(var raiting: Double = 0.0, var totalGoals : Int = 0) : Parcelable{

    fun vote(mark: MARK){
        val number = mark.number;
        raiting = (raiting * totalGoals + raiting) / (totalGoals + 1)
    }

    companion object{
        enum class MARK(val number : Int){
            ZERO(1), ONE(2), TWO(4), THREE(3), FOUR(4), FIVE(5);
        }
    }
}

