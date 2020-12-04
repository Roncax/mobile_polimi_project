package com.example.iadvice.evaluation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.chat.ChatActivity
import kotlinx.android.synthetic.main.evaluation_item.view.*


class EvaluationDataAdapter(private val mDataset: Array<String>,
                            internal var recyclerViewItemClickListener: ChatActivity
) : RecyclerView.Adapter<EvaluationDataAdapter.EvaluationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): EvaluationViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.evaluation_item, parent, false)

        return EvaluationViewHolder(v)

    }

    override fun onBindViewHolder(evaluationViewHolder: EvaluationViewHolder, i: Int) {
        evaluationViewHolder.mTextView.text = mDataset[i]
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }


    inner class EvaluationViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var mTextView: TextView = v.navSubtitle
        var mValuationText: TextView = v.editTextNumber

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            recyclerViewItemClickListener.clickOnItem(mDataset[this.adapterPosition])

        }
    }

    interface RecyclerViewItemClickListener {
        fun clickOnItem(data: String)
    }
}