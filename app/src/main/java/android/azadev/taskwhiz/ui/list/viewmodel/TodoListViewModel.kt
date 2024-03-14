package android.azadev.taskwhiz.ui.list.viewmodel

import android.azadev.taskwhiz.data.repository.TodoRepository
import android.azadev.taskwhiz.model.TodoData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/14/2024
 */

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    val getAllTodo = repository.getAllData
    val sortByHighPriority = repository.sortByHighPriority
    val sortByLowPriority = repository.sortByLowPriority

    private val _searchResult = MutableStateFlow<List<TodoData>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    private val _isEmptyDatabase = MutableStateFlow(true)
    val isEmptyDatabase: StateFlow<Boolean> = _isEmptyDatabase.asStateFlow()

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun checkIsEmptyDatabase() {
        viewModelScope.launch {
            getAllTodo.collect {
                _isEmptyDatabase.value = it.isEmpty()
            }
        }
    }

    fun deleteTodoData(todoData: TodoData) {
        viewModelScope.launch {
            repository.deleteData(todoData)
        }
    }

    fun upsertTodoData(todoData: TodoData) {
        viewModelScope.launch {
            repository.upsertData(todoData)
        }
    }

    fun searchDatabase(searchQuery: String) {
        viewModelScope.launch {
            repository.searchDatabase(searchQuery).collect {
                _searchResult.value = it
            }
        }
    }
}