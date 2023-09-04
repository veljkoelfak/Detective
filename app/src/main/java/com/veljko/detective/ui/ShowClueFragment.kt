package com.veljko.detective.ui

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.veljko.detective.FirebaseManager
import com.veljko.detective.R
import com.veljko.detective.UserDataViewModel
import java.text.SimpleDateFormat
import java.util.*


class ShowClueFragment : Fragment() {

    private var clueObject : FirebaseManager.Objects? = null
    private val viewModel: UserDataViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShowClueAdapter
    private val comment: String? = null
    private var user : FirebaseUser? = null
    private var extra : Double = 1.00
    private var storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        clueObject = args!!.getSerializable("data") as FirebaseManager.Objects
        if (args.getDouble("extra") != 0.0) {
            extra = args.getDouble("extra")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_show_clue, container, false)

        viewModel.getComments(clueObject?.id!!)

        viewModel.userData.observe(viewLifecycleOwner, Observer { data ->
            user = data
        })

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ShowClueAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.commentData.observe(this.viewLifecycleOwner, Observer { data ->
            Log.d(ContentValues.TAG, "DATAAA")
            adapter.setData(data.reversed())
        })

        val typeView : TextView = view.findViewById<TextView>(R.id.typeText)
        val diffView : TextView = view.findViewById(R.id.diffText)
        val dateView : TextView = view.findViewById(R.id.dateText)
        val descView : TextView = view.findViewById(R.id.descText)
        val imageView : ImageView = view.findViewById(R.id.objectPhoto)

        val id = clueObject!!.id

        val tmp = clueObject!!.date!!.toDate()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        typeView.setText(clueObject!!.type.toString())
        diffView.setText(clueObject!!.diff.toString())
        dateView.setText(dateFormat.format(tmp).toString())
        descView.setText(clueObject!!.desc!!.toString())

        viewModel.getPhoto(id!!)


        viewModel.photoData.observe(viewLifecycleOwner, Observer { data ->
            imageView.setImageBitmap(data)
        })

        val addButton : Button = view.findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener{
            val txt : TextView = view.findViewById(R.id.commentBox)
            val mult = clueObject!!.diff!!.toDouble()
            viewModel.addComment(clueObject!!.id!!, user!!.displayName!!, Timestamp(Date()), txt.text.toString())
            Log.d(TAG, extra.toString())
            viewModel.earnPoints(user!!.uid, 5.00 * mult * extra)
            txt.setText("")
        }

        val closeButton : Button = view.findViewById(R.id.closeButton)

        closeButton.setOnClickListener{
            getParentFragmentManager().popBackStack();
        }


        return view


        return view
    }



}