package com.example.iadvice.database


data class Poll (
    var pollName: String,
    var pollQuestions: List<String>,
    var pollImages: List<String>//user a cui appartiene il messaggio
)