package com.veljko.detective

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDataViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val fb = FirebaseManager()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var pointsListener: ListenerRegistration
    private lateinit var objectListener: ListenerRegistration

    private val _leaderData = MutableLiveData<List<FirebaseManager.UserPoints>>()
    val leaderData : LiveData<List<FirebaseManager.UserPoints>>
        get() = _leaderData

    private val _addobjectData = MutableLiveData<Boolean>()
    val addobjectData : LiveData<Boolean>
        get() = _addobjectData

    private val _objectData = MutableLiveData<List<FirebaseManager.Objects>>()
    val objectData : LiveData<List<FirebaseManager.Objects>>
        get() = _objectData

    private val _userData = MutableLiveData<FirebaseUser>()
    val userData: LiveData<FirebaseUser>
            get() = _userData


    init {
        _userData.value = firebaseAuth.currentUser // Initialize with the current user, if available
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            _userData.value = firebaseAuth.currentUser
        }
    }


    fun getPoints() {
        pointsListener = db.collection("users").orderBy("points").addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            val tempList = mutableListOf<FirebaseManager.UserPoints>()
            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val id = doc.id
                    val points = doc.get("points") as Number
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
                    val diff = doc.get("diff") as Number
                    val date = doc.getTimestamp("date")
                    val photo = doc.getString("photo")
                    val loc = doc.getGeoPoint("location")
                    val toAdd = FirebaseManager.Objects(id, type, desc, author, diff, date, photo, loc)
                    tempList.add(toAdd)

                }
            }
            _objectData.value = tempList
        }
    }
    fun addObject(type: String, desc: String, loc: GeoPoint, author: String, diff : Number, date: Timestamp, photo : String) {
        fb.addObject(type, desc, loc, author, diff, date, photo, OnCompleteListener { task ->
            _addobjectData.value = task.isSuccessful
        })

    }

    override fun onCleared() {
        super.onCleared()
        pointsListener.remove()
        objectListener.remove()
    }


}