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
    @ColumnInfo(name = "in_chat") var chatId: Int,
    @ColumnInfo(name = "message_sender") var username: String, //user a cui appartiene il messaggio
    @ColumnInfo(name = "message_text") var text: String?,
    @ColumnInfo(name = "message_time") var time: Long?,
    @ColumnInfo(name = "message_image") var image:String? //TODO come aggiungere l'immagine?
)

data class ChatWithMessages(
    @Embedded val chat: Chat,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "in_chat"
    )
    val messages: List<Message>
)
