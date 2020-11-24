package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Chat(
    var chatId: String = "",
    var owner: String = "",
    var title: String = "",
    var question: String = "",
    var isActive: Boolean = true,
    var userList: MutableMap<String, String> = mutableMapOf(),
    var expiration: Date = Date(),
    var coverId:Int = -1
)

