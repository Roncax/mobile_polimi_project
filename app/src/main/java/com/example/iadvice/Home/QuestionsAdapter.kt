package com.example.iadvice.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class QuestionsAdapter ( val myDataset: MutableList<Chat>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<QuestionsAdapter.QuestionChatViewHolder>() {

    /* Create new views (invoked by the layout manager) */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionChatViewHolder {
        return QuestionChatViewHolder.from(parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: QuestionChatViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = myDataset[position]
        holder.bind(item, itemClickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size


    /**
     *  Holder for the view of the single item
     **/
    class QuestionChatViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTitle: TextView = itemView.findViewById(R.id.questionChatTitle_text)
        val questionSubtitle: TextView = itemView.findViewById(R.id.questionChatOwner_text)
        val questionImage: ImageView = itemView.findViewById(R.id.questionChat_image)


        fun bind(item: Chat, clickListener: OnItemClickListener){
            questionTitle.text = item.title
            questionSubtitle.text = item.owner
            val imageRef: StorageReference? = FirebaseStorage.getInstance().reference.child("chat_images/${item.chatId}/${item.coverId}" )
            GlideApp.with(this.itemView)
                .load(imageRef)
                .fitCenter()
                .circleCrop()
                .into(questionImage)

            itemView.setOnClickListener{
                clickListener.onItemClick(item)
            }

        }

        companion object {
            fun from(parent: ViewGroup): QuestionChatViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.question_chat, parent, false)
                return QuestionChatViewHolder(view)
            }
        }

    }

}






