package com.example.urtisi.Settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity
import androidx.core.content.edit
import com.example.urtisi.FloorMapActivity

class ThemeSelectionActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_theme_selection)

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)

        // Обработчики для карточек выбора темы
        findViewById<CardView>(R.id.cardLight).setOnClickListener {
            saveTheme(AppCompatDelegate.MODE_NIGHT_NO)
        }
        findViewById<CardView>(R.id.cardDark).setOnClickListener {
            saveTheme(AppCompatDelegate.MODE_NIGHT_YES)
        }
        findViewById<CardView>(R.id.cardSystem).setOnClickListener {
            saveTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        // Обработчики для нижней панели
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        val icon3 = findViewById<ImageView>(R.id.icon3)
        icon3.setOnClickListener {
            val intent = Intent(this, FloorMapActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

            private fun saveTheme(themeMode: Int) {
                sharedPreferences.edit {
                    putInt("theme_mode", themeMode)
                    apply()
                }
                AppCompatDelegate.setDefaultNightMode(themeMode)
                recreate()
            }
}


