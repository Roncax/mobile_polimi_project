package com.example.iadvice.chat

import com.example.iadvice.database.*

class SampleDbUpload() {

    val sampleFirstName_1 = "Paolo"
    val sampleFirstName_2 = "Tasker"

    val sampleEmail_1 = "paolo.roncaglioni@gmail.com"
    val sampleEmail_2 = "tasca@gmail.com"

    val sampleChatId_1= 123
    val sampleChatId_2= 1234

    val sampleTxt_1 = "Messaggio 1"

    val sampleMessage = Message(sampleChatId_1, sampleFirstName_1, sampleTxt_1, 1234)

    val user_1 = User(sampleFirstName_1, sampleEmail_1, 25, "male", "", "Roncax", "1")
    val chat_1 = Chat(sampleChatId_1, sampleFirstName_1, "chat_di_prova")

    val user_2 = User(sampleFirstName_2, sampleEmail_2, 21, "male", "", "Task", "1")
    val chat_2 = Chat(sampleChatId_1, sampleFirstName_2, "chat_di_prova")



    fun chat1Upload(chatDao: ChatDao){
        chatDao.insert(chat_1)
        chatDao.insert(sampleMessage)
    }

    fun chat2Upload(chatDao: ChatDao){
        chatDao.insert(chat_2)
        chatDao.insert(sampleMessage)
    }

    fun userUpload(userDao: UserDao){
        userDao.insert(user_1)
        userDao.insert(user_2)
    }

    fun deleteAllMsg(chatDao: ChatDao){
        chatDao.deleteAllChat()
        chatDao.deleteAllMessages()
    }

    fun deleteAllUser(userDao: UserDao){
        userDao.deleteAll()
    }
}