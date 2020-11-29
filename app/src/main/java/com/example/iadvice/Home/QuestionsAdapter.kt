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
        holder.bind(item, itemClickListener, myDataset.size.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size


    /**
     *  Holder for the view of the single item
     **/
    class QuestionChatViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.questionChatTitle_text)
        val question: TextView = itemView.findViewById(R.id.questionChatQuestion_text)
        val numberUsers: TextView = itemView.findViewById(R.id.questionNumberUsers_text)
        val Image: ImageView = itemView.findViewById(R.id.questionChat_image)


        fun bind(item: Chat, clickListener: OnItemClickListener, size:String){  //todo questa size passata cosi è un po' una porcata
            title.text = item.title
            question.text = item.question
            numberUsers.text = size
            val imageRef: StorageReference? = FirebaseStorage.getInstance().reference.child("chat_images/${item.chatId}/${item.coverId}" )
            GlideApp.with(this.itemView)
                .load(imageRef)
                .fitCenter()
                .circleCrop()
                .into(Image)

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

interface OnItemClickListener {
    fun onItemClick(item: Chat)
}


