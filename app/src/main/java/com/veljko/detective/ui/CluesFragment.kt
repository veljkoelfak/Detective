package com.veljko.detective.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veljko.detective.R
import com.veljko.detective.UserDataViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CluesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CluesAdapter

    private val viewModel: UserDataViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_clues, container, false)


        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = CluesAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.objectData.observe(this.viewLifecycleOwner, Observer { data ->
            Log.d(ContentValues.TAG, "DATAAA")
            adapter.setData(data)
        })

        return view
    }


}