package com.todolist.view
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.todolist.factory.TodoViewModelFactory
import com.todolist.repository.TodoRepository
import com.todolist.viewmodel.TodoViewModel

/**
 * [AddToDoScreen]
 * This class is used to design the Add todo screen
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddToDoScreen(navController: NavController, repository: TodoRepository) {
    val context = LocalContext.current
    val viewModel: TodoViewModel =
        viewModel(factory = TodoViewModelFactory(repository = repository))
    var text by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(context, "Error!! Data is not saved", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add TODO") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter TODO") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                viewModel.addTodoItem(text)
                if (text != "Error") {
                    navController.popBackStack()
                }
            }) {
                Text("Add TODO")
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
