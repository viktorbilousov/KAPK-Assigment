package vib.oth.archaeological_fieldwork.store.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.wit.placemark.helpers.readImageFromPath
import vib.oth.archaeological_fieldwork.models.User
import java.io.ByteArrayOutputStream
import java.io.File

class UsersFireStore(val context: Context) : BaseStore<User>, AnkoLogger {
  val users = ArrayList<User>()
  lateinit var userId: String
  lateinit var db: DatabaseReference
  lateinit var st: StorageReference

  override fun findAll(): List<User> {
    return users
  }

  override fun findById(id: Long): User? {
    return users.find { p -> p.id == id }
  }

  override fun create(user: User) {
    val key = db.child("users").push().key
    key?.let {
      user.fbId = key
      users.add(user)
//      db.child("users").child(userId).child("sites").child(key).setValue(site)
      db.child("users").child(key).setValue(user)
      updateImage(user)
    }
  }
  // todo 
  override fun update(user: User) {
    var foundUser: User? = users.find { p -> p.fbId == user.fbId }
    if (foundUser != null) {
      foundUser.name = user.name
      foundUser.email = user.email
      foundUser.favoriteSites = user.favoriteSites
      foundUser.givenRating = user.givenRating
      foundUser.image = user.image
      foundUser.notes = user.notes
      foundUser.visitedSites = user.visitedSites
    }

//    db.child("users").child(userId).child("Users").child(User.fbId).setValue(User)
    db.child("users").child(user.fbId).setValue(user)

    // ?
    updateImage(user)

    // todo 

  }

  override fun delete(user: User) {
//    db.child("users").child(userId).child("Users").child(User.fbId).removeValue()
    db.child("users").child(user.fbId).removeValue()
    users.remove(user)
  }

  override fun clear() {
    users.clear()
  }

  fun updateImage(user: User) {

    if (user.image.isEmpty()) {
      return
    }

    val image = user.image;

    val fileName = File(image)
    val imageName = fileName.name

    var imageRef = st.child(userId + '/' + imageName)
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
            user.image = it.toString()
            db.child("users").child(user.fbId).setValue(user)
          }
        }

    }
  }


  override fun fetch(UsersReady: () -> Unit) {
    val valueEventListener = object : ValueEventListener {
      override fun onCancelled(dataSnapshot: DatabaseError) {
      }
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot.children.mapNotNullTo(users) { it.getValue(User::class.java) }
        UsersReady()
      }
    }
    userId = FirebaseAuth.getInstance().currentUser!!.uid
    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    users.clear()
    db.child("users").addListenerForSingleValueEvent(valueEventListener)
  }
}