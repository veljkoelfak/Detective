package com.veljko.detective

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class RegisterScreen : AppCompatActivity() {

    private lateinit var viewModel: RegLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)

        viewModel = ViewModelProvider(this).get(RegLogViewModel::class.java)



        val registerButton = findViewById<Button>(R.id.RegisterButton) as Button
        var userID = ""
        var usernameText = ""

        registerButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.email)
            val emailText = email.text.toString()
            val password = findViewById<EditText>(R.id.password)
            val passwordText = password.text.toString()
            val username = findViewById<EditText>(R.id.username)
            usernameText = username.text.toString()

            val cpassword = findViewById<EditText>(R.id.cpassword)
            val cpasswordText = password.text.toString()

            if (passwordText.isEmpty() ) {
                val shakeAnimation = ObjectAnimator.ofFloat(password, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                shakeAnimation.duration = 500
                val animatorSet = AnimatorSet()
                animatorSet.play(shakeAnimation)
                animatorSet.start()
            }

            if (emailText.isEmpty()) {
                val shakeAnimation = ObjectAnimator.ofFloat(email, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                shakeAnimation.duration = 500
                val animatorSet = AnimatorSet()
                animatorSet.play(shakeAnimation)
                animatorSet.start()
            }

            if (usernameText.isEmpty()) {
                val shakeAnimation = ObjectAnimator.ofFloat(username, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                shakeAnimation.duration = 500
                val animatorSet = AnimatorSet()
                animatorSet.play(shakeAnimation)
                animatorSet.start()
            }

            if (passwordText != cpasswordText) {
                cpassword.setBackgroundColor(Color.parseColor("#FF0000"))
            }

            if (usernameText.isNotEmpty() && emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                viewModel.registerUser(emailText,passwordText)
}
        }

        // You can retrieve the password using the text property of the EditText

        viewModel.userLiveData.observe(this, Observer { user ->
            if (user != null) {

                val intent = Intent(this, CreateProfileActivity::class.java)
                intent.putExtra("username", usernameText)
                startActivity((intent))
            } else {
                Toast.makeText(getApplicationContext(),"There was an error while registering. Please try later",Toast.LENGTH_SHORT);
            }
        })


    }


}