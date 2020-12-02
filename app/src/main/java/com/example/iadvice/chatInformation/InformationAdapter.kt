package com.example.iadvice.chatInformation

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.iadvice.R


data class InformationAdapter(var imageInformationList:List<Uri>, var activity: Activity) : BaseAdapter(){

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
        val view: View = View.inflate(activity, R.layout.information_item,null)

        val img_inf=view.findViewById<ImageView>(R.id.imageView_information_item)
        val descriptionTextView = view.findViewById<TextView>(R.id.textView_information_item)


        val lang_pic=imageInformationList.get(position)

        descriptionTextView.text = "test"
        //img_lang.drawable =langaugeList.get(position).img_icon
        //img_lang.setImageResource(lang_pic)



        return view
    }

}

