package com.todolist.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * [TodoDao]
 * This interface contains methods to perform the operation on the database
 */
@Dao
interface TodoDao {
    /**
     * This function is used to get the all items from the database
     */
    @Query("SELECT * FROM todo_items")
    fun getAllTodoItems(): Flow<List<TodoItem>>

    /**
     * This function is used to insert the item into the database
     * @param todoItem : Todo item object
     */
    @Insert
    suspend fun insertTodoItem(todoItem: TodoItem)
}
