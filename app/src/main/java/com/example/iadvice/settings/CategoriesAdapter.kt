package com.example.iadvice.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R



var click: Boolean = false


class CategoriesAdapter(
    val myDataset: Array<String>,
    val arrayChecked: BooleanArray,
    val categoryClickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private val listener: OnCategoryClickListener = categoryClickListener
    //todo impostare da fuori



    /* Create new views (invoked by the layout manager) */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    fun setClickable(clickable: Boolean) {
        click = clickable
        this.notifyDataSetChanged()
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = myDataset[position]
        val check = arrayChecked[position]
        holder.bind(item, check, categoryClickListener, myDataset.size.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size


    /**
     *  Holder for the view of the single item
     **/
    class CategoryViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {

        //val clickable = clickable

        val label: TextView = itemView.findViewById(R.id.category_label_text)
        val checkBox: CheckBox = itemView.findViewById(R.id.category_checkBox)


        fun bind(item: String, check: Boolean, clickListener: OnCategoryClickListener, size: String){  //todo questa size passata cosi è un po' una porcata
            label.text = item
            if (check){
                checkBox.isChecked = true
            }
            //set the clickability according to value passed
            checkBox.isClickable = click
        }

        companion object {
            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //val clickable = false
                val view = layoutInflater
                    .inflate(R.layout.categories_fragment, parent, false)   //TODO load the correct layout depending on the situation
                return CategoryViewHolder(view)
                //return CategoryViewHolder(view, clickable)
            }

        }

    }
}

interface OnCategoryClickListener {
    fun onItemClick(item: String)
}


