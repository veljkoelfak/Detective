package com.veljko.detective

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt

class UserDataViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val fb = FirebaseManager()

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



    fun getPoints() {
        pointsListener = db.collection("Users").orderBy("points").addSnapshotListener { querySnapshot, e ->
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
                    val name = doc.getString("name")
                    val desc = doc.getString("username")
                    val loc = doc.getGeoPoint("location")
                    val toAdd = FirebaseManager.Objects(id, name, desc, loc)
                    tempList.add(toAdd)

                }
            }
            _objectData.value = tempList
        }
    }

    override fun onCleared() {
        super.onCleared()
        pointsListener.remove()
        objectListener.remove()
    }

    fun addObject(name: String, desc: String, loc: GeoPoint) {
        fb.addObject(name, desc, loc, OnCompleteListener { task ->
            _addobjectData.value = task.isSuccessful
        })

    }
}