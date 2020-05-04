package com.example.iadvice.database

import androidx.room.*

@Dao
interface ChatDao {

    @Insert
    fun insert(chat: Chat)

    @Insert
    fun insert(message: Message)

    @Delete
    fun delete(chat: Chat)

    @Delete
    fun delete(message: Message)

    @Update
    fun update(chat: Chat)

    @Update
    fun update(message: Message)

    @Query("SELECT * FROM chat_table")
    fun getAll(): List<Chat>?

    @Query ("DELETE FROM chat_table")
    fun deleteAll()

    @Query ("SELECT * FROM chat_table WHERE chatId = :chatId")
    fun getChat(chatId: Int): Chat

    @Transaction
    @Query("SELECT * FROM chat_table")
    fun getChatWithMessages(): List<ChatWithMessages>

    //give a id of a chat, return all the associated messages
    @Transaction
    @Query("SELECT * FROM chat_table WHERE chatId = :chatId")
    fun getChatWithMessagesFromId(chatId: Int): ChatWithMessages

}