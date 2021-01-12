package vib.oth.archaeological_fieldwork.views.site

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_site_view.*
import org.jetbrains.anko.AnkoLogger
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.models.Rating
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW


class SiteView : BaseView(), AnkoLogger, ImageClickListener {

    lateinit var presenter: SitePresenter
    lateinit var site: Site;
    lateinit var map: GoogleMap
    lateinit var imageAdapter: SiteImageAdapter
    lateinit var stars: Array<CheckBox>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_view)
//        super.init(toolbarAdd, true);
        super.initBottomToolbar(bottomNavigationView, VIEW.EDIT_SITE);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        presenter = initPresenter(SitePresenter(this)) as SitePresenter
        site = presenter.site
        imageAdapter = SiteImageAdapter(imageLayout, this, this)

       stars = arrayOf(star1, star2, star3, star4, star5)
       stars.forEach { checkbox ->
           checkbox.setOnClickListener {
                presenter.doOnClickStar(checkbox);
            }
        }
        isVisited.setOnClickListener {
            presenter.doOnClickSetVisited(it as CheckBox)
        }


        val view : View = scrollView2
        view.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY-oldScrollY > 10 && btnSave.visibility == View.VISIBLE) {
                btnSave.hide();
            } else if (scrollY-oldScrollY < 0 && btnSave.visibility != View.VISIBLE) {
                btnSave.show();
            }
        }

        btnSave.setOnClickListener {
            cashe()
            presenter.updateSiteRating()
            presenter.doAddOrSave()
        }
        btn_delete.setOnClickListener {
            presenter.doDelete();
        }

        checkBoxIsFavorite.setOnClickListener {
            presenter.doSetFavorite((it as CheckBox).isChecked)
        }

        showSite(site)
    }

    override fun showSite(site: Site) {

        if(site.name.isNotEmpty()) textName.setText(site.name)
        if(site.description.isNotEmpty()) textEditName.setText(site.description)

        textEditName.setText(site.description)
        textRating.text = site.raiting.toString(true)


        if(site.description.isNotEmpty()) textEditName.setText(site.description)


        this.presenter.setRating(Rating.Companion.Rate.parse(presenter.user.getRating(site) ?: 0)!!, true)
        this.showLocation(site.location)
        this.showImages(site, true)
        showUserInfo(presenter.app.currentUser)

    }

    fun cashe(){
        presenter.cacheSite(textName.text.toString() , textEditName.text.toString())
        presenter.cacheUser(textNotes.text.toString())
    }


    fun showUserInfo(user: User){
        showStars(user.getRating(site) ?: 0)
        isVisited.isChecked = user.visitedSites.contains(site.id)
        if(user.getNote(site) != null) textNotes.text = user.getNote(site)
    }

    fun showStars(number: Int){
        stars.forEachIndexed { i, checkBox ->
            checkBox.isChecked = i <= number-1
        }
    }

    override fun showLocation(loc: Location) {
        text_loc_lat.text = ("lat: " +  "%.6f".format(loc.lat))
        text_loc_lng.text = ("lng: " + "%.6f".format(loc.lng))
    }

    fun showImages(site: Site, isEditable: Boolean = false) {
        imageAdapter.passImages(site, isEditable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showSite(site)
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onImageClick(view: View, image: String) {
        presenter.onImageClick(view, image)
    }
    override fun onAddImage(view: View) {
        presenter.onAddImage(view)
    }

    override fun onDelete(view: View, image: String, index: Int) {
        presenter.onDeleteImage(image);
        showImages(site, true)
    }

    override fun onSetHead(view: View, image: String, index: Int) {
        presenter.onSetHeadImage(image, index);
        showImages(site, true)
    }


}