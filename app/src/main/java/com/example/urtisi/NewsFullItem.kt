package com.example.urtisi

data class NewsFullItem(
    val url: String,           // Уникальный ключ
    val title: String,
    val date: String,
    val content: String,
    val imageUrl: String
)
