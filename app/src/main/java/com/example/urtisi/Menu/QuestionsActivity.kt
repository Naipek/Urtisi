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
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity
import androidx.core.view.isGone

class QuestionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_questions)

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

        //Анимация текста
        //1 карточка
        val questionCard1 = findViewById<LinearLayout>(R.id.questionCard1)
        val answerText1 = findViewById<TextView>(R.id.answerText1)

        questionCard1.setOnClickListener {
            val parent = questionCard1.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText1.isGone) {
                answerText1.visibility = View.VISIBLE
            } else {
                answerText1.visibility = View.GONE
            }
        }

        //2 карточка
        val questionCard2 = findViewById<LinearLayout>(R.id.questionCard2)
        val answerText2 = findViewById<TextView>(R.id.answerText2)

        questionCard2.setOnClickListener {
            val parent = questionCard2.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText2.isGone) {
                answerText2.visibility = View.VISIBLE
            } else {
                answerText2.visibility = View.GONE
            }
        }

        //3 карточка
        val questionCard3 = findViewById<LinearLayout>(R.id.questionCard3)
        val answerText3 = findViewById<TextView>(R.id.answerText3)

        questionCard3.setOnClickListener {
            val parent = questionCard3.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText3.isGone) {
                answerText3.visibility = View.VISIBLE
            } else {
                answerText3.visibility = View.GONE
            }
        }

        //4 карточка
        val questionCard4 = findViewById<LinearLayout>(R.id.questionCard4)
        val answerText4 = findViewById<TextView>(R.id.answerText4)

        questionCard4.setOnClickListener {
            val parent = questionCard4.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText4.isGone) {
                answerText4.visibility = View.VISIBLE
            } else {
                answerText4.visibility = View.GONE
            }
        }

        //5 карточка
        val questionCard5 = findViewById<LinearLayout>(R.id.questionCard5)
        val answerText5 = findViewById<TextView>(R.id.answerText5)

        questionCard5.setOnClickListener {
            val parent = questionCard5.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText5.isGone) {
                answerText5.visibility = View.VISIBLE
            } else {
                answerText5.visibility = View.GONE
            }
        }

        //6 карточка
        val questionCard6 = findViewById<LinearLayout>(R.id.questionCard6)
        val answerText6 = findViewById<TextView>(R.id.answerText6)

        questionCard6.setOnClickListener {
            val parent = questionCard6.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText6.isGone) {
                answerText6.visibility = View.VISIBLE
            } else {
                answerText6.visibility = View.GONE
            }
        }

        //7 карточка
        val questionCard7 = findViewById<LinearLayout>(R.id.questionCard7)
        val answerText7 = findViewById<TextView>(R.id.answerText7)

        questionCard7.setOnClickListener {
            val parent = questionCard6.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText7.isGone) {
                answerText7.visibility = View.VISIBLE
            } else {
                answerText7.visibility = View.GONE
            }
        }

        //8 карточка
        val questionCard8 = findViewById<LinearLayout>(R.id.questionCard8)
        val answerText8 = findViewById<TextView>(R.id.answerText8)

        questionCard8.setOnClickListener {
            val parent = questionCard6.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText8.isGone) {
                answerText8.visibility = View.VISIBLE
            } else {
                answerText8.visibility = View.GONE
            }
        }

        //9 карточка
        val questionCard9 = findViewById<LinearLayout>(R.id.questionCard9)
        val answerText9 = findViewById<TextView>(R.id.answerText9)

        questionCard9.setOnClickListener {
            val parent = questionCard6.parent as ViewGroup
            TransitionManager.beginDelayedTransition(parent, AutoTransition())

            if (answerText9.isGone) {
                answerText9.visibility = View.VISIBLE
            } else {
                answerText9.visibility = View.GONE
            }
        }
    }
}