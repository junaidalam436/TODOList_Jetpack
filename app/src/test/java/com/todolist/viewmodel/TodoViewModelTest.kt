import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.todolist.database.TodoItem
import com.todolist.repository.TodoRepository
import com.todolist.viewmodel.TodoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * [TodoViewModelTest]
 * Added the test class for Todo view model test cases
 */
@ExperimentalCoroutinesApi
class TodoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TodoViewModel
    private lateinit var repository: TodoRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = TodoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchTodoItems`() = runTest {
        val todoItems = listOf(
            TodoItem(id = 1, text = "First TODO"),
            TodoItem(id = 2, text = "Second TODO")
        )
        coEvery { repository.allTodoItems } returns flowOf(todoItems)

        viewModel.fetchTodoItems()
        advanceUntilIdle()
        assertEquals(todoItems, viewModel.todoItems.value)
    }

    @Test
    fun `test addTodoItem success`() = runTest {
        val newItemText = "New TODO"
        coEvery { repository.insert(any()) } returns Unit
        coEvery { repository.allTodoItems } returns MutableStateFlow(emptyList())

        viewModel.addTodoItem(newItemText)
        advanceTimeBy(3000)
        advanceUntilIdle()

        coVerify { repository.insert(TodoItem(text = newItemText)) }
        assertEquals(false, viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `test addTodoItem error`() = runTest {
        val errorItemText = "Error"

        viewModel.addTodoItem(errorItemText)
        advanceUntilIdle()

        assertEquals("Failed to add TODO", viewModel.error.value)
    }

    @Test
    fun `test filterTodoItems`() = runTest {
        val todoItems = listOf(
            TodoItem(id = 1, text = "First TODO"),
            TodoItem(id = 2, text = "Second TODO"),
            TodoItem(id = 3, text = "Another TODO")
        )
        coEvery { repository.allTodoItems } returns MutableStateFlow(todoItems)

        viewModel.filterTodoItems("First")
        advanceTimeBy(2000)
        advanceUntilIdle()

        assertEquals(1, viewModel.todoItems.value.size)
        assertEquals("First TODO", viewModel.todoItems.value[0].text)
    }

    @Test
    fun `test clearError`() = runTest {
        viewModel.addTodoItem("Error")
        advanceUntilIdle()
        assertEquals("Failed to add TODO", viewModel.error.value)

        viewModel.clearError()
        assertNull(viewModel.error.value)
    }
}
