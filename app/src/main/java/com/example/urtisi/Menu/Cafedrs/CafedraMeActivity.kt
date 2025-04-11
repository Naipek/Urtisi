package com.example.urtisi.Menu.Cafedrs

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.urtisi.MainActivity
import com.example.urtisi.Menu.Education.EducationSpo2Activity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity

class CafedraMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cafedra_me)

        // Обработчики для нижней панели
        val icon1 = findViewById<ImageView>(R.id.icon1)
        icon1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val icon4 = findViewById<ImageView>(R.id.icon4)
        icon4.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Многоканальная электросвязь
        val cardMe = findViewById<CardView>(R.id.CardMe)
        // Установить обработчик нажатия
        cardMe.setOnClickListener {
            val intent = Intent(this, CafedraMeActivity::class.java)
            startActivity(intent)
        }
    }
}