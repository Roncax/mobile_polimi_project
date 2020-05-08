package com.example.iadvice.chat

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Body
import android.util.Log
import com.example.iadvice.database.Message
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ChatService"

interface ChatService {
    @POST("/message")
    // this post emulate the endpoint of the API
    fun postMessage(@Body body: Message): Call<Void>

    // to create the json
    companion object {
        //localhost for android
        private const val BASE_URL = "http://10.0.2.2:8080/"
        fun create(): ChatService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            Log.i(TAG, "Chiamato chatservice su URL $BASE_URL")
            return retrofit.create(ChatService::class.java)
        }
    }
}