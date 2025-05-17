package com.example.urtisi.data

data class Schedule(
    val pairNumber: Int,
    val subject: String,
    val type: String, // лекция/практика
    val teacher: String,
    val location: String,
    val weekDay: String, // ПОНЕДЕЛЬНИК/ВТОРНИК
    val dateRange: String // например 17.02-22.02
) 