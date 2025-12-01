package com.example.hwtasks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskEntity): Long

    @Delete
    suspend fun delete(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    // Incomplete tasks sorted by due date
    @Query("""
        SELECT * FROM tasks
        WHERE completionStatus = 0
        ORDER BY CASE WHEN dueAt IS NULL THEN 1 ELSE 0 END,
               dueAt ASC
    """)
    fun incompleteTasksByDueDate(): Flow<List<TaskEntity>>

    // Incomplete tasks sorted by priority
    @Query("""
        SELECT * FROM tasks
        WHERE completionStatus = 0
        ORDER BY priority ASC,
               createdAt DESC
    """)
    fun incompleteTasksByPriority(): Flow<List<TaskEntity>>

    // Completed tasks sorted by due date
    @Query("""
        SELECT * FROM tasks
        WHERE completionStatus = 1
        ORDER BY CASE WHEN dueAt IS NULL THEN 1 ELSE 0 END,
               dueAt ASC
    """)
    fun completedTasksByDueDate(): Flow<List<TaskEntity>>

    // Completed tasks sorted by priority
    @Query("""
        SELECT * FROM tasks
        WHERE completionStatus = 1
        ORDER BY priority DESC,
               createdAt DESC
    """)
    fun completedTasksByPriority(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TaskEntity?

    @Query("SELECT COUNT(*) FROM tasks")
    fun totalCountFlow(): Flow<Long>

    @Query("SELECT COUNT(*) FROM tasks WHERE completionStatus = 1")
    fun completedCountFlow(): Flow<Long>

    @Query("SELECT COUNT(*) FROM tasks WHERE completionStatus = 0")
    fun incompleteCountFlow(): Flow<Long>

    @Query("SELECT COUNT(*) FROM tasks WHERE dueAt < :now AND completionStatus = 0")
    fun getPastDueCount(now: Long): Flow<Int>


    /*@Query("SELECT COUNT(*) FROM tasks WHERE dueAt < curr") */

}