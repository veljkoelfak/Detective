package com.veljko.detective

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class LoginScreen : AppCompatActivity() {

    private lateinit var viewModel: RegLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val regButton = findViewById(R.id.signupText) as TextView

        viewModel = ViewModelProvider(this).get(RegLogViewModel::class.java)

        regButton.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity((intent))
        }

        val loginButton = findViewById<Button>(R.id.loginButton) as Button

        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.email)
            val emailText = email.text.toString()
            val password = findViewById<EditText>(R.id.password)
            val passwordText = password.text.toString()


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

            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                viewModel.loginUser(emailText, passwordText)
            }

        }

        viewModel.loginData.observe(this, Observer { loginSuccess  ->
            if (loginSuccess == true ) {

                val intent = Intent(this, MainActivity::class.java)

                startActivity((intent))
                finish()
            } else {
                Toast.makeText(getApplicationContext(),"Error logging in",Toast.LENGTH_SHORT).show()
            }
        })
    }
}