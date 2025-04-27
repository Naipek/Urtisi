package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.urtisi.Menu.BasicActivity
import com.example.urtisi.Menu.CafedraActivity
import com.example.urtisi.Menu.CallActivity
import com.example.urtisi.Menu.EducationActivity
import com.example.urtisi.Menu.EmployeesActivity
import com.example.urtisi.Menu.FactsActivity
import com.example.urtisi.Menu.InstituteActivity
import com.example.urtisi.Menu.QuestionsActivity
import com.example.urtisi.Menu.ReferencesActivity
import com.example.urtisi.Menu.StatementActivity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.urtisi.Menu.NewsActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Устанавливаем сохранённую тему до super.onCreate и setContentView
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val savedTheme = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Нижняя панель — переход в настройки
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        // Нижняя панель — переход в карта
        findViewById<ImageView>(R.id.icon3).setOnClickListener {
            startActivity(Intent(this, FloorMapActivity::class.java))
        }

        // Карточки и переходы
        findViewById<CardView>(R.id.CardCall).setOnClickListener {
            startActivity(Intent(this, CallActivity::class.java))
        }
        findViewById<CardView>(R.id.CardEmployees).setOnClickListener {
            startActivity(Intent(this, StaffActivity::class.java))
        }
        findViewById<CardView>(R.id.CardInstitute).setOnClickListener {
            startActivity(Intent(this, InstituteActivity::class.java))
        }
        findViewById<CardView>(R.id.CardEducation).setOnClickListener {
            startActivity(Intent(this, EducationActivity::class.java))
        }
        findViewById<CardView>(R.id.CardBasic).setOnClickListener {
            startActivity(Intent(this, BasicActivity::class.java))
        }
        findViewById<CardView>(R.id.CardCafedra).setOnClickListener {
            startActivity(Intent(this, CafedraActivity::class.java))
        }
        findViewById<CardView>(R.id.CardFacts).setOnClickListener {
            startActivity(Intent(this, FactsActivity::class.java))
        }
        findViewById<CardView>(R.id.CardQuestions).setOnClickListener {
            startActivity(Intent(this, QuestionsActivity::class.java))
        }
        findViewById<CardView>(R.id.CardReferences).setOnClickListener {
            startActivity(Intent(this, ReferencesActivity::class.java))
        }
        findViewById<CardView>(R.id.CardStatement).setOnClickListener {
            startActivity(Intent(this, StatementActivity::class.java))
        }
        findViewById<CardView>(R.id.CardNews).setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }
    }
}