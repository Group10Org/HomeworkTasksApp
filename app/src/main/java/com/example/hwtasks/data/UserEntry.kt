package com.example.hwtasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user_entries")
data class UserEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateCreated: LocalDate,
    val username: String?,
    val password: String?,
    val numOfCompletedTasks: Int,
    val privacySetting: Boolean
)

