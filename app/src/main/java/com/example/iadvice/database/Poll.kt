package com.example.iadvice.database

data class Poll (
    var pollQuestions: String,
    var pollImages: String //user a cui appartiene il messaggio
)
{
    constructor(): this("","")  //TODO va lasciato???
}