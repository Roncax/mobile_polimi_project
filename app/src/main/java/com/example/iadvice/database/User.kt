package com.example.iadvice.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "age") var age: Int?,
    @ColumnInfo(name = "gender") var gender: String?,
    @ColumnInfo(name = "photo") var photo: String?, //it is the reference to the photo, not the photo itself?
    @ColumnInfo(name = "username") var nickname: String,
    @ColumnInfo(name = "password") var password: String

){
//important to set 0 if autogenerate
    @PrimaryKey(autoGenerate = true) var uid: Int = 0

}
