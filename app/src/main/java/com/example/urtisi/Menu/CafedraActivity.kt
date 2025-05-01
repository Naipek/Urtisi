package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.urtisi.FloorMapActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.Menu.Cafedrs.CafedraEsActivity
import com.example.urtisi.Menu.Cafedrs.CafedraIstActivity
import com.example.urtisi.Menu.Cafedrs.CafedraItimsActivity
import com.example.urtisi.Menu.Cafedrs.CafedraMeActivity
import com.example.urtisi.Menu.Cafedrs.CafedraVmifActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity

class CafedraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_cafedra)

        // Обработчики для нижней панели
        val icon1 = findViewById<ImageView>(R.id.icon1)
        icon1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val icon3 = findViewById<ImageView>(R.id.icon3)
        icon3.setOnClickListener {
            val intent = Intent(this, FloorMapActivity::class.java)
            startActivity(intent)
        }
        val icon4 = findViewById<ImageView>(R.id.icon4)
        icon4.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Кафедры МЭ
        val cardMe = findViewById<CardView>(R.id.CardMe)
        // Установить обработчик нажатия
        cardMe.setOnClickListener {
            val intent = Intent(this, CafedraMeActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Кафедры Итимс
        val cardItims = findViewById<CardView>(R.id.CardItims)
        // Установить обработчик нажатия
        cardItims.setOnClickListener {
            val intent = Intent(this, CafedraItimsActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Кафедры Вмиф
        val cardVmif = findViewById<CardView>(R.id.CardVmif)
        // Установить обработчик нажатия
        cardVmif.setOnClickListener {
            val intent = Intent(this, CafedraVmifActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Кафедры Ист
        val cardIst = findViewById<CardView>(R.id.CardIst)
        // Установить обработчик нажатия
        cardIst.setOnClickListener {
            val intent = Intent(this, CafedraIstActivity::class.java)
            startActivity(intent)
        }

        // Найти карточку Кафедры Эс
        val cardEs = findViewById<CardView>(R.id.CardEs)
        // Установить обработчик нажатия
        cardEs.setOnClickListener {
            val intent = Intent(this, CafedraEsActivity::class.java)
            startActivity(intent)
        }
    }
}