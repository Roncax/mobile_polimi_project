package com.example.iadvice.Home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.Chat


class QuestionsAdapter ( val myDataset: MutableList<Chat>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<QuestionsAdapter.QuestionChatViewHolder>() {

    private val listener: OnItemClickListener = itemClickListener


    /* Create new views (invoked by the layout manager) */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsAdapter.QuestionChatViewHolder {
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
     *
     *  Holder for the view of the single item
     *
     **/
    class QuestionChatViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTitle: TextView = itemView.findViewById(R.id.questionChatTitle_text)
        val questionSubtitle: TextView = itemView.findViewById(R.id.questionChatOwner_text)
        // todo implement image
        //  val questionChat: ImageView = itemView.findViewById(R.id.questionChat_image)


        fun bind(item: Chat, clickListener: OnItemClickListener){
            questionTitle.text = item.question
            questionSubtitle.text = item.owner
            Log.i("BIND","${item.chatId}")

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






