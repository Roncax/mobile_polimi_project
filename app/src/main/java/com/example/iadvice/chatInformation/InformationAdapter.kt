package com.example.iadvice.chatInformation

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.google.firebase.storage.StorageReference


data class InformationAdapter(var imageInformationList: List<StorageReference>, var activity: Activity) :
    BaseAdapter() {

    companion object {
        const val TAG = "INFORMATION_ADAPTER"
    }

    override fun getItem(position: Int): Any {
        return imageInformationList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imageInformationList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val pic = imageInformationList[position]
        val view: View = View.inflate(activity, R.layout.information_item, null)
        val imgInf = view.findViewById<ImageView>(R.id.imageView_information_item)

        Log.d(TAG, "Get pic $pic")

        GlideApp.with(view)
            .load(pic)
            .fitCenter()
            .transform(RoundedCorners(25))
            .into(imgInf)

        return view
    }

}

