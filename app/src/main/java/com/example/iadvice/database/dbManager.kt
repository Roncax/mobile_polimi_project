package com.example.iadvice.database

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class dbManager {
    val usersJson = "A:\\Git\\mobile_polimi_project\\json\\user_database.json"
    val chatsJson = "A:\\Git\\mobile_polimi_project\\json\\chat_database.json"

    fun getUsersFromJson(context: Context): List<User>? {
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, User::class.java)
        val adapter: JsonAdapter<List<User>> = moshi.adapter(listType)

        val myjson = context.assets.open(usersJson).bufferedReader().use { it.readText() }

        return adapter.fromJson(myjson)
    }

    fun writeUserInJson(user: User){

    }

    fun getChatsOfUser(id: String){

    }

    fun writeChatOfUser(id: String){

    }

    fun deleteUser(id:String){

    }

    fun deleteChatOfUser(id: String){

    }
}
