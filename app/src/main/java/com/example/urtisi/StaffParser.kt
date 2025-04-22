package com.example.urtisi

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object StaffParser {

    suspend fun parseStaff(url: String): List<Employee> {
        return try {
            val doc = Jsoup.connect(url).get()
            parseDocument(doc)
        } catch (e: Exception) {
            Log.e("StaffParser", "Ошибка при парсинге сотрудников", e)
            emptyList()
        }
    }

    private fun parseDocument(doc: Document): List<Employee> {
        val staffList = mutableListOf<Employee>()

        // Ищем целевую таблицу с сотрудниками по указанному пути
        val table = doc.select("body > center > div > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > table > tbody > tr:nth-child(1) > td:nth-child(3) > div > table").first()

        // Если таблица не найдена - возвращаем пустой список
        if (table == null) {
            Log.w("StaffParser", "Таблица сотрудников не найдена")
            return emptyList()
        }

        // Обрабатываем каждую строку таблицы
        for (row in table.select("tr")) {
            try {
                // Пропускаем строки без верхней границы (заголовки и пустые строки)
                if (!row.attr("style").contains("border-top")) continue

                val cells = row.select("td")
                // Проверяем, что в строке достаточно ячеек
                if (cells.size < 5) continue

                // Извлекаем данные из ячеек
                val name = cells[0].text().trim()
                val degree = cells[2].text().trim()
                val title = cells[3].text().trim()
                val position = cells[4].text().trim()

                // Создаем объект сотрудника и добавляем в список
                staffList.add(
                    Employee(
                        name = name,
                        degree = if (degree.isEmpty()) "Нет данных" else degree,
                        title = if (title.isEmpty()) "Нет данных" else title,
                        position = if (position.isEmpty()) "Нет данных" else position
                    )
                )
            } catch (e: Exception) {
                Log.e("StaffParser", "Ошибка при обработке строки таблицы", e)
            }
        }

        return staffList
    }
}