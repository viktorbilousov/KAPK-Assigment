package vib.oth.archaeological_fieldwork.views.site

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_site_view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Location
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.views.BaseView
import vib.oth.archaeological_fieldwork.views.VIEW

class SiteView : BaseView(), AnkoLogger, ImageClickListener {

    lateinit var presenter: SitePresenter
    lateinit var site: Site;
    lateinit var map: GoogleMap
    lateinit var imageAdapter: SiteImageAdapter
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

        presenter = initPresenter (SitePresenter(this)) as SitePresenter
        site = presenter.site
        imageAdapter = SiteImageAdapter(imageLayout, this, this)

        showSite(site)
    //        chooseImage.setOnClickListener {
//            presenter.cachePlacemark(placemarkTitle.text.toString(), description.text.toString())
//            presenter.doSelectImage()
//        }
    }

    override fun showSite(site: Site) {

       val user =  presenter.app.currentUser;

        if(site.name.isNotEmpty()) textName.setText(site.name)
        if(site.description.isNotEmpty()) textDescription.setText(site.description)

        textDescription.setText(site.description)
        textRating.text = site.raiting.toString(true)

        if(user.notes[site.id] != null) textNotes.text = user.notes[site.id];

        if(site.description.isNotEmpty()) textDescription.setText(site.description)

        this.showLocation(site.location)
        this.showImages(site, true)

//        btnSave.backgroundTintList = ContextCompat.getColorStateList(
//            btnSave.context,
//            R.color.purple_200
//        )

    }

    override fun showLocation (loc : Location) {
        text_loc_ing.text = ("ing: " +  "%.6f".format(loc.lat))
        text_loc_inv.text = ("inv: " + "%.6f".format(loc.lng))
    }

    fun showImages (site: Site, isEditable: Boolean = false) {
        imageAdapter.passImages(site, isEditable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
        info("onSetHead $view $image $index")
        presenter.onSetHeadImage(image, index);
        showImages(site, true)
    }


}