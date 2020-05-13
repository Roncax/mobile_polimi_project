package com.example.iadvice.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey var email: String,
    @ColumnInfo(name = "password") var password: String?,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "age") var age: Int?,
    @ColumnInfo(name = "gender") var gender: String?,
    @ColumnInfo(name = "categories") var categories:List<String>
)
