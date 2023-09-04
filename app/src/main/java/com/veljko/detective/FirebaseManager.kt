package com.veljko.detective

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.lang.reflect.Field
import java.sql.Time
import java.util.*

class FirebaseManager {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    fun uploadPhoto(uid: String, file: Uri, onCompleteListener: OnCompleteListener<UploadTask.TaskSnapshot>) {
        val ref = storage.reference.child("clues/$uid.jpg")
        val uploadTask = ref.putFile(file).addOnCompleteListener(onCompleteListener)

    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun uploadAvatar(uid: String, file: Uri, onCompleteListener: OnCompleteListener<UploadTask.TaskSnapshot>) {
        val ref = storage.reference.child("avatars/$uid.jpg")
        val uploadTask = ref.putFile(file).addOnCompleteListener(onCompleteListener)

    }

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


    fun addObject(type: String, desc: String, loc: GeoPoint, author: String, diff : Number, date: Timestamp, onCompleteListener: OnCompleteListener<DocumentReference> ) {
        var data = hashMapOf<String, Any>("type" to type,
            "desc" to desc,
            "author" to author,
            "date" to date,
            "diff" to diff,
            "location" to loc)
        db.collection("clues").add(data).addOnCompleteListener(onCompleteListener)
    }

    fun addPoints(username: String, id: String, onCompleteListener: OnCompleteListener<DocumentReference> ) {
        var data = hashMapOf<String, Any>("username" to username, "points" to 0)
        db.collection("points").document(id).set(data)
    }

    fun earnPoints(uid: String, add: Double, onCompleteListener: OnCompleteListener<DocumentReference> ) {
        db.collection("points").document(uid).update("points",FieldValue.increment(add))
    }

    fun addComment(uid: String, username: String, date: Timestamp, comment: String, onCompleteListener: OnCompleteListener<DocumentReference>) {

        var data = hashMapOf<String, Any>("username" to username,
            "date" to date,
            "comment" to comment
        )

        db.collection("comments").document(uid).collection("comms").add(data)

    }

    data class Comment(
        val id : String? = null,
        val username : String? = null,
        val date : Timestamp? = null,
        val text : String? = null
    )

    data class Objects(
        val id: String? = null,
        val type: String? = null,
        val desc: String? = null,
        val author: String? = null,
        val diff: Number? = null,
        val date: Timestamp? = null,
        val loc: GeoPoint? = null
    ) : java.io.Serializable

}