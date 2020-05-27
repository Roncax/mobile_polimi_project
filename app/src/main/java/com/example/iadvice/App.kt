package com.example.iadvice

import android.app.Application
import com.example.iadvice.database.User

class App : Application() {
    companion object {
        lateinit var user: User
    }
}
