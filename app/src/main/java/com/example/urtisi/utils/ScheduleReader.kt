package com.example.urtisi.utils

import android.content.Context
import android.util.Log
import com.example.urtisi.data.Schedule
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class ScheduleReader {
    companion object {
        private const val TAG = "ScheduleReader"

        // Структура расписания: день недели -> строка Excel (1-based)
        private val DAY_ROWS = mapOf(
            "ПОНЕДЕЛЬНИК" to 2,  // R2
            "ВТОРНИК" to 9,      // R9
            "СРЕДА" to 16,       // R16
            "ЧЕТВЕРГ" to 23,     // R23
            "ПЯТНИЦА" to 30,     // R30
            "СУББОТА" to 37      // R37
        )

        // Колонки для информации о парах
        private const val FIRST_WEEK_COLUMN = 2 // Начальная колонка C (индекс 2)
        private const val COLUMNS_PER_WEEK = 2 // Предмет и кабинет

        fun readSchedule(context: Context, fileName: String): List<Schedule> {
            val schedules = mutableListOf<Schedule>()
            
            try {
                Log.d(TAG, "Начинаем чтение файла $fileName")
                
                val assetFiles = context.assets.list("schedules")
                if (assetFiles?.contains(fileName) != true) {
                    Log.e(TAG, "Файл $fileName не найден в assets/schedules. Доступные файлы: ${assetFiles?.joinToString()}")
                    return schedules
                }

                val inputStream: InputStream = context.assets.open("schedules/$fileName")
                val workbook = WorkbookFactory.create(inputStream)
                val sheet = workbook.getSheetAt(0)

                // Определяем количество недель по заголовкам
                val headerRow = sheet.getRow(0)
                val weeks = mutableListOf<WeekInfo>()
                
                if (headerRow != null) {
                    var column = FIRST_WEEK_COLUMN
                    while (column < headerRow.lastCellNum) {
                        val headerCell = headerRow.getCell(column)
                        if (headerCell != null && !isCellEmpty(headerCell)) {
                            val dateRange = headerCell.toString().trim()
                            weeks.add(WeekInfo(
                                startColumn = column,
                                dateRange = dateRange
                            ))
                            Log.d(TAG, "Найдена неделя в колонке $column с датами: $dateRange")
                        }
                        column += COLUMNS_PER_WEEK
                    }
                }

                // Читаем расписание для каждого дня недели
                DAY_ROWS.forEach { (dayName, startRow) ->
                    Log.d(TAG, "Обработка $dayName, строка Excel: $startRow")

                    // Для каждого дня читаем 6 пар
                    for (pairOffset in 0..5) { // 6 пар, начиная с 0
                        val rowIndex = startRow + pairOffset - 1 // -1 потому что Excel строки 1-based, а POI 0-based
                        val row = sheet.getRow(rowIndex)
                        
                        if (row != null) {
                            weeks.forEachIndexed { weekIndex, weekInfo ->
                                val subjectCell = row.getCell(weekInfo.startColumn)
                                val locationCell = row.getCell(weekInfo.startColumn + 1)
                                
                                // Проверяем, что ячейка предмета не пустая и не содержит номер кабинета
                                if (subjectCell != null && !isCellEmpty(subjectCell)) {
                                    val subjectInfo = subjectCell.toString().trim()
                                    
                                    // Проверяем, что это не номер кабинета
                                    if (!isRoomNumber(subjectInfo)) {
                                        Log.d(TAG, "Найдена пара в ячейке R${rowIndex + 1}C${weekInfo.startColumn + 1}: $subjectInfo")

                                        // Определяем тип занятия и название предмета
                                        val (type, subject) = parseSubjectInfo(subjectInfo)

                                        // Получаем аудиторию
                                        val location = if (locationCell != null && !isCellEmpty(locationCell)) {
                                            locationCell.toString().trim()
                                        } else ""

                                        // Получаем преподавателя из названия предмета
                                        val teacher = extractTeacher(subject)

                                        if (subject.isNotEmpty()) {
                                            schedules.add(Schedule(
                                                pairNumber = pairOffset + 1,
                                                subject = cleanupSubject(subject),
                                                type = type,
                                                teacher = teacher,
                                                location = location,
                                                weekDay = dayName,
                                                dateRange = weekInfo.dateRange
                                            ))
                                            Log.d(TAG, "Добавлена пара для недели с датами ${weekInfo.dateRange}: $dayName, ${pairOffset + 1} - $subject")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                workbook.close()
                inputStream.close()
                
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при чтении расписания", e)
            }
            
            return schedules
        }

        private fun isCellEmpty(cell: org.apache.poi.ss.usermodel.Cell): Boolean {
            return when (cell.cellType) {
                CellType.BLANK -> true
                CellType.STRING -> cell.stringCellValue.trim().isEmpty()
                else -> false
            }
        }

        private fun isRoomNumber(text: String): Boolean {
            // Проверяем различные форматы номеров аудиторий
            return text.matches(Regex(
                ".*(?:" +
                    "рим|" +  // римская аудитория
                    "УК-?\\d+|" +  // УК или УК-1
                    "^\\d+[а-я]?$|" +  // просто номер (например, "115" или "115а")
                    "ауд\\.?\\s*\\d+[а-я]?|" +  // ауд. 115
                    "аудитория\\s*\\d+[а-я]?" +  // аудитория 115
                    ")",
                RegexOption.IGNORE_CASE
            ))
        }

        private fun parseSubjectInfo(subjectInfo: String): Pair<String, String> {
            // Если это номер аудитории, возвращаем пустые значения
            if (isRoomNumber(subjectInfo)) {
                return Pair("", "")
            }

            val type = when {
                subjectInfo.contains("лекция", ignoreCase = true) -> "лекция"
                subjectInfo.contains("практика", ignoreCase = true) -> "практика"
                subjectInfo.contains("лаб.раб.", ignoreCase = true) -> "лаб.раб."
                subjectInfo.contains("семинар", ignoreCase = true) -> "семинар"
                else -> ""
            }

            // Возвращаем оригинальный текст без изменений
            return Pair(type, subjectInfo)
        }

        private fun extractTeacher(subject: String): String {
            // Больше не используется
            return ""
        }

        private fun cleanupSubject(subject: String): String {
            // Возвращаем текст без изменений
            return subject
        }

        private data class WeekInfo(
            val startColumn: Int,
            val dateRange: String
        )
    }
} 