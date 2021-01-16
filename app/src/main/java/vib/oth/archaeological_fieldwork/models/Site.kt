package vib.oth.archaeological_fieldwork.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize
import java.lang.StringBuilder


@Parcelize
@Entity
data class Site(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var fbId : String = "",
    var name: String = "",
    var description: String = "",
    var images: MutableList<String> = mutableListOf(),
    var headImageIndex: Int = -1,
    @Embedded var raiting : Rating = Rating(),
    @Embedded var location : Location = Location(),
): Parcelable {

    companion object{
        public const val MAX_IMAGES=4;
    }
    init {
        while(images.size > 4) images.removeAt(4)
    }



    @get:Exclude
    val imagesIterator : Iterator<String>
        get() = images.iterator();


    fun setHeadImage(imagePath: String): Boolean {
       if(!images.contains(imagePath)) return false;
        headImageIndex = images.indexOf(imagePath);
        return true
    };



    val canAddImage : Boolean
        get() = imagesCnt() < MAX_IMAGES;


    fun imagesCnt() : Int{
        return images.size
    }

    fun getImage(index: Int) : String{
        return images[index];
    }
    fun addImage(imagePath: String, isHead: Boolean = false): Boolean{
        if(images.contains(imagePath)) return false
        if(!canAddImage) return false
        val cnt = imagesCnt();
        if(cnt == 0 || isHead) headImageIndex = cnt;
        images.add(imagePath)
        return true
    }

    fun removeImageAt(index: Int) : Boolean{
        val lastIndex = imagesCnt() -1;
        if(index < 0 || index > lastIndex) return false

        images.removeAt(index)

        if(headImageIndex > index) headImageIndex --;
        else if(headImageIndex == index) {
            if(imagesCnt() > 0) headImageIndex = 0
            else headImageIndex = -1;
        };
        return true
    }

    fun removeImage(image: String) : Boolean{
        return removeImageAt(images.indexOf(image))
    }

    fun getHeadImage() : String? {
        if(headImageIndex < 0) return null
        if(images.size == 0 ){
            headImageIndex = -1;
            return null
        }
        return images[headImageIndex]
    }

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
                    var zoom: Float = 0f) : Parcelable{
    constructor(loc: Location) : this(loc.lat, loc.lng, loc.zoom)
    fun toLanLng() : LatLng = LatLng(lat, lng)

}

@Parcelize
data class Rating(var raiting: Double = 0.0, var totalGoals : Int = 0) : Parcelable{

    fun vote(rate: Rate){
        if(rate == Rate.ZERO) return
        val number = rate.number;
        raiting = (raiting * totalGoals + number) / (totalGoals + 1)
        totalGoals++
    }

    fun deleteVote(rate: Rate){
        if(rate == Rate.ZERO) return
        if(totalGoals == 1) {
            totalGoals = 0;
            raiting = 0.0;
            return;
        }
        raiting =  (raiting * totalGoals - rate.number) / (totalGoals - 1 )
        totalGoals--;
    }


    fun toString(printTotal: Boolean): String{
        if(totalGoals == 0) return "no votes"
        val sb = StringBuilder();
        sb.append("%.1f".format(raiting))
        sb.append(" / 5.0")
        if(printTotal) {
            sb.append( " ($totalGoals)")
        }
        return sb.toString()
    }

    override fun toString(): String {
        return toString(false)
    }

    companion object{
        enum class Rate(val number : Int){
            ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);
            companion object {
                fun parse(number: Int): Rate? {
                    return values().findLast { it.number == number }
                }
            }
        }
    }
}

