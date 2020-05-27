package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class Chat(
    var chatId: Int,
    var user:String, //user (email) dello user a cui appartiene la chat
    var question: String?, //this is also the name of the chat
    var poll: Poll?,
    var isActive:Boolean = true
)



