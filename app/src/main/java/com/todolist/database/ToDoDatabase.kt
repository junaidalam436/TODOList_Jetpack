package com.todolist.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * [TodoDatabase]
 * This class is used for database
 */
@Database(entities = [TodoItem::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    /**
     * This function is used for getting the dao reference
     */
    abstract fun todoDao(): TodoDao
}
