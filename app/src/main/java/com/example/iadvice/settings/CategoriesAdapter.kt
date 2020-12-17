package com.example.iadvice.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CategoriesAdapter(val myDataset: MutableList<String>, val categoryClickListener: OnCategoryClickListener) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private val listener: OnCategoryClickListener = categoryClickListener

    /* Create new views (invoked by the layout manager) */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = myDataset[position]
        holder.bind(item, categoryClickListener, myDataset.size.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size


    /**
     *  Holder for the view of the single item
     **/
    class CategoryViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(R.id.category_label_text)
        val checkBox: CheckBox = itemView.findViewById(R.id.category_checkBox)

        fun bind(item: String, clickListener: OnCategoryClickListener , size:String){  //todo questa size passata cosi è un po' una porcata
            label.text = item
        }
        companion object {
            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.categories_fragment, parent, false)   //TODO load the correct layout depending on the situation
                return CategoryViewHolder(view)
            }
        }

    }
}

interface OnCategoryClickListener {
    fun onItemClick(item: String)
}


