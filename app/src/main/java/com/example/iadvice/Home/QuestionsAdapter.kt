package com.example.iadvice.home

import android.R.id
import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class QuestionsAdapter(
    val myDataset: MutableList<Chat>,
    val chatType: String,
    val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<QuestionsAdapter.QuestionChatViewHolder>() {

    private val listener: OnItemClickListener = itemClickListener

    /* Create new views (invoked by the layout manager) */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionChatViewHolder {
        return QuestionChatViewHolder.from(parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: QuestionChatViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = myDataset[position]
        holder.getType(chatType)



        holder.bind(item, itemClickListener, myDataset.size.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size


    /**
     *  Holder for the view of the single item
     **/
    class QuestionChatViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        val title: TextView = itemView.findViewById(R.id.questionChatTitle_text)
        val question: TextView = itemView.findViewById(R.id.questionChatQuestion_text)
        val numberUsers: TextView = itemView.findViewById(R.id.questionNumberUsers_text)
        val Image: ImageView = itemView.findViewById(R.id.questionChat_image)
        val owner: TextView = itemView.findViewById(R.id.ownerChatQuestion_text)
        val ownerLabel: TextView = itemView.findViewById(R.id.ownerLabel_text)
        val card:CardView = itemView.findViewById(R.id.chatCard)
        val categoryImage: ImageView = itemView.findViewById(R.id.category_image)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: Chat, clickListener: OnItemClickListener, size: String){
            title.text = item.title
            question.text = item.question
            numberUsers.text = (item.userList.size + 1).toString()
            owner.text = item.owner.values.first()
            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("chat_images/${item.chatId}/${item.coverId}")

            GlideApp.with(this.itemView)
                .load(imageRef)
                .fitCenter()
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(Image)

            when(item.category){
                "Games" -> categoryImage.setImageResource(R.drawable.ic_games)
                "Sport" -> categoryImage.setImageResource(R.drawable.ic_sport)
                "Home" -> categoryImage.setImageResource(R.drawable.ic_home)
                "Clothes" -> categoryImage.setImageResource(R.drawable.ic_clothes)
                else -> categoryImage.setImageResource(R.drawable.ic_language)
            }


            itemView.setOnClickListener{
                //card.setCardBackgroundColor(Color.parseColor("#2196F3"))
                clickListener.onItemClick(item)
            }

        }

        fun getType(chatType: String) {
            when(chatType){
                "your" -> {
                    owner.visibility = View.GONE
                    ownerLabel.visibility = View.GONE
                }
                "other" -> {
                }
                else -> {
                    //TODO inserire cambio layout chatlist in archive
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): QuestionChatViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.other_question_chat, parent, false)
                return QuestionChatViewHolder(view)
            }
        }

    }
}

interface OnItemClickListener {
    fun onItemClick(item: Chat)
}


