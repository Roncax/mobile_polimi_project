package com.example.iadvice.Home

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import org.json.JSONObject


class your_questions_fragment : Fragment(),OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var  yourQuestionsView: View
    /* Used for JSON mamagement in the innerClass */
    private var dataList = ArrayList<HashMap<String, String>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        yourQuestionsView= inflater.inflate(R.layout.your_questions_fragment, container, false)
        return yourQuestionsView
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchJsonData().execute()
    }



    /**
     * Inner class to define a AsyncTask to perform the fetching of Json DB
     */
    inner class fetchJsonData() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            //    findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            val data = "{\n" +
                    "\t\"chats\": [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"Big Dicks\",\n" +
                    "\t\t\t\"info\": \"we have big dicks\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"love pussy\",\n" +
                    "\t\t\t\"info\": \"we love it\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"pepperoniGuys\",\n" +
                    "\t\t\t\"info\": \"pepperoni at power\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"yes we can't\",\n" +
                    "\t\t\t\"info\": \"we cannot do it\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"w la foca\",\n" +
                    "\t\t\t\"info\": \"salviamo i cuccioli\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"prova\",\n" +
                    "\t\t\t\"info\": \"insert info here\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"prova\",\n" +
                    "\t\t\t\"info\": \"insert info here\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"prova\",\n" +
                    "\t\t\t\"info\": \"insert info here\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"prova\",\n" +
                    "\t\t\t\"info\": \"insert info here\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"prova\",\n" +
                    "\t\t\t\"info\": \"insert info here\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"prova\",\n" +
                    "\t\t\t\"info\": \"insert info here\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"id\": \"num\",\n" +
                    "\t\t\t\"title\": \"prova\",\n" +
                    "\t\t\t\"info\": \"insert info here\",\n" +
                    "\t\t\t\"owner\": \"paolo.roncaglioni@gmail.com\",\n" +
                    "\t\t\t\"type\": \"openchat\", \n" +
                    "\t\t\t\"photo\": \"id_photo\"\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}"

            return data
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            //  findViewById<ProgressBar>(R.id.loader).visibility = View.GONE

            val jsonObj = JSONObject(result)
            val chatsArr = jsonObj.getJSONArray("chats")
            for (i in 0 until chatsArr.length()) {
                val singleChat = chatsArr.getJSONObject(i)

                val map = HashMap<String, String>()
                map["id"] = singleChat.getString("id")
                map["title"] = singleChat.getString("title")
                map["info"] = singleChat.getString("info")
                map["type"] = singleChat.getString("type")
                map["photo"] = singleChat.getString("photo")
                dataList.add(map)
            }

            viewAdapter =  QuestionsAdapter(dataList,this@your_questions_fragment)

            recyclerView = yourQuestionsView.findViewById<RecyclerView>(R.id.RecyclerView).apply {
                //used to improve performances
                setHasFixedSize(true)
                adapter = viewAdapter
            }
        }
    }


    override fun onItemClick(item: HashMap<String, String>) {
        Toast.makeText(activity, "${item.get("title")} selected", Toast.LENGTH_SHORT).show()
    }

}
