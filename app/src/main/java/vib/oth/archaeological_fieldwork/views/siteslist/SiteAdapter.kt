package vib.oth.archaeological_fieldwork.views.siteslist

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_search.view.*
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
interface SearchListener:  SearchView.OnQueryTextListener{
  fun onCleanSearch()
  fun buttonState(): () -> Boolean
}


class SiteAdapter constructor(
  private var sites: List<Site>,
  private val user: User,
  private val listener: SiteListener,
  private val searchListener: SearchListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private enum class VIEW_TYPE(val num: Int){
    SEARCH(1), CARD(2);
    companion object{
      fun parse(num: Int): VIEW_TYPE?{
        return values().findLast { it.num == num }
      }
    }
  }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    return when (VIEW_TYPE.parse(viewType)!!) {
      VIEW_TYPE.CARD -> MainHolder(
          LayoutInflater.from(parent.context).inflate(
              R.layout.card_site,
              parent,
              false
          ),
          user
      )
      VIEW_TYPE.SEARCH -> SearchHolder(LayoutInflater.from(parent.context).inflate(
          R.layout.card_search,
          parent,
          false
      ))
    }
  }

  override fun getItemViewType(position: Int): Int {
    if(position == 0) return VIEW_TYPE.SEARCH.num
    return VIEW_TYPE.CARD.num
  }



  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (VIEW_TYPE.parse(holder.itemViewType)!!) {
      VIEW_TYPE.SEARCH ->    (holder as SearchHolder).bind(searchListener)
      VIEW_TYPE.CARD ->      (holder as MainHolder).bind(sites[holder.adapterPosition-1], listener)
    }
  }

  override fun getItemCount(): Int = sites.size + 1

  class SearchHolder  constructor(view: View) : RecyclerView.ViewHolder(view){
     fun bind(searchListener: SearchListener) {
       setEnableBtn(itemView.cleanSearch, searchListener.buttonState().invoke())

       itemView.search_location.setOnQueryTextListener(
           object : SearchView.OnQueryTextListener{
             override fun onQueryTextSubmit(query: String?): Boolean {
                return searchListener.onQueryTextSubmit(query)
             }
             override fun onQueryTextChange(newText: String?): Boolean {
               return searchListener.onQueryTextChange(newText)
             }
           })
       itemView.search_location.setOnClickListener {
         (it as SearchView).isIconified = false
       }
       itemView.cleanSearch.setOnClickListener {
         setEnableBtn(itemView.cleanSearch, false)
         searchListener.onCleanSearch()
       }
     }


    fun setEnableBtn(button: ImageButton, isEnable: Boolean){
      val color = if (isEnable) R.color.searchEnable else R.color.searchDisable
      button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, color))
    }
  }

  class MainHolder constructor(itemView: View, private val user: User) : RecyclerView.ViewHolder(itemView) {

    fun bind(site: Site, listener: SiteListener) {
      itemView.textName.text = site.name;
      itemView.textEditName.text = site.description
      itemView.text_loc_lng.text = "lng: ${"%.6f".format(site.location.lng)}"
      itemView.text_loc_lat.text = "lat: ${"%.6f".format(site.location.lat)}"
      itemView.checkBoxIsVisited.isChecked = user.visitedSites.contains(site.id);
      itemView.checkBoxIsFavorite.isChecked = user.favoriteSites.contains(site.id);
      itemView.textRating.text = site.raiting.toString(true)
      val image = site.getHeadImage() ?: R.drawable.no_image

      Glide.with(itemView.context).load(image).into(itemView.image1);

      itemView.setOnClickListener { listener.onSiteCardClick(site) }
      itemView.details.setOnClickListener { listener.onDetailsClick(site) }
      itemView.checkBoxIsFavorite.setOnClickListener { listener.onFavoriteChanged(site, it.checkBoxIsFavorite) }
      itemView.checkBoxIsVisited.setOnClickListener  { listener.onVisitedChanged(site, it.checkBoxIsVisited)  }
    }
  }
}