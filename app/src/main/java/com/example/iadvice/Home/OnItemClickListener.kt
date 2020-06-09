package com.example.iadvice.Home

import com.example.iadvice.database.Chat

/**
 * Interface use do to handle click listener on QuestionChat
 */
interface OnItemClickListener {
    fun onItemClick(item: Chat)
}