package com.veljko.detective.ui

import android.content.Context
import android.location.Address
import android.location.Geocoder
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
        private var context : Context? = null

        fun setData(newData: List<FirebaseManager.Objects>) {
            data = newData
            notifyDataSetChanged()
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameView: TextView
            val descView: TextView
            val locView : TextView

            init {
                nameView = view.findViewById(R.id.userNameTextView)
                descView = view.findViewById(R.id.dateTextView)
                locView = view.findViewById(R.id.commentTextViw)
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.clue_item, viewGroup, false)

            context = view.context


            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            val geocoder = Geocoder(context!!)
            var addressText : MutableList<Address>

            addressText = geocoder.getFromLocation(data!!.get(position).loc!!.latitude, data!!.get(position).loc!!.longitude, 1)!!



            viewHolder.nameView.text = data?.get(position)?.type.toString()
            viewHolder.descView.text = data?.get(position)?.author.toString()
            viewHolder.locView.text = addressText.get(0).getAddressLine(0) + " " + addressText.get(0).getLocality()


        }

        override fun getItemCount(): Int {
            return data?.size ?: 0
        }
}