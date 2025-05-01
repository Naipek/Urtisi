package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmployeeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)

        // Подключение нижней панели
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

        // Получаем данные о сотруднике
        val employee = intent.getSerializableExtra("employee") as? Employee ?: run {
            showError("Не удалось получить данные сотрудника")
            return
        }

        // Отображаем данные
        displayEmployeeData(employee)
    }

    private fun displayEmployeeData(employee: Employee) {
        try {
            // Устанавливаем значения в текстовые поля
            setText(R.id.name, employee.name)
            setText(R.id.degree, employee.degree)
            setText(R.id.title, employee.title)
            setText(R.id.position, employee.position)
        } catch (e: Exception) {
            showError("Ошибка при отображении данных")
            Log.e("EmployeeDetail", "Ошибка отображения данных", e)
        }
    }

    private fun setText(viewId: Int, text: String) {
        findViewById<TextView>(viewId)?.text = text
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }
}