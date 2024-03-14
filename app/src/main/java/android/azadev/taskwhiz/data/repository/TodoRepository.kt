package android.azadev.taskwhiz.data.repository

import android.azadev.taskwhiz.model.TodoData
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

interface TodoRepository {

    val getAllData: Flow<List<TodoData>>
    val sortByHighPriority: Flow<List<TodoData>>
    val sortByLowPriority: Flow<List<TodoData>>

    suspend fun upsertData(todoData: TodoData)

    suspend fun deleteData(todoData: TodoData)

    fun searchDatabase(searchQuery: String): Flow<List<TodoData>>

    suspend fun deleteAll()


}