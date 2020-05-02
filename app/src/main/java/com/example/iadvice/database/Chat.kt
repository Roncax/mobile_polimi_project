package com.example.iadvice.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table")
data class Chat(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "chat_name") var name: String?,
    @ColumnInfo(name = "chat_messages") var messages: List<Message?>
)

data class Message (
    //TODO id chat, tavola differente?
    @ColumnInfo(name = "message_sender") var userId: Int,
    @ColumnInfo(name = "message_text") var text: String?,
    @ColumnInfo(name = "message_time") var time: String?,
    @ColumnInfo(name = "message_image") var image:String? //TODO come aggiungere l'immagine?
)
