package com.example.iadvice.database

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User (val email: String,
val password:String,
val name:String,
val username:String,
val age:Int,
val gender:String,
val nickname:String)
