package com.veljko.detective.ui

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.veljko.detective.FirebaseManager
import com.veljko.detective.LoginScreen
import com.veljko.detective.R
import com.veljko.detective.UserDataViewModel


class ProfileFragment : Fragment() {

    private val viewModel: UserDataViewModel by activityViewModels()
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val fb = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val imageView : ImageView = view.findViewById(R.id.objectPhoto)


        val textView : TextView = view.findViewById(R.id.firstText)
        textView.setText(user!!.displayName)

        val outButton : Button = view.findViewById(R.id.logButton)

        outButton.setOnClickListener{
            fb.signOut()
            val intent = Intent(activity, LoginScreen::class.java)
            startActivity(intent)
        }

        viewModel.getAvatar(user!!.uid)

        viewModel.avatarData.observe(viewLifecycleOwner, Observer { data ->
            imageView.setImageBitmap(data)
        })



        return view
    }


}