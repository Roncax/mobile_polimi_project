package com.example.iadvice.ranking

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.GlideApp

import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.User
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
        private var textViewName: TextView = itemView.ranking_item_name
        private var textViewPoints: TextView = itemView.ranking_item_points
        private var textViewPosition: TextView = itemView.ranking_item_position
        private var imageViewUser: ImageView = itemView.ranking_item_image

        fun bindItems(user: User, position: Int) {
            textViewName.text = user.username
            textViewPoints.text = user.points.toString()
            textViewPosition.text = (position+1).toString()


            val image = PersistenceUtils.allUserImages[user.uid]
            Log.d(TAG, "Show image on ranking: $image")

            GlideApp.with(this.itemView)
                .load(image)
                .circleCrop()
                .into(imageViewUser)


        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context)
            .inflate(R.layout.ranking_item, viewGroup, false)
        Log.d(TAG, "OnCreateViewHolder")

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bindItems(dataSet[position], position)
        Log.d(TAG, "OnBindViewHolder: ${dataSet[position].username}")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
