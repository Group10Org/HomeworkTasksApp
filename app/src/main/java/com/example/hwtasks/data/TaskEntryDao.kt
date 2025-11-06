package com.example.hwtasks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskEntryDao {
    @Query("SELECT * FROM task_entries ORDER BY dateCreated DESC, id DESC")
    fun getAll(): Flow<List<TaskEntry>>

    @Query("SELECT * FROM task_entries WHERE dateCreated = :onDate ORDER BY id DESC")
    fun getByDate(onDate: LocalDate): Flow<List<TaskEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: TaskEntry): Long

    @Delete
    suspend fun delete(entry: TaskEntry)

}