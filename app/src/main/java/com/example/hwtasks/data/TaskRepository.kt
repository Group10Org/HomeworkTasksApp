package com.example.hwtasks.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    val totalCount = dao.totalCountFlow()
    val completedCount = dao.completedCountFlow()
    val incompleteCount = dao.incompleteCountFlow()
    val currentTime = System.currentTimeMillis()
    // Function that returns the appropriate Flow based on incomplete functions
    fun getIncompleteTasks(sortByPriority: Boolean): Flow<List<TaskEntity>> {
        return if (sortByPriority) {
            dao.incompleteTasksByPriority()
        } else {
            dao.incompleteTasksByDueDate()
        }
    }

    //returns flow based on completed functions
    fun getCompletedTasks(sortByPriority: Boolean): Flow<List<TaskEntity>> {
        return if (sortByPriority) {
            dao.completedTasksByPriority()
        } else {
            dao.completedTasksByDueDate()
        }
    }

    val pastDueCount: Flow<Int>
        get() = dao.getPastDueCount(System.currentTimeMillis())
    suspend fun add(
        title: String,
        desc: String?,
        className: String?,
        priority: Int,
        dueAt: Long?
    ) = dao.upsert(
        TaskEntity(
            title = title,
            description = desc,
            className = className,
            priority = priority,
            dueAt = dueAt
        )
    )

    suspend fun toggle(task: TaskEntity) =
        dao.update(task.copy(completionStatus = !task.completionStatus))

    suspend fun remove(task: TaskEntity) = dao.delete(task)

    suspend fun getTask(id: Long): TaskEntity? =
        dao.getById(id)
}