package com.example.iadvice.database

import androidx.room.*

@Entity(tableName = "chat_table")
data class Chat(
    @PrimaryKey(autoGenerate = true) var chatId: Int,
    @ColumnInfo(name = "chat_owner") var user:String, //user a cui appartiene la chat
    @ColumnInfo(name = "chat_name") var name: String?
)

@Entity(tableName = "message_table")
data class Message (
    //TODO id chat, tavola differente?
    @ColumnInfo(name = "owner_chat") var chatId: Int,
    @ColumnInfo(name = "message_sender") var username: String, //user a cui appartiene il messaggio
    @ColumnInfo(name = "message_text") var text: String?,
    @ColumnInfo(name = "message_time") var time: Long
) {
    @PrimaryKey(autoGenerate = true) var messageId:Long = 0
}

data class ChatWithMessages(
    @Embedded val chat: Chat,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "owner_chat"
    )
    val messages: List<Message>
)
