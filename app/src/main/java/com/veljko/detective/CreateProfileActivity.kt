package com.veljko.detective

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File


class CreateProfileActivity : AppCompatActivity() {

    private var uri = Uri.EMPTY
    private lateinit var viewModel: RegLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)


        val username = intent.getStringExtra("username")
        viewModel = ViewModelProvider(this).get(RegLogViewModel::class.java)

        val user = Firebase.auth.currentUser

        val finishButton = findViewById<Button>(R.id.FinishButton) as Button

        finishButton.setOnClickListener {
            val imageView = findViewById<ImageView>(R.id.addPhoto)
            val firstName = findViewById<EditText>(R.id.registerName)
            val firstNameText = firstName.text.toString()
            val lastName = findViewById<EditText>(R.id.registerLast)
            val lastNameText = lastName.text.toString()
            val phoneNumber = findViewById<EditText>(R.id.registerPhNo)
            val phoneNumberText = phoneNumber.text.toString()

            if (firstNameText.isEmpty() ) {
                val shakeAnimation = ObjectAnimator.ofFloat(firstName, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                shakeAnimation.duration = 500
                val animatorSet = AnimatorSet()
                animatorSet.play(shakeAnimation)
                animatorSet.start()
            }

            if (lastNameText.isEmpty()) {
                val shakeAnimation = ObjectAnimator.ofFloat(lastName, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                shakeAnimation.duration = 500
                val animatorSet = AnimatorSet()
                animatorSet.play(shakeAnimation)
                animatorSet.start()
            }

            if (phoneNumberText.isEmpty()) {
                val shakeAnimation = ObjectAnimator.ofFloat(phoneNumber, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                shakeAnimation.duration = 500
                val animatorSet = AnimatorSet()
                animatorSet.play(shakeAnimation)
                animatorSet.start()
            }


            if (firstNameText.isNotEmpty() && lastNameText.isNotEmpty() && phoneNumberText.isNotEmpty()) {
                viewModel.appProfile(user!!,username!!, uri)
                viewModel.createUser(user.uid,firstNameText, lastNameText, phoneNumberText)
                viewModel.addPoints(username, user.uid)
            }
        }

        viewModel.createLiveData.observe(this, Observer { profileSuccess ->
            if (profileSuccess == true) {

                val intent = Intent(this, LoginScreen::class.java)
                startActivity((intent))
            } else {
                Toast.makeText(getApplicationContext(),"There was an error while registering. Please try later",
                    Toast.LENGTH_SHORT);
            }
        })

    }


    private val getPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    uri = result.data?.data
                    val imageView = findViewById<ImageView>(R.id.addPhoto)
                    imageView.setImageURI((uri))
                }
            }
        }

    private val getPictureCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                Log.d(ContentValues.TAG, "DATAAA")

                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Avatar.jpg")

                val uri = FileProvider.getUriForFile(
                    this,
                    this.applicationContext.packageName + ".provider",
                    file
                )

                val imageView = findViewById<ImageView>(R.id.addPhoto)
                imageView.setImageURI((uri))
            }
        }



    fun onImageViewClicked(view: View) {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, which ->
            when (options[which]) {
                "Take Photo" -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Avatar.jpg")

                    val uri = FileProvider.getUriForFile(
                        this,
                        this.applicationContext.packageName + ".provider",
                        file
                    )
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    getPictureCamera.launch(takePicture)

                }
                "Choose from Gallery" -> {
                    val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    getPicture.launch(pickPhotoIntent)
                }
                "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }}




