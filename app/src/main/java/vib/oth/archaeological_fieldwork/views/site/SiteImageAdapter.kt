package vib.oth.archaeological_fieldwork.views.site

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Site

interface ImageClickListener{
    fun onImageClick(view: View, image: String)
    fun onAddImage(view: View)
}

class SiteImageAdapter(val relativeLayout: RelativeLayout, val context: Context,  val imageClickListener: ImageClickListener)  {

    companion object {
      private val arrayImagesView = listOf(
          Pair(R.layout.images_one, arrayOf(R.id.image1_1)),
          Pair(R.layout.images_two, arrayOf(R.id.image2_1, R.id.image2_2)),
          Pair(R.layout.images_three, arrayOf(R.id.image3_1, R.id.image3_2, R.id.image3_3)),
          Pair(R.layout.images_four, arrayOf(R.id.image4_1, R.id.image4_2, R.id.image4_3, R.id.image4_4 )
          )
      )
        private const val defaultImage : Int = R.mipmap.no_add_image
    }

    fun passImages(site: Site, isEditable: Boolean){
        if(isEditable) passImagesEditable(site)
        else passImages(site)
    }

    fun passImagesEditable(site: Site){
        val size = site.imagesCnt();
        if(size >= 4) {
            passImages(site)
            return
        }

        val li = LayoutInflater.from(context)
        val view: View = li.inflate(arrayImagesView[size].first, null)

        val imagesViewsArray  = arrayImagesView[size].second

        for (i in 0 .. size ) {
            val imageViewId = imagesViewsArray[i]
            val imageView = view.findViewById<ImageView>(imageViewId)!!
            if(i==size){
                imageView.setOnClickListener { imageClickListener.onAddImage(it) }
//                imageView.setOnClickListener { imageClickListener.onImageClick(view, site.getImage(i)) }
                Glide.with(imageView.context).load(defaultImage).into(imageView);
            }else{
                imageView.setOnClickListener { imageClickListener.onImageClick(view, site.getImage(i)) }
                Glide.with(imageView.context).load(site.getImage(i)).into(imageView);
            }
        }
        relativeLayout.removeAllViews()
        relativeLayout.addView(view);
    }

    fun passImages(site: Site) {

        val size = site.imagesCnt();
        if(size == 0) return


        val li = LayoutInflater.from(context)
        val view: View = li.inflate(arrayImagesView[size-1].first, null)
        val imagesViewsIdsArrays = arrayImagesView[size - 1].second

        imagesViewsIdsArrays.forEachIndexed { index, imageViewid ->
            val imageView = view.findViewById<ImageView>(imageViewid)
            val image = site.getImage(index);
            imageView.setOnClickListener {  imageClickListener.onImageClick(view, image) }
            Glide.with(imageView.context).load(image).into(imageView);
        }

        relativeLayout.removeAllViews()
        relativeLayout.addView(view);
    }

}