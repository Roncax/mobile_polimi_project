package com.example.iadvice.chat


data class Message(
    var user: String,
    var message: String,
    var time: Long,
    var chatId: Int
) {}