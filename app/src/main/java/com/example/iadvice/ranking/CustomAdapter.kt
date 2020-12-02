package com.example.iadvice.ranking

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.User
import kotlinx.android.synthetic.main.my_bubble.view.*
import kotlinx.android.synthetic.main.ranking_item.view.*


class CustomAdapter(val context: Context, private val dataSet: MutableList<User>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    companion object{
        const val TAG = "CUSTOM_ADAPTER"
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var textViewName: TextView = itemView.ranking_item_points

        fun bindItems(user: User) {
            textViewName.text = "paolo"
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context)
            .inflate(R.layout.evaluation_item, viewGroup, false)
        Log.d(TAG, "OnCreateViewHolder")

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bindItems(dataSet[position])
        Log.d(TAG, "OnBindViewHolder: ${dataSet[position].username}")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
