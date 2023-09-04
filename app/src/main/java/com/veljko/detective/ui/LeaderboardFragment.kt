package com.veljko.detective.ui

import android.os.Bundle
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


class LeaderboardFragment : Fragment() {


    private val viewModel: UserDataViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LeaderbordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        viewModel.getPoints()

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = LeaderbordAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.leaderData.observe(viewLifecycleOwner, Observer { newData->
            adapter.setData(newData.reversed())
        })

        return view
    }




}