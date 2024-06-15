package com.todolist.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todolist.repository.TodoRepository

/**
 * [NavGraph]
 * This is used for nav graph for moving one screen to another screen
 */
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    repository: TodoRepository
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            ToDoListScreen(
                navController = navController,
                repository = repository
            )
        }
        composable("details") {
            AddToDoScreen(
                navController = navController,
                repository = repository
            )
        }
    }
}
