package vib.oth.archaeological_fieldwork.views.site

import android.content.Context
import android.content.res.ColorStateList
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.childrenRecursiveSequence
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Site

interface ImageClickListener{
    fun onImageClick(view: View, image: String)
    fun onAddImage(view: View)
    fun onDelete (view: View, image: String, index: Int);
    fun onSetHead(view: View, image: String, index: Int);
}

class SiteImageAdapter(val relativeLayout: RelativeLayout, val context: Context,  val imageClickListener: ImageClickListener) {

    data class Buttons(val removeId: Int, val flagId: Int)
    data class Image(val id: Int, val imageIds: Array<Int>, val buttonsId: Array<Buttons>)
    var currentImageLayout: Image? = null;

    companion object {
        private val arrayImagesView = listOf(
            Image(
                R.layout.images_one,
                arrayOf(R.id.image1_1),
                arrayOf(Buttons(R.id.btnRemoveImage1_1, R.id.btnFlagImage1_1))
            ),
            Image(
                R.layout.images_two, arrayOf(R.id.image2_1, R.id.image2_2),
                arrayOf(
                    Buttons(R.id.btnRemoveImage2_1, R.id.btnFlagImage2_1),
                    Buttons(R.id.btnRemoveImage2_2, R.id.btnFlagImage2_2)
                )
            ),

            Image(
                R.layout.images_three, arrayOf(R.id.image3_1, R.id.image3_2, R.id.image3_3),
                arrayOf(
                    Buttons(R.id.btnRemoveImage3_1, R.id.btnFlagImage3_1),
                    Buttons(R.id.btnRemoveImage3_2, R.id.btnFlagImage3_2),
                    Buttons(R.id.btnRemoveImage3_3, R.id.btnFlagImage3_3),
                )
            ),
            Image(
                R.layout.images_four,
                arrayOf(R.id.image4_1, R.id.image4_2, R.id.image4_3, R.id.image4_4),
                arrayOf(
                    Buttons(R.id.btnRemoveImage4_1, R.id.btnFlagImage4_1),
                    Buttons(R.id.btnRemoveImage4_2, R.id.btnFlagImage4_2),
                    Buttons(R.id.btnRemoveImage4_3, R.id.btnFlagImage4_3),
                    Buttons(R.id.btnRemoveImage4_4, R.id.btnFlagImage4_4),
                )
            )
        )

        private const val defaultImage: Int = R.mipmap.no_add_image
        private const val selecedBtnColor =  R.color.orange
        private const val unselectedBtnColor =  R.color.grey

    }

    fun passImages(site: Site, isEditable: Boolean) {
        if (isEditable) passImagesEditable(site)
        else passImages(site)
    }

    fun passImagesEditable(site: Site) {
        val size = site.imagesCnt();
        if (size >= 4) {
            passImages(site)
            return
        }

        currentImageLayout = arrayImagesView[size];

        val li = LayoutInflater.from(context)
        val view: View = li.inflate(currentImageLayout!!.id, null)
        val imagesViewsIdsArrays = currentImageLayout!!.imageIds
        val buttonsIdArray = currentImageLayout!!.buttonsId;

        for (i in 0..size) {
            val imageViewId = imagesViewsIdsArrays[i]
            val imageView = view.findViewById<ImageView>(imageViewId)!!
            if (i == size) {
                imageView.setOnClickListener { imageClickListener.onAddImage(it) }
//                imageView.setOnClickListener { imageClickListener.onImageClick(view, site.getImage(i)) }
                val buttons = buttonsIdArray[i];
                view.findViewById<FloatingActionButton>(buttons.removeId).visibility = View.GONE
                view.findViewById<FloatingActionButton>(buttons.flagId).visibility = View.GONE
                Glide.with(imageView.context).load(defaultImage).into(imageView);
            } else {
                imageView.setOnClickListener {
                    imageClickListener.onImageClick(view, site.getImage(i))
                }

                val image = site.getImage(i);
               val buttons = buttonsIdArray[i];

                view.findViewById<FloatingActionButton>(buttons.removeId).setOnClickListener {
                    imageClickListener.onDelete(view, image, i)
                }
                view.findViewById<FloatingActionButton>(buttons.flagId).setOnClickListener {
                    imageClickListener.onSetHead(view, image, i)
                }


                Glide.with(imageView.context).load(image).into(imageView);
            }
        }
        selectFlag(site.headImageIndex, view)
        relativeLayout.removeAllViews()
        relativeLayout.addView(view);
    }

    fun passImages(site: Site) {

        val size = site.imagesCnt();
        if (size == 0) return


        val li = LayoutInflater.from(context)

        currentImageLayout = arrayImagesView[size - 1];

        val view: View = li.inflate(currentImageLayout!!.id, null)
        val imagesViewsIdsArrays = currentImageLayout!!.imageIds
        val buttonsIdArray = currentImageLayout!!.buttonsId;

        imagesViewsIdsArrays.forEachIndexed { index, imageViewId ->
            val imageView = view.findViewById<ImageView>(imageViewId)
            val image = site.getImage(index);
            imageView.setOnClickListener { imageClickListener.onImageClick(view, image) }
            buttonsIdArray.forEachIndexed { index, buttons ->
                view.findViewById<FloatingActionButton>(buttons.removeId).setOnClickListener {
                    imageClickListener.onDelete(view, site.getImage(index), index)
                }
                view.findViewById<FloatingActionButton>(buttons.flagId).setOnClickListener {
                    imageClickListener.onSetHead(view, site.getImage(index), index)
                }
            }
            Glide.with(imageView.context).load(image).into(imageView);
        }


        selectFlag(site.headImageIndex, view)
        relativeLayout.removeAllViews()
        relativeLayout.addView(view);
    }

    private fun selectFlag(btnIndex: Int, view: View){

        currentImageLayout!!.buttonsId.forEachIndexed { index, buttons ->
            val btn = view.findViewById<FloatingActionButton>(buttons.flagId)
            if(index == btnIndex)   btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, selecedBtnColor))
            else btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, unselectedBtnColor))

        }
        view.refreshDrawableState()
    }

}