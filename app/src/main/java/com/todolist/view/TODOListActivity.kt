package com.todolist.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.todolist.database.TodoDatabase
import com.todolist.repository.TodoRepository

/**
 * [TODOListActivity]
 * This is the main activity class
 */
class TODOListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java, "todo-database"
        ).build()
        val repository = TodoRepository(db.todoDao())
        setContent {
            NavGraph(repository = repository)
        }
    }
}
