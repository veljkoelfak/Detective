package com.veljko.detective

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseManager {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    fun appLogin(email: String, password:String, onCompleteListener: OnCompleteListener<AuthResult>) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(onCompleteListener)
    }

    fun appRegister(email: String, password: String, onCompleteListener: OnCompleteListener<AuthResult>) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(onCompleteListener)
    }

    fun appProfile(user: FirebaseUser, username: String, uri: Uri, onCompleteListener: OnCompleteListener<Void>) {

        val profileUpdates = userProfileChangeRequest {
            displayName = username
            photoUri = uri

        }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener(onCompleteListener)
    }

    fun createUser(uid: String, firstName: String, lastName: String, phoneNo: String, onCompleteListener: OnCompleteListener<DocumentReference>) {

        var data = hashMapOf<String, Any>("firstName" to firstName,
        "lastName" to lastName,
        "phoneNumber" to phoneNo)
        db.collection("users").add(data
        ).addOnCompleteListener(onCompleteListener)
    }

    data class UserPoints(
        val id: String? = null,
        val username: String? = null,
        val points: Number? = null
        )


    fun addObject(type: String, desc: String, loc: GeoPoint, author: String, diff : Number, date: Timestamp, photo: String, onCompleteListener: OnCompleteListener<DocumentReference> ) {
        var data = hashMapOf<String, Any>("type" to type,
            "desc" to desc,
            "author" to author,
            "date" to date,
            "diff" to diff,
            "photo" to photo,
            "location" to loc)
        db.collection("clues").add(data).addOnCompleteListener(onCompleteListener)
    }

    fun addPoints(username: String, id: String, onCompleteListener: OnCompleteListener<DocumentReference> ) {
        var data = hashMapOf<String, Any>("username" to username, "points" to 0)
        db.collection("points").document(id).set(data)
    }

    data class Objects(
        val id: String? = null,
        val type: String? = null,
        val desc: String? = null,
        val author: String? = null,
        val diff: Number? = null,
        val date: Timestamp? = null,
        val photo: String? = null,
        val loc: GeoPoint? = null
    )

}