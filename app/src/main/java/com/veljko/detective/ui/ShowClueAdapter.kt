package com.veljko.detective.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veljko.detective.FirebaseManager
import com.veljko.detective.R
import java.text.SimpleDateFormat
import java.util.*

class ShowClueAdapter : RecyclerView.Adapter<ShowClueAdapter.ViewHolder>() {

    private var data : List<FirebaseManager.Comment>? = null

    fun setData(newData: List<FirebaseManager.Comment>) {
        data = newData
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView
        val textView: TextView
        val dateView: TextView

        init {
            // Define click listener for the ViewHolder's View
            nameView = view.findViewById(R.id.userNameTextView)
            textView = view.findViewById(R.id.commentTextViw)
            dateView = view.findViewById(R.id.dateTextView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.comment_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val tmp = data?.get(position)?.date?.toDate()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        viewHolder.nameView.text = data?.get(position)?.username.toString()
        viewHolder.dateView.text = dateFormat.format(tmp).toString()
        viewHolder.textView.text = data?.get(position)?.text.toString()

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
}