package com.example.urtisi

import java.net.URL

data class NewsItem(
    val title: String,
    val date: String,
    val link: String,
    val imageURL: String = ""
)
