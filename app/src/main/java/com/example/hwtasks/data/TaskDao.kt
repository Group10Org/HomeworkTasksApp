package com.example.hwtasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query(
        """
        SELECT * FROM tasks
        ORDER BY completionStatus ASC,
               CASE WHEN dueAt IS NULL THEN 1 ELSE 0 END,
               dueAt ASC
    """
    )
    fun tasks(): Flow<List<TaskEntity>>

    // 🔹 Used by DetailFragment
    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TaskEntity?

    @Query("SELECT COUNT(*) FROM tasks")
    fun totalCountFlow(): Flow<Long>

    @Query("SELECT COUNT(*) FROM tasks WHERE completionStatus = 1")
    fun completedCountFlow(): Flow<Long>

    @Query("SELECT COUNT(*) FROM tasks WHERE completionStatus = 0")
    fun incompleteCountFlow(): Flow<Long>
}
