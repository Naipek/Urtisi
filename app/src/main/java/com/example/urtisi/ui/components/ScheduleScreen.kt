package com.example.urtisi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.urtisi.data.Schedule
import com.example.urtisi.utils.ScheduleReader

@Composable
fun ScheduleScreen() {
    val context = LocalContext.current
    var schedules by remember { mutableStateOf<List<Schedule>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedDateRange by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            schedules = ScheduleReader.readSchedule(context, "ИТ-41.xlsx")
            selectedDateRange = schedules.firstOrNull()?.dateRange
        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Расписание",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Выбор диапазона дат
        if (schedules.isNotEmpty()) {
            val dateRanges = schedules.map { it.dateRange }.distinct()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dateRanges.forEach { dateRange ->
                    FilterChip(
                        selected = dateRange == selectedDateRange,
                        onClick = { selectedDateRange = dateRange },
                        label = { Text(dateRange) }
                    )
                }
            }
        }

        if (error != null) {
            Text(
                text = "Ошибка загрузки расписания: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filteredSchedules = schedules.filter { it.dateRange == selectedDateRange }
            val groupedByDay = filteredSchedules.groupBy { it.weekDay }

            groupedByDay.forEach { (day, daySchedules) ->
                item {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(daySchedules.sortedBy { it.pairNumber }) { schedule ->
                    ScheduleItem(schedule = schedule)
                }
            }
        }
    }
}

@Composable
fun ScheduleItem(schedule: Schedule) {
    val backgroundColor = when (schedule.type.lowercase()) {
        "лекция" -> Color(0xFFE8F5E9) // Светло-зеленый
        "практика" -> Color(0xFFF3E5F5) // Светло-фиолетовый
        else -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Номер пары
            Text(
                text = "${schedule.pairNumber}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )

            // Информация о паре
            Column {
                Text(
                    text = schedule.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${schedule.type}: ${schedule.teacher}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (schedule.location.isNotEmpty()) {
                    Text(
                        text = "Аудитория: ${schedule.location}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
} 