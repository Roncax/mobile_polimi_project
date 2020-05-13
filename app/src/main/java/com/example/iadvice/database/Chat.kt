package com.example.iadvice.database

import androidx.room.*

@Entity(tableName = "chat_table")
data class Chat(
    @PrimaryKey(autoGenerate = true) var chatId: Int,
    @ColumnInfo(name = "chat_owner") var user:String, //user a cui appartiene la chat
    @ColumnInfo(name = "chat_question") var question: String?, //this is also the name of the chat
    @ColumnInfo(name = "poll") var poll: Poll?,
    @ColumnInfo(name = "active") var isActive:Boolean = true
)

data class ChatWithMessages(
    @Embedded val chat: Chat,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId"
    )
    val messages: List<Message>
)


