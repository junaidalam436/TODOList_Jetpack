package com.todolist.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.todolist.factory.TodoViewModelFactory
import com.todolist.repository.TodoRepository
import com.todolist.viewmodel.TodoViewModel

/**
 * [ToDoListScreen]
 * This class is used to design the todo list screen
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(navController: NavController, repository: TodoRepository) {

    val viewModel: TodoViewModel =
        viewModel(factory = TodoViewModelFactory(repository = repository))


    val todoItems by viewModel.todoItems.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        viewModel.fetchTodoItems()
    }

    LaunchedEffect(searchQuery) {
        viewModel.filterTodoItems(searchQuery.text)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TODO List") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("details") }) {
                Icon(Icons.Default.Add, contentDescription = "Add TODO")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (searchQuery.text.isEmpty()) {
                        Text("Tap to search TODOs...")
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (todoItems.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Press the + button to add a TODO item")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(todoItems) { item ->
                        Text(text = item.text, modifier = Modifier.padding(8.dp))
                        Divider()
                    }
                }
            }
        }
    }

    if (error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(error!!) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
