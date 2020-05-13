package com.example.iadvice.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class Message (
    @ColumnInfo(name = "chatId") var chatId: Int,
    @ColumnInfo(name = "message_sender") var user: String, //user a cui appartiene il messaggio
    @ColumnInfo(name = "message_text") var text: String,
    @ColumnInfo(name = "message_time") var time: Long
) {
    @PrimaryKey(autoGenerate = true) var messageId:Long = 0
}