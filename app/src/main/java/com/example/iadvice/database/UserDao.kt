package com.example.iadvice.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM users_table")
    fun getAll(): List<User>?

    @Query ("DELETE FROM users_table")
    fun deleteAll()

    @Query("SELECT * FROM users_table WHERE uid = :userId")
    fun getUser(userId: Int): User

    @Query("SELECT * FROM users_table WHERE email = :mail")
    fun findByEmail(mail: String): User

}
