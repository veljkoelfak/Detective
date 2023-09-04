package com.veljko.detective

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayInputStream
import java.util.*

class UserDataViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val fb = FirebaseManager()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage

    private lateinit var pointsListener: ListenerRegistration
    private lateinit var objectListener: ListenerRegistration
    private lateinit var commentListener : ListenerRegistration

    private val _leaderData = MutableLiveData<List<FirebaseManager.UserPoints>>()
    val leaderData : LiveData<List<FirebaseManager.UserPoints>>
        get() = _leaderData

    private val _addobjectData = MutableLiveData<DocumentReference>()
    val addobjectData : LiveData<DocumentReference>
        get() = _addobjectData

    private val _objectData = MutableLiveData<List<FirebaseManager.Objects>>()
    val objectData : LiveData<List<FirebaseManager.Objects>>
        get() = _objectData

    private val _userData = MutableLiveData<FirebaseUser>()
    val userData: LiveData<FirebaseUser>
            get() = _userData

    private val _earnPts = MutableLiveData<Boolean>()
    val earnPts: LiveData<Boolean>
        get() = _earnPts

    private val _addComment = MutableLiveData<Boolean>()
    val addComment: LiveData<Boolean>
            get() = _addComment

    private val _commentData = MutableLiveData<List<FirebaseManager.Comment>>()
    val commentData: LiveData<List<FirebaseManager.Comment>>
        get() = _commentData

    private val _photoData = MutableLiveData<Bitmap>()
    val photoData : LiveData<Bitmap>
        get() = _photoData

    private val _avatarData = MutableLiveData<Bitmap>()
    val avatarData : LiveData<Bitmap>
        get() = _avatarData



    init {
        _userData.value = firebaseAuth.currentUser // Initialize with the current user, if available
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            _userData.value = firebaseAuth.currentUser
        }
    }


    fun getPoints() {
        pointsListener = db.collection("points").orderBy("points").addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            val tempList = mutableListOf<FirebaseManager.UserPoints>()
            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val id = doc.id
                    val points = doc.get("points") as? Number
                    val username = doc.getString("username")
                    val toAdd = FirebaseManager.UserPoints(id, username, points)
                    tempList.add(toAdd)

                }
            }
            _leaderData.value = tempList
        }

    }

    fun getObjects() {
        objectListener = db.collection("clues").addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val tempList = mutableListOf<FirebaseManager.Objects>()
            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val id = doc.id
                    val type = doc.getString("type")
                    val desc = doc.getString("desc")
                    val author = doc.getString("author")
                    val diff = doc.get("diff") as? Number
                    val date = doc.getTimestamp("date")
                    val loc = doc.getGeoPoint("location")
                    val toAdd = FirebaseManager.Objects(id, type, desc, author, diff, date, loc)
                    tempList.add(toAdd)

                }
            }
            _objectData.value = tempList
        }
    }
    fun addObject(type: String, desc: String, loc: GeoPoint, author: String, diff : Number, date: Timestamp) {
        fb.addObject(type, desc, loc, author, diff, date, OnCompleteListener { task ->
            _addobjectData.value = task.result
        })

    }

    fun earnPoints(uid: String, point: Double) {
        fb.earnPoints(uid, point, OnCompleteListener {task->
            _earnPts.value=task.isSuccessful
        })
    }

    fun addComment(uid: String, username: String, date: Timestamp, comment: String) {
        fb.addComment(uid, username, date, comment, OnCompleteListener { task -> _addComment.value = task.isSuccessful })
    }

    fun getComments(uid:String) {
        commentListener = db.collection("comments").document(uid).collection("comms").orderBy("date").addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val tempList = mutableListOf<FirebaseManager.Comment>()
            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val id = doc.id
                    val username = doc.getString("username")
                    val date = doc.getTimestamp("date")
                    val text = doc.getString("comment")
                    val toAdd = FirebaseManager.Comment(id, username, date, text)
                    tempList.add(toAdd)

                }
            }
            _commentData.value = tempList
        }
    }

    fun uploadPhoto(uid: String, file: Uri) {
        fb.uploadPhoto(uid, file, OnCompleteListener { task -> })
    }

    fun getPhoto(uid: String) {
        val ref = storage.reference.child("clues/$uid.jpg")
        ref.getBytes(1024*1024*5).addOnSuccessListener { imageBytes ->

            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(imageBytes))
            _photoData.value = bitmap

        }.addOnFailureListener { exception ->

        }
    }


    fun getAvatar(uid: String) {
        val ref = storage.reference.child("avatars/$uid.jpg")
        ref.getBytes(1024*1024*5).addOnSuccessListener { imageBytes ->

            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(imageBytes))
            _avatarData.value = bitmap

        }.addOnFailureListener { exception ->

        }
    }


    override fun onCleared() {
        super.onCleared()
        pointsListener.remove()
        objectListener.remove()
        commentListener.remove()
    }


}