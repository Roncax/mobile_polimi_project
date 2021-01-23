package com.example.iadvice.database
/*

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var userDaoDb: UserDao
    private lateinit var chatDao: ChatDao
    private lateinit var db: AppDatabase

    val sampleFirstName = "Paolo"
    val sampleEmail = "paolo.roncaglioni@gmail.com"
    val sampleChatId = 111
    val sampleTxt1 = "Questo é un messaggio"
    val sampleTxt2 = "Immagine"
    val user = User(sampleFirstName, sampleEmail, 25, "male", "", "Roncax", "eltinto1")
    val chat = Chat(sampleChatId, sampleFirstName, "chat_di_prova")
    val sampleMessage = Message(sampleChatId, sampleFirstName, sampleTxt1, 1234)
    val sampleMessage2 = Message(sampleChatId, sampleFirstName, sampleTxt2, 124)

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        userDaoDb = db.userDao
        chatDao = db.chatDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUser() {
        userDaoDb.insert(user)
        assertEquals("Insert or Get non funzionanti", sampleFirstName, userDaoDb.findByEmail(sampleEmail).firstName)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetChat() {
        chatDao.insert(chat)
        assertEquals("Insert or Get in Chat not working", sampleFirstName, chatDao.getChat(sampleChatId).user)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMessages() {
        chatDao.insert(chat)
        chatDao.insert(sampleMessage)
        chatDao.insert(sampleMessage2)
        for (elem in chatDao.getChatWithMessages()) {
            print("chatname:" + elem.chat.chatId + "\n")
            for (mess in elem.messages)
                print(mess.text + "\n")
        }
    }


}


 */