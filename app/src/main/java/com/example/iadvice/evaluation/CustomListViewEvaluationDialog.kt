package com.example.iadvice.evaluation

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import kotlinx.android.synthetic.main.chat_information.*
import kotlinx.android.synthetic.main.evaluation_dialog.*
import kotlinx.android.synthetic.main.evaluation_item.*
import kotlinx.android.synthetic.main.evaluation_item.view.*

class CustomListViewEvaluationDialog(
    var activity: FragmentActivity,
    usernameList: MutableMap<String, String>
) : Dialog(activity),
    View.OnClickListener {

    companion object {
        const val TAG = "CUSTOM_EVALUATION"
    }

    var dialog: Dialog? = null

    var usernameList = usernameList
    private var linearLayout: LinearLayout? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.evaluation_dialog)

        linearLayout = linear_layout_evaluation

        usernameList.forEach { (k, v) ->


            val item = cardView_evaluation_item
            cardView_evaluation_item.navSubtitle.text = v
            linear_layout_evaluation.addView(item)
            Log.d(TAG, "Username: $v")
            onAddField(linear_layout_evaluation)

        }


        cancel_evaluation_button.setOnClickListener(this)
        done_evaluation_button.setOnClickListener(this)

    }

    fun onAddField(view: View) {
        val inflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.evaluation_item, null)
        linearLayout!!.addView(rowView, linearLayout!!.childCount - 1)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancel_evaluation_button -> {
                dismiss()
            }
            R.id.done_evaluation_button -> {
                retrieveUserEvaluations()
                dismiss()
            }
            else -> {
            }
        }
        dismiss()
    }

    private fun retrieveUserEvaluations() {

        TODO("Not yet implemented")
    }
}