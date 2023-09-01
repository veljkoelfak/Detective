package com.veljko.detective

import android.location.Location
import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URI

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
        db.collection("Users").add(data
        ).addOnCompleteListener(onCompleteListener)
    }

    data class UserPoints(
        val id: String? = null,
        val username: String? = null,
        val points: Number? = null
        )


    fun addObject(name: String, desc: String, loc: GeoPoint, onCompleteListener: OnCompleteListener<DocumentReference> ) {
        var data = hashMapOf<String, Any>("name" to name,
            "desc" to desc,
            "location" to loc)
        db.collection("clues").add(data).addOnCompleteListener(onCompleteListener)
    }

    data class Objects(
        val id: String? = null,
        val name: String? = null,
        val desc: String? = null,
        val loc : GeoPoint? = null
    )

}