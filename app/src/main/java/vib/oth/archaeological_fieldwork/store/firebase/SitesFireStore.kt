package vib.oth.archaeological_fieldwork.store.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.placemark.helpers.readImageFromPath
import vib.oth.archaeological_fieldwork.models.Site
import java.io.ByteArrayOutputStream
import java.io.File

class SitesFireStore(val context: Context) : BaseStore<Site>, AnkoLogger {
  val sites = ArrayList<Site>()
  lateinit var db: DatabaseReference
  lateinit var st: StorageReference

  override fun findAll(): List<Site> {
    return sites
  }

  override fun findById(id: Long): Site? {
    return sites.find { p -> p.id == id }
  }

  override fun create(site: Site) {
    val key = db.child("sites").push().key
    key?.let {
      site.fbId = key
      sites.add(site)
//      db.child("users").child(userId).child("sites").child(key).setValue(site)
      db.child("sites").child(key).setValue(site)
      updateImages(site)
    }
  }

  // todo 
  override fun update(site: Site) {
    var foundSite: Site? = sites.find { p -> p.fbId == site.fbId }
    if (foundSite != null) {
      foundSite.name = site.name
      foundSite.description = site.description
      foundSite.images = site.images
      foundSite.location = site.location
      foundSite.raiting = site.raiting;
    }

//    db.child("users").child(userId).child("sites").child(site.fbId).setValue(site)
    db.child("sites").child(site.fbId).setValue(site)

    // ?
    updateImages(site)

    // todo 

  }

  override fun delete(site: Site) {
//    db.child("users").child(userId).child("sites").child(site.fbId).removeValue()
    db.child("sites").child(site.fbId).removeValue()
    sites.remove(site)
  }

  override fun clear() {
    sites.clear()
  }

  fun updateImages(site: Site) {

    site.images.findLast { it != null } ?: return;


    site.images.forEachIndexed { index, image ->

      if(image == null) return@forEachIndexed

      val fileName = File(image)
      val imageName = fileName.getName()

      var imageRef = st.child(imageName) // todo test
      val baos = ByteArrayOutputStream()
      val bitmap = readImageFromPath(context, image)

      bitmap?.let {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask
          .addOnFailureListener {
            println(it.message)
            error(it.message.toString())
          }
          .addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
              site.images[index] = it.toString()
//              db.child("users").child(userId).child("sites").child(site.fbId).setValue(site)
              db.child("sites").child(site.fbId).setValue(site)
            }
          }
      }
    }
  }


  override fun fetch(ready: () -> Unit) {
    info("fetching Sites")
    val valueEventListener = object : ValueEventListener {
      override fun onCancelled(dataSnapshot: DatabaseError) {
      }
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot.children.mapNotNullTo(sites) { it.getValue(Site::class.java) }
        info("sites fetching OK")
        ready()
      }
    }
    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    sites.clear()
    db.child("sites").addListenerForSingleValueEvent(valueEventListener)
  }
}