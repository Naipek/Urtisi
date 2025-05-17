package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.urtisi.FloorMapActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.ScheduleActivity
import com.example.urtisi.SettingsActivity

class ReferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_references)

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
        val icon2 = findViewById<ImageView>(R.id.icon2)
        icon2.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        //1 карточка
        val referencesCard1 = findViewById<LinearLayout>(R.id.referencesCard1)
        val answerText1 = findViewById<TextView>(R.id.answerText1)

        referencesCard1.setOnClickListener {
            val parent = referencesCard1.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText1.isGone) {
                answerText1.visibility = View.VISIBLE
            } else {
                answerText1.visibility = View.GONE
            }
        }

        //2 карточка
        val referencesCard2 = findViewById<LinearLayout>(R.id.referencesCard2)
        val answerText2 = findViewById<TextView>(R.id.answerText2)

        referencesCard2.setOnClickListener {
            val parent = referencesCard2.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText2.isGone) {
                answerText2.visibility = View.VISIBLE
            } else {
                answerText2.visibility = View.GONE
            }
        }

        //3 карточка
        val referencesCard3 = findViewById<LinearLayout>(R.id.referencesCard3)
        val answerText3 = findViewById<TextView>(R.id.answerText3)

        referencesCard3.setOnClickListener {
            val parent = referencesCard3.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText3.isGone) {
                answerText3.visibility = View.VISIBLE
            } else {
                answerText3.visibility = View.GONE
            }
        }

        //4 карточка
        val referencesCard4 = findViewById<LinearLayout>(R.id.referencesCard4)
        val answerText4 = findViewById<TextView>(R.id.answerText4)

        referencesCard4.setOnClickListener {
            val parent = referencesCard4.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText4.isGone) {
                answerText4.visibility = View.VISIBLE
            } else {
                answerText4.visibility = View.GONE
            }
        }
    }
}