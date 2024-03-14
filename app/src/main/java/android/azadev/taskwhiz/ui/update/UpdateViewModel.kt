package android.azadev.taskwhiz.ui.update

import android.azadev.taskwhiz.data.repository.TodoRepository
import android.azadev.taskwhiz.model.TodoData
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/14/2024
 */

class UpdateViewModel(private val repository: TodoRepository) : ViewModel() {

    fun updateTodoData(todoData: TodoData) {
        viewModelScope.launch {
            repository.upsertData(todoData)
        }
    }

    fun deleteTodoData(todoData: TodoData) {
        viewModelScope.launch {
            repository.deleteData(todoData)
        }
    }

    fun verifyDataFromUser(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

}