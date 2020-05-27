package com.example.iadvice.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var email: String? = "",
    var password: String? = "",
    var username: String? = "",
    var age: Int? = 0,
    var gender: String? = "",
    var categories: String? = ""
)
