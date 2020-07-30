package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    var chatId: String = "-1",
    var user: String = "", //user_id message owner
    var nickname: String = "", //nickname user
    var messageId: String = "",
    var text: String = "",
    var time: Long = 0
    )