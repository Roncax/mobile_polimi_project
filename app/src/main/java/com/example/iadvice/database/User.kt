package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String = "",
    var username: String = "",
    var gender: String = "",
    var country: String = "",
    var categories: MutableMap<String, String> = mutableMapOf<String, String>(),
    var points: Int = 0
)
