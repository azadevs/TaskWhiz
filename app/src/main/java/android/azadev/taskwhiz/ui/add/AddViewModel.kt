package android.azadev.taskwhiz.ui.add

import android.azadev.taskwhiz.data.repository.TodoRepository
import android.azadev.taskwhiz.model.TodoData
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

class AddViewModel(private val repository: TodoRepository) : ViewModel() {

    fun upsertData(todoData: TodoData) {
        viewModelScope.launch {
            repository.upsertData(todoData)
        }
    }

    fun verifyDataFromUser(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

}