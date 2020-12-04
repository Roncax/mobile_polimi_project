package com.example.iadvice.evaluation

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.evaluation_dialog.*
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

    var userEvaluationListView = mutableMapOf<String, View>()
    var usernameList = usernameList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.evaluation_dialog)


        val inflater:LayoutInflater = LayoutInflater.from(context)
        val linear: LinearLayout = findViewById(R.id.linear_layout_evaluation)



        usernameList.forEach { (k, v) ->


            val layout: View = inflater.inflate(R.layout.evaluation_item, null, false)
            layout.navSubtitle.text = v
            linear.addView(layout)

            userEvaluationListView[k] = layout

        }

        cancel_evaluation_button.setOnClickListener(this)
        done_evaluation_button.setOnClickListener(this)

    }




    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancel_evaluation_button -> {
                dismiss()
            }
            R.id.done_evaluation_button -> {
                Firebase.database.reference.child("chats").child(PersistenceUtils.currenChatId).child("active").setValue(false)
                retrieveUserEvaluations()
                Toast.makeText(
                    context, "Chat successfully closed",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController(activity, R.id.myNavHostFragment).popBackStack()
                dismiss()
            }
            else -> {
            }
        }
        dismiss()
    }

    private fun retrieveUserEvaluations() {
        var usernameListEvaluation = mutableMapOf<String, String>()
        userEvaluationListView.forEach { (k, v) ->
            usernameListEvaluation[k] = v.ratingBar2.rating.toInt().toString()
        }
        Log.d(TAG, "User evaluation list: $usernameListEvaluation")

    PersistenceUtils.addEvaluationToUser(usernameListEvaluation)
    }
}