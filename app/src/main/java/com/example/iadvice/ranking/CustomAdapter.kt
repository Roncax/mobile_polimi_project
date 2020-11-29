package com.example.iadvice.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.User


class UserRankViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.ranking_item, parent, false)) {


    private var position: TextView? = null
    private var name: TextView? = null
    private var points: TextView? = null
    private var image: ImageView? = null


    init {
        position = itemView.findViewById(R.id.ranking_item_position)
        name = itemView.findViewById(R.id.ranking_item_name)
        points = itemView.findViewById(R.id.ranking_item_points)
        image = itemView.findViewById(R.id.ranking_item_image)
    }

    fun bind(user: User, positionIndex:Int) {
        name?.text = user.username
        points?.text = user.points.toString()
        position?.text = positionIndex.toString()
        //TODO show user image
    }

}

class ListAdapter(private val list: List<User>)
    : RecyclerView.Adapter<UserRankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRankViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserRankViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UserRankViewHolder, position: Int) {
        val user: User = list[position]
        holder.bind(user, position)
    }

    override fun getItemCount(): Int = list.size

}