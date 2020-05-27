package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message (
     var chatId: Int? = -1,
    var user: String? = "", //user a cui appartiene il messaggio
    var text: String? = "",
    var time: Long = 0
)