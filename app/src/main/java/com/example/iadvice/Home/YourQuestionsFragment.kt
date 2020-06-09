package com.example.iadvice.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.database.Poll
import com.example.iadvice.databinding.YourQuestionsFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class YourQuestionsFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: YourQuestionsFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private  var menu: MutableList<Chat> = mutableListOf()

    private lateinit var userId: String

    // private var chatList = ArrayList<String>() //TODO CANCELLA
    var chatList: MutableList<String> = mutableListOf()
    var mie_chat: MutableList<Chat> = mutableListOf()

    var owners:MutableList<Chat> = mutableListOf()

    private var dataList = ArrayList<HashMap<String, String>>()



    var lista_chat:MutableList<Chat> = mutableListOf()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.your_questions_fragment,
            container,
            false
        )
        return binding.root
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

        findChats() //Todo va spostato da qui, altrimenti ogni volta che ritorno al frame mi ricarica tutto.

        binding.fab.setOnClickListener { onFabClick() }

    }


    /**
     * Inner class to define a AsyncTask to perform the fetching of Json DB


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

    viewAdapter =  QuestionsAdapter(dataList,this@YourQuestionsFragment)

    recyclerView = binding.RecyclerView.apply {
    //used to improve performances
    setHasFixedSize(true)
    adapter = viewAdapter
    }
    }
    }
     */


    private fun findChats() {

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("chatlist")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.getChildren()) {
                        val value = snapshot.value
                        chatList.add(value.toString())
                    }
                    processData()
                }
            })
    }

    /**
     * dato il nome della chat ne vado a prendere l'oggetto vero e proprio

    private fun takeTheChat() {

        Log.i("TAKETHECHAT", "${chatList}")

        for (chat in chatList) {
            Log.i("TAKETHECHAT", "${chat}")

            FirebaseDatabase.getInstance().reference
                .child("chats")
                .child(chat)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {}

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                       /* val value = dataSnapshot.value
                        Log.i("VALUE", "${value}")
            */          Log.i("VALUE", "${dataSnapshot.value}")
                        val chatty = HashMap<String,String>()



                   /*     for(ds:DataSnapshot  in  dataSnapshot.getChildren()) {
                            val owner= ds.child("owner").toString()
                            chatty.put(chat,owner)
                            Log.i("CHATTY","${chatty.get("owner")}")
                        }
                    */


                    }
                })
        }


    }

    */


    private fun takeTheChat() {
        Log.i("CHATLIST","${chatList}")
        for (chat in chatList) {
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(chat)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    //println("loadPost:onCancelled ${databaseError.toException()}")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    //dataSnapshot.children.mapNotNullTo(menu) { it.getValue<Chat>(Chat::class.java) }
                   // for(ds:DataSnapshot  in  dataSnapshot.getChildren().filterNotNull()) {

                        lista_chat.clear()
                        //Log.i("KEY-VALUE","${ds.key} -- ${ds.value}")


                        val chat: Chat? = dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        lista_chat.add(chat)
                    }
                    Log.i("LISTA CHAT", "${lista_chat}")



                       /* val chatid = ds.child("chatid").getValue(String::class.java)
                        Log.i("chatid","${chatid}")
                        val isActive = ds.child("isActive").getValue(String()::class.java)
                        Log.i("isActive","${isActive}")
                        val owner = ds.child("owner").getValue(String::class.java)
                        Log.i("owner","${owner}")
                        val question = ds.child("question").getValue(String::class.java)
                        Log.i("question","${question}")
                        val userlist = ds.child("userlist").getValue(ArrayList<String>()::class.java)
                        Log.i("userList","${userlist}")
                        //val poll = ds.child("question").getValue(String::class.java)
                        val poll = Poll("prova","prova")

                       // var aaa: MutableList<String> = mutableListOf("a","b","c")
                        //val chat = Chat("dsdasadsdasdasdasda" ,owner,question ,true,aaa)
                        //Log.i("CHATTTT","${chat}")

*/

                    }

               // }
            })
        }

    }







    private fun processData() {
      takeTheChat()

        for (i in 0 until chatList.size) {
            val map = HashMap<String, String>()
            map["title"] = chatList.get(i)
            map["owner"] = "pirupiru"
            dataList.add(map)
        }

        viewAdapter = QuestionsAdapter(dataList, this@YourQuestionsFragment)

        recyclerView = binding.RecyclerView.apply {
            //used to improve performances
            setHasFixedSize(true)
            adapter = viewAdapter
        }
    }


    override fun onItemClick(item: HashMap<String, String>) {
        Toast.makeText(activity, "${item.get("title")} selected", Toast.LENGTH_SHORT).show()
    }

    fun onFabClick() {
        findNavController().navigate(R.id.newQuestionFragment, null)
    }

}
