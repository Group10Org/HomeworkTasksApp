package com.example.hwtasks.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class UserRepository(private val dao: UserEntryDao) {
    fun entries(): Flow<List<UserEntry>> = dao.getAll()
    fun entriesFor(date: LocalDate): Flow<List<UserEntry>> = dao.getByDate(date)
    suspend fun upsert(entry: UserEntry): Long = dao.upsert(entry)
    suspend fun delete(entry: UserEntry) = dao.delete(entry)

    fun userCompletionStats(userId: Long): LiveData<UserEntryDao.UserCompletionStats> =
        dao.userCompletionStats(userId)

    suspend fun refreshCompletedCount(userId: Long) =
        dao.refreshCompletedCount(userId)
}