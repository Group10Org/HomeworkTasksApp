package com.example.hwtasks.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class TaskRepository(private val dao: TaskEntryDao) {
    fun entries(): Flow<List<TaskEntry>> = dao.getAll()
    fun entriesFor(date: LocalDate): Flow<List<TaskEntry>> = dao.getByDate(date)
    suspend fun upsert(entry: TaskEntry): Long = dao.upsert(entry)
    suspend fun delete(entry: TaskEntry) = dao.delete(entry)
}