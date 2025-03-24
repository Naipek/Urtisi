package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.urtisi.Menu.CallActivity
import com.example.urtisi.Menu.EmployeesActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Поиск виджета шестерёнки
        val settingsIcon = findViewById<ImageView>(R.id.icon4)

        // Установка обработчика нажатия
        settingsIcon.setOnClickListener {
            // Создать Intent для перехода на SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Расписание звонков
        val CardCall = findViewById<CardView>(R.id.CardCall)
        // Установить обработчик нажатия
        CardCall.setOnClickListener {
            val intent = Intent(this, CallActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Сотрудников
        val cardEmployees = findViewById<CardView>(R.id.CardEmployees)
        // Установить обработчик нажатия
        cardEmployees.setOnClickListener {
            val intent = Intent(this, EmployeesActivity::class.java)
            startActivity(intent)
        }
    }
}