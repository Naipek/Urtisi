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

        // Найти карточку Об институте
        val cardInstiute = findViewById<CardView>(R.id.CardInstitute)
        // Установить обработчик нажатия
        cardInstiute.setOnClickListener {
            val intent = Intent(this, InstituteActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Образование
        val cardEducation = findViewById<CardView>(R.id.CardEducation)
        // Установить обработчик нажатия
        cardEducation.setOnClickListener {
            val intent = Intent(this, EducationActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Основное
        val cardBasic = findViewById<CardView>(R.id.CardBasic)
        // Установить обработчик нажатия
        cardBasic.setOnClickListener {
            val intent = Intent(this, BasicActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Кафедры
        val cardCafedra = findViewById<CardView>(R.id.CardCafedra)
        // Установить обработчик нажатия
        cardCafedra.setOnClickListener {
            val intent = Intent(this, CafedraActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Факты
        val cardFacts = findViewById<CardView>(R.id.CardFacts)
        // Установить обработчик нажатия
        cardFacts.setOnClickListener {
            val intent = Intent(this, FactsActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Вопросы
        val cardQuestions = findViewById<CardView>(R.id.CardQuestions)
        // Установить обработчик нажатия
        cardQuestions.setOnClickListener {
            val intent = Intent(this, QuestionsActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Справки
        val cardReferences = findViewById<CardView>(R.id.CardReferences)
        // Установить обработчик нажатия
        cardReferences.setOnClickListener {
            val intent = Intent(this, ReferencesActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Ведомость
        val cardStatement = findViewById<CardView>(R.id.CardStatement)
        // Установить обработчик нажатия
        cardStatement.setOnClickListener {
            val intent = Intent(this, StatementActivity::class.java)
            startActivity(intent)
        }
    }
}