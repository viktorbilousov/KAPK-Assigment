package vib.oth.archaeological_fieldwork.views.siteslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_site.view.*
import vib.oth.archaeological_fieldwork.R
import vib.oth.archaeological_fieldwork.models.Site
import vib.oth.archaeological_fieldwork.models.User

interface SiteListener {
  fun onSiteCardClick(site: Site)
  fun onFavoriteChanged(site: Site, checkBox: CheckBox)
  fun onVisitedChanged(site: Site, checkBox: CheckBox)
  fun onDetailsClick(site: Site)
}

class SiteAdapter constructor(
  private var sites: List<Site>,
  private val user: User,
  private val listener: SiteListener,
) : RecyclerView.Adapter<SiteAdapter.MainHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
    return MainHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.card_site,
        parent,
        false
      ),
        user
    )
  }

  override fun onBindViewHolder(holder: MainHolder, position: Int) {
    val site = sites[holder.adapterPosition]
    holder.bind(site, listener)
  }

  override fun getItemCount(): Int = sites.size

  class MainHolder constructor(itemView: View, private val user: User) : RecyclerView.ViewHolder(itemView) {

    fun bind(site: Site, listener: SiteListener) {
      itemView.name.text = site.name;
      itemView.textDescription.text = site.description
      // todo fix location lat, lng -> inv , ing
      itemView.text_loc_inv.text = "inv: ${site.location.lng.toString()}"
      itemView.text_loc_ing.text = "ing: ${site.location.lat.toString()}"
      itemView.checkBoxIsVisited.isChecked = user.visitedSites.contains(site.id);
      itemView.checkBoxIsFavorite.isChecked = user.favoriteSites.contains(site.id);
      val image = site.images[0] ?: R.drawable.no_image

      Glide.with(itemView.context).load(image).into(itemView.imageSite);

      itemView.setOnClickListener { listener.onSiteCardClick(site) }
      itemView.details.setOnClickListener { listener.onDetailsClick(site) }
      itemView.checkBoxIsFavorite.setOnClickListener { listener.onFavoriteChanged(site, it.checkBoxIsFavorite) }
      itemView.checkBoxIsVisited.setOnClickListener  { listener.onVisitedChanged(site, it.checkBoxIsVisited)  }
    }
  }
}