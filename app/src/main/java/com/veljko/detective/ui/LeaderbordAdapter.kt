package com.veljko.detective.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veljko.detective.FirebaseManager
import com.veljko.detective.R

class LeaderbordAdapter :
    RecyclerView.Adapter<LeaderbordAdapter.ViewHolder>() {

    private var data : List<FirebaseManager.UserPoints>? = null

    fun setData(newData: List<FirebaseManager.UserPoints>) {
        data = newData
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userView: TextView
        val pointView: TextView
        val numberView : TextView

        init {
            // Define click listener for the ViewHolder's View
            userView = view.findViewById(R.id.usernameTextView)
            pointView = view.findViewById(R.id.pointsTextView)
            numberView = view.findViewById(R.id.numberTextView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.pointView.text = data?.get(position)?.points.toString() + " points"
        viewHolder.userView.text = data?.get(position)?.username
        viewHolder.numberView.text = (position+1).toString() + "."



    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

}