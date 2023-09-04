package com.veljko.detective

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference

class RegLogViewModel : ViewModel() {

    private val firebaseAuthenticationModel = FirebaseManager()

    private val _userLiveData = MutableLiveData<FirebaseUser?>()
    val userLiveData: LiveData<FirebaseUser?>
        get() = _userLiveData

    private val _loginData = MutableLiveData<Boolean?>()
    val loginData: LiveData<Boolean?>
        get() = _loginData

    private val _createLiveData = MutableLiveData<Boolean?>()
    val createLiveData: LiveData<Boolean?>
        get() = _createLiveData

    private val _registerLiveData = MutableLiveData<Boolean?>()
    val registerLiveData: LiveData<Boolean?>
        get() = _registerLiveData

    private val _pointsLiveData = MutableLiveData<Boolean?>()
    val pointsLiveData: LiveData<Boolean?>
        get() = _pointsLiveData

    private val _uploadAvatar = MutableLiveData<Boolean>()
    val uploadAvatar : LiveData<Boolean>
        get() = _uploadAvatar

    private val signedIn = MutableLiveData<Boolean>()

    fun registerUser(email: String, password: String) {
        firebaseAuthenticationModel.appRegister(email, password, OnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                _userLiveData.value = user // Registration success

            } else {
                _userLiveData.value = null // Registration failure
            }
        })
    }

    fun loginUser(email: String, password: String) {
        firebaseAuthenticationModel.appLogin(email, password, OnCompleteListener { task ->
            if (task.isSuccessful) {
                _loginData.value = true // Registration success

            } else {
                _loginData.value = false // Registration failure
            }
        })
    }

    fun appProfile(user: FirebaseUser, username: String, uri: Uri) {

       firebaseAuthenticationModel.appProfile(user,username, uri, OnCompleteListener { task ->
           if (task.isSuccessful) {
               _registerLiveData.value = true // Registration success

           } else {
               _registerLiveData.value = false // Registration failure
           }
       })
       }


    fun createUser(uid: String, firstName: String, lastName: String, phoneNo: String) {

       firebaseAuthenticationModel.createUser(uid, firstName, lastName, phoneNo, OnCompleteListener {task ->
           if (task.isSuccessful) {
               _createLiveData.value = true
           }
           else {
               _createLiveData.value = false
           }
       })

    }

    fun addPoints(username: String, id: String) {
        firebaseAuthenticationModel.addPoints(username, id, OnCompleteListener {task ->
            if (task.isSuccessful) {
                _pointsLiveData.value = true
            }
            else {
                _pointsLiveData.value = false
            }
    })}

    fun uploadAvatar(uid:String, file:Uri) {
        firebaseAuthenticationModel.uploadAvatar(uid, file, OnCompleteListener { task-> _uploadAvatar.value = task.isSuccessful })
    }



}