package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.urtisi.MainActivity
import com.example.urtisi.Menu.Education.EducationBac1Activity
import com.example.urtisi.Menu.Education.EducationSpo1Activity
import com.example.urtisi.Menu.Education.EducationSpo2Activity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity

class EducationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_education)

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

        // Найти карточку СПО1
        val cardspo1 = findViewById<CardView>(R.id.Cardspo1)
        // Установить обработчик нажатия
        cardspo1.setOnClickListener {
            val intent = Intent(this, EducationSpo1Activity::class.java)
            startActivity(intent)
        }

        // Найти карточку СПО2
        val cardspo2 = findViewById<CardView>(R.id.Cardspo2)
        // Установить обработчик нажатия
        cardspo2.setOnClickListener {
            val intent = Intent(this, EducationSpo2Activity::class.java)
            startActivity(intent)
        }

        // Найти карточку БАК1
        val cardbac1 = findViewById<CardView>(R.id.Cardbac1)
        // Установить обработчик нажатия
        cardbac1.setOnClickListener {
            val intent = Intent(this, EducationBac1Activity::class.java)
            startActivity(intent)
        }

    }
}