package com.todolist.repository
import com.todolist.database.TodoDao
import com.todolist.database.TodoItem
import kotlinx.coroutines.flow.Flow

/**
 * [TodoRepository]
 * This class is used for creating the repository for getting the items and insert item into the database
 */
open class TodoRepository(private val todoDao: TodoDao) {
    /**
     * This is used to get the all items from the database
     */
    val allTodoItems: Flow<List<TodoItem>> = todoDao.getAllTodoItems()

    /**
     * This item is used to insert the item into the database
     */
    suspend fun insert(todoItem: TodoItem) {
        todoDao.insertTodoItem(todoItem)
    }
}

