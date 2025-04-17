package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.edit
import com.example.urtisi.Settings.HelpActivity
import com.example.urtisi.Settings.LinkActivity
import com.example.urtisi.Settings.ThemeSelectionActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)


        // Найти виджет шестерёнки
        val settingsIcon = findViewById<ImageView>(R.id.icon1)
        // Установить обработчик нажатия
        settingsIcon.setOnClickListener {
            Log.d("SettingsActivity", "Иконка настроек нажата")
            // Создать Intent для перехода на MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку выбора темы
        val cardThemeSelection = findViewById<CardView>(R.id.CardThemeSelection)
        // Установить обработчик нажатия
        cardThemeSelection.setOnClickListener {
            val intent = Intent(this, ThemeSelectionActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку справки
        val cardhelp = findViewById<CardView>(R.id.cardHelp)
        // Установить обработчик нажатия
        cardhelp.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку ссылок
        val cardlink = findViewById<CardView>(R.id.cardLink)
        // Установить обработчик нажатия
        cardlink.setOnClickListener {
            val intent = Intent(this, LinkActivity::class.java)
            startActivity(intent)
        }

    }
}