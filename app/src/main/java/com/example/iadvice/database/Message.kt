package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    var chatId: String = "-1",
    var user: String = "", //user a cui appartiene il messaggio
    var messageId: String = "",
    var text: String = "",
    var time: Long = 0
    )