package com.example.urtisi.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.urtisi.R
import com.example.urtisi.data.Schedule

class ScheduleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<ScheduleItem>()

    private val weekDayOrder = mapOf(
        "ПОНЕДЕЛЬНИК" to 0,
        "ВТОРНИК" to 1,
        "СРЕДА" to 2,
        "ЧЕТВЕРГ" to 3,
        "ПЯТНИЦА" to 4,
        "СУББОТА" to 5
    )

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_SCHEDULE = 1

        // Цвета для разных типов занятий
        private const val LECTURE_COLOR = "#E8F5E9" // светло-зеленый
        private const val PRACTICE_COLOR = "#F3E5F5" // светло-фиолетовый
        private const val LAB_COLOR = "#E3F2FD" // светло-голубой
        private const val SEMINAR_COLOR = "#FFF3E0" // светло-оранжевый
    }

    fun updateSchedules(newSchedules: List<Schedule>) {
        items.clear()
        
        // Сортируем и группируем расписание по дням недели
        val sortedSchedules = newSchedules
            // Сначала группируем по диапазону дат
            .groupBy { it.dateRange }
            // Сортируем группы по начальной дате
            .toSortedMap(compareBy { dateRange ->
                try {
                    // Очищаем строку от лишних точек
                    val cleanDateRange = dateRange.replace("..", ".").replace(".-", "-")
                    // Берем первую дату из диапазона (до дефиса)
                    val startDate = cleanDateRange.split("-")[0].trim()
                    // Разбиваем на день и месяц
                    val (day, month) = startDate.split(".")
                        .filter { it.isNotEmpty() }
                        .map { it.toInt() }
                    // Создаем числовое значение для сортировки
                    month * 100 + day
                } catch (e: Exception) {
                    0 // В случае ошибки помещаем в начало списка
                }
            })
            // Для каждой недели сортируем по дням
            .flatMap { (_, schedules) ->
                schedules
                    .groupBy { it.weekDay }
                    .toSortedMap(compareBy { weekDayOrder[it] ?: -1 })
                    .flatMap { (day, daySchedules) ->
                        listOf(ScheduleItem.Header(day)) + 
                        daySchedules.sortedBy { it.pairNumber }
                            .map { ScheduleItem.ScheduleEntry(it) }
                    }
            }

        items.addAll(sortedSchedules)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ScheduleItem.Header -> TYPE_HEADER
            is ScheduleItem.ScheduleEntry -> TYPE_SCHEDULE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_day_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_SCHEDULE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schedule, parent, false)
                ScheduleViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ScheduleItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ScheduleItem.ScheduleEntry -> (holder as ScheduleViewHolder).bind(item.schedule)
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayHeaderTextView: TextView = itemView.findViewById(R.id.dayHeaderTextView)

        fun bind(header: ScheduleItem.Header) {
            dayHeaderTextView.text = header.dayOfWeek
        }
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val pairNumberTextView: TextView = itemView.findViewById(R.id.pairNumberTextView)
        private val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        private val teacherTextView: TextView = itemView.findViewById(R.id.teacherTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)

        fun bind(schedule: Schedule) {
            // Устанавливаем цвет фона в зависимости от типа занятия
            cardView.setCardBackgroundColor(when (schedule.type.lowercase()) {
                "лекция" -> Color.parseColor(LECTURE_COLOR)
                "практика" -> Color.parseColor(PRACTICE_COLOR)
                "лаб.раб." -> Color.parseColor(LAB_COLOR)
                "семинар" -> Color.parseColor(SEMINAR_COLOR)
                else -> Color.WHITE
            })

            pairNumberTextView.text = schedule.pairNumber.toString()
            subjectTextView.text = schedule.subject
            
            // Скрываем поле с преподавателем, так как информация уже есть в subject
            teacherTextView.visibility = View.GONE
            
            if (schedule.location.isNotEmpty()) {
                locationTextView.visibility = View.VISIBLE
                locationTextView.text = "Аудитория: ${schedule.location}"
            } else {
                locationTextView.visibility = View.GONE
            }
        }
    }
}

sealed class ScheduleItem {
    data class Header(val dayOfWeek: String) : ScheduleItem()
    data class ScheduleEntry(val schedule: Schedule) : ScheduleItem()
} 