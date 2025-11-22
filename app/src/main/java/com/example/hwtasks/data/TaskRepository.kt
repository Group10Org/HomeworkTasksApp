package com.example.hwtasks.data

class TaskRepository(private val dao: TaskDao) {
    val tasks = dao.tasks()
    val totalCount = dao.totalCountFlow()
    val completedCount = dao.completedCountFlow()
    val incompleteCount = dao.incompleteCountFlow()

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
