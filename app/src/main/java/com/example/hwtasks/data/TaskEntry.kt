package com.example.hwtasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "task_entries")
data class TaskEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userID: Long,
    val dateCreated: LocalDate,
    val dueDate: LocalDate?,
    val priority: Int,
    val title: String,
    val description: String?,
    val completionStatus: Int
)