package com.example.iadvice.chat

import androidx.lifecycle.ViewModel
import com.example.iadvice.PersistenceUtils

class ChatActivityViewModel : ViewModel() {

    var currentChatId: String

init {
    currentChatId = PersistenceUtils.currenChatId
}

}