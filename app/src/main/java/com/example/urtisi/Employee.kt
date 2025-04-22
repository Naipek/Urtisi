package com.example.urtisi

import java.io.Serializable

data class Employee(
    val name: String,          // ФИО сотрудника
    val degree: String,       // Ученая степень
    val title: String,        // Ученое звание
    val position: String      // Должность
) : Serializable