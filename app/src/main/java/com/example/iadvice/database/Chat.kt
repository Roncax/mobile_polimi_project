package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class Chat(
    var chatId: String = "",
    var user: String = "", //user (email) dello user a cui appartiene la chat
    var question: String = "", //this is also the name of the chat
    var poll: Poll = Poll("Testo", "Immagine"),
    var isActive: Boolean = true,
    var userList: MutableList<String> = mutableListOf()
)



