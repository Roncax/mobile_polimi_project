package com.example.iadvice.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//TODO bisogna guardare le observable queries
@Database(entities = [User::class, Chat::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val chatDao: ChatDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            //this is to use the context
            synchronized(this) {
                var instance = INSTANCE

                //TODO allowMainTread NOT TO DO, must change in coroutines
                //if no db yet
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
