package com.example.hwtasks.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface UserEntryDao {
    @Query("SELECT * FROM user_entries ORDER BY dateCreated DESC, id DESC")
    fun getAll(): Flow<List<UserEntry>>

    @Query("SELECT * FROM user_entries WHERE dateCreated = :onDate ORDER BY id DESC")
    fun getByDate(onDate: LocalDate): Flow<List<UserEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: UserEntry): Long

    @Delete
    suspend fun delete(entry: UserEntry)

    @Query("""
        SELECT 
          u.id AS userId,
          COUNT(CASE WHEN t.completionStatus = 1 THEN 1 END) AS completed
        FROM user_entries u
        LEFT JOIN task_entries t ON t.userId = u.id
        WHERE u.id = :userId
        GROUP BY u.id
    """)
    fun userCompletionStats(userId: Long): LiveData<UserCompletionStats>

    data class UserCompletionStats(
        val userId: Long,
        val completed: Int
    )
    @Query("""
        UPDATE user_entries
        SET numOfCompletedTasks = (
            SELECT COUNT(*)
            FROM task_entries
            WHERE userId = :userId AND completionStatus = 1
        )
        WHERE id = :userId
    """)
    suspend fun refreshCompletedCount(userId: Long)
}