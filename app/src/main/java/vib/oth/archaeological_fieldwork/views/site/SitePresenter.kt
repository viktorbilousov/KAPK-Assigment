package vib.oth.archaeological_fieldwork.views.site

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.CheckBox
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_site_view.*
import org.jetbrains.anko.*
import vib.oth.archaeological_fieldwork.helpers.checkLocationPermissions
import vib.oth.archaeological_fieldwork.helpers.createDefaultLocationRequest
import vib.oth.archaeological_fieldwork.helpers.isPermissionGranted
import vib.oth.archaeological_fieldwork.helpers.showImagePicker
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.models.Rating
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.*
import kotlin.random.Random

class SitePresenter(view: SiteView) : BasePresenter(view), AnkoLogger {

    private var map: GoogleMap? = null
    val site: Site
    val user : User
    private var edit = false
    private var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private val locationRequest = createDefaultLocationRequest()
    private var locationManualyChanged = false;
    private val defaultLocation = Location(.0, .0)
    private val starsList : List<CheckBox>
    private val siteView = view;
    private var oldUserVote: Rating.Companion.Rate

    init {
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras?.getParcelable("site_edit")!!
            oldUserVote = Rating.Companion.Rate.ZERO;
//            view.showSite(site)
        } else {
            site = Site(Random.nextLong())
            view.btn_delete.visibility = View.GONE
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            oldUserVote = Rating.Companion.Rate.parse(app.currentUser.getRating(site) ?: 0 )!!
        }

        user = app.currentUser
        starsList = listOf(view.star1, view.star2, view.star3, view.star4, view.star5 )

    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if (!locationManualyChanged) {
                        locationUpdate(Location(l.latitude, l.longitude))
                    }
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }


    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(site.location)
    }

    fun locationUpdate(location: Location) {
        site.location = location
        site.location.zoom = 15f
        map?.clear()
        val options = MarkerOptions().title(site.name).position(LatLng(site.location.lat, site.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(site.location.lat, site.location.lng), site.location.zoom))
        view?.showLocation(site.location)
    }

    fun doAddOrSave() {

        if(site.description.isEmpty() || site.name.isEmpty()) {
            view?.toast("Please enter Name + Description")
            return
        }

        doAsync {
            if (edit) {
                app.sites.update(site)
            } else {
                app.sites.create(site)
            }
            app.users.update(user)
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        doAsync {
            app.sites.delete(site)
            uiThread {
                view?.finish()
            }
        }
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        locationManualyChanged = true;
        view?.navigateTo(VIEW.EDIT_LOCATION, LOCATION_REQUEST, "location", Location(site.location))
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                val image = data.data.toString()
                site.addImage(image)
//                (view as SiteView).showSite(site)
                updateImages()
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                site.location = location
                locationUpdate(location)
                doResartLocationUpdates()
            }
        }
    }

    fun updateImages(){
        (view as SiteView).showImages(site, true)
    }

    fun onImageClick(view: View, image: String) {
        info("onImageClick: $image, $view")
    }

    fun onAddImage(view: View) {
        info("onAddImage: $view, $view")
        doSelectImage()
    }

    fun onDeleteImage(image: String) {
        site.removeImage(image);
    }

    fun onSetHeadImage(image: String, index: Int) {
        site.setHeadImage(image)
    }

    fun doOnClickStar(checkBox: CheckBox) {
        val index = starsList.indexOf(checkBox)
        setRating(Rating.Companion.Rate.parse(index+1)!!)
        setIsVisited(true)
        siteView.showUserInfo(user)
    }

    private fun setIsVisited(boolean: Boolean){
        if (boolean) user.addVisitedSite(site)
        else user.removeVisitedSite(site);
    }

    fun setRating(vote: Rating.Companion.Rate, isSource: Boolean = false){
        if(isSource){
            oldUserVote = vote
        }
        user.setUserRating(site, vote)
    }

    fun doOnClickSetVisited(checkBox: CheckBox) {
        user.removeUserRating(site)
        setIsVisited(checkBox.isChecked)
        siteView.showUserInfo(user)
    }

    fun doSetFavorite(checked: Boolean) {
        if(checked) user.addFavoriteSite(site)
        else user.removeFavoriteSite(site)
    }

    fun cacheUser(note: String) {
        user.setUserNote(site, note)
    }

    fun cacheSite(name: String, description: String) {
        site.name = name.trim();
        site.description = description.trim()
    }

    fun updateSiteRating(){
        val vote = user.getRating(site) ?: 0;
        val rate = Rating.Companion.Rate.parse(vote)!!
        site.raiting.deleteVote(oldUserVote)
        site.raiting.vote(rate)
        oldUserVote = rate;
    }

}
