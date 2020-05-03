package com.example.iadvice.database

import androidx.room.*

@Dao
interface ChatDao {

    @Insert
    fun insert(chat: Chat)

    @Delete
    fun delete(chat: Chat)

    @Update
    fun update(chat: Chat)

    @Query("SELECT * FROM chat_table")
    fun getAll(): List<Chat>?

    @Query ("DELETE FROM chat_table")
    fun deleteAll()

    @Query("SELECT * FROM chat_table WHERE chatId = :chatId")
    fun getChatById(chatId: Int): Chat

    @Transaction
    @Query("SELECT * FROM chat_table")
    fun getChatWithMessages(): List<ChatWithMessages>


}