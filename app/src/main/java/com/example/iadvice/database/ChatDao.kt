package com.example.iadvice.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChatDao {

    @Insert
    fun insert(chat: Chat)

    @Insert
    fun insert(message: Message)

    @Delete
    fun delete(chat: Chat)

    @Update
    fun update(chat: Chat)

    @Query("SELECT * FROM chat_table")
    fun getAll(): LiveData<List<Chat>?>

    @Query ("SELECT * FROM chat_table WHERE chatId = :chatId")
    fun getChat(chatId: Int): LiveData<Chat>

    //give a id of a chat, return all the associated messages
    @Transaction
    @Query("SELECT * FROM chat_table WHERE chatId = :chatId")
    fun getChatWithMessagesFromId(chatId: Int): LiveData<ChatWithMessages>

}