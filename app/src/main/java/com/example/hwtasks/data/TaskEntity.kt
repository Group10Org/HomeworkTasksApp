package com.example.hwtasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String? = null,
    val className: String? = null,
    val priority: Int = 3,
    val createdAt: Long = System.currentTimeMillis() / 1000,
    val dueAt: Long? = null,
    val imageUri: String? = null,
    val completionStatus: Boolean = false
)

