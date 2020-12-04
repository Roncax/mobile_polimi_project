package com.example.iadvice.evaluation

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import kotlinx.android.synthetic.main.evaluation_dialog.*

class CustomListViewEvaluationDialog(var activity: Activity, private var adapter: RecyclerView.Adapter<*>) : Dialog(activity),
    View.OnClickListener {
    var dialog: Dialog? = null

    private var recyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.evaluation_dialog)

        recyclerView = recycler_view
        mLayoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = adapter

        cancel_evaluation_button.setOnClickListener(this)
        done_evaluation_button.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancel_evaluation_button -> {
                dismiss()
            }
            R.id.done_evaluation_button -> {
                dismiss()
            }
            else -> {
            }
        }
        dismiss()
    }
}