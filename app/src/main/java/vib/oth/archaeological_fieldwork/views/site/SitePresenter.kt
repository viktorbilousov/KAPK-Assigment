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
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import vib.oth.archaeological_fieldwork.helpers.checkLocationPermissions
import vib.oth.archaeological_fieldwork.helpers.createDefaultLocationRequest
import vib.oth.archaeological_fieldwork.helpers.isPermissionGranted
import vib.oth.archaeological_fieldwork.helpers.showImagePicker
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.models.Rating
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.views.*
import kotlin.random.Random

class SitePresenter(view: SiteView) : BasePresenter(view), AnkoLogger {

    private var map: GoogleMap? = null
    val site: Site
    private var edit = false;
    private var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private val locationRequest = createDefaultLocationRequest()
    private var locationManualyChanged = false;
    private val defaultLocation = Location(.0, .0)
    private val starsList : List<CheckBox>
    private val siteView = view;

    init {
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras?.getParcelable("site_edit")!!
            view.showSite(site)
        } else {
            site = Site(Random.nextLong())
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }

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

    fun cache (title: String, description: String) {
//        placemark.title = title;
//        placemark.description = description
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
        doAsync {
            if (edit) {
                app.sites.update(site)
            } else {
                app.sites.create(site)
            }
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
        setRating(Rating.Companion.MARK.parse(index+1)!!)
        setIsVisited(true)
        siteView.showUserInfo(app.currentUser)
    }

    private fun setIsVisited(boolean: Boolean){
        val user = app.currentUser;
        if (boolean) user.addVisitedSite(site)
        else user.visitedSites.remove(site.id);
    }

    fun setRating(vote: Rating.Companion.MARK){
        app.currentUser.givenRating[site.id] = vote.number
    }

    fun doOnClickSetVisited(checkBox: CheckBox) {
        app.currentUser.givenRating.remove(site.id)
        setIsVisited(checkBox.isChecked)
        siteView.showUserInfo(app.currentUser)
    }

}
