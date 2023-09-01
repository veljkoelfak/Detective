package com.veljko.detective.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veljko.detective.FirebaseManager
import com.veljko.detective.R

class CluesAdapter :

    RecyclerView.Adapter<CluesAdapter.ViewHolder>() {

        private var data : List<FirebaseManager.Objects>? = null

        fun setData(newData: List<FirebaseManager.Objects>) {
            data = newData
            notifyDataSetChanged()
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameView: TextView
            val descView: TextView

            init {
                // Define click listener for the ViewHolder's View
                nameView = view.findViewById(R.id.nameObjectTextView)
                descView = view.findViewById(R.id.descTextView)
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.clue_item, viewGroup, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.nameView.text = data?.get(position)?.name.toString()
            viewHolder.descView.text = data?.get(position)?.desc.toString()

        }

        override fun getItemCount(): Int {
            return data?.size ?: 0
        }
}