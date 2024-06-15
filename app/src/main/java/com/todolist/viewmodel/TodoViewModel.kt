package com.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todolist.database.TodoItem
import com.todolist.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

/**
 * [TodoViewModel]
 * This model is used for writing the business logic and make calls to save data and get data from the database
 */
class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchTodoItems() {
        viewModelScope.launch {
            repository.allTodoItems.collect {
                _todoItems.value = it
            }
        }
    }

    fun addTodoItem(text: String) {
        if (text == "Error") {
            _error.value = "Failed to add TODO"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            repository.insert(TodoItem(text = text))
            kotlinx.coroutines.delay(3000)
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun filterTodoItems(query: String) {
        viewModelScope.launch {
            repository.allTodoItems.debounce(2000).collect { list ->
                _todoItems.value = list.filter { it.text.contains(query, ignoreCase = true) }
            }
        }
    }
}
