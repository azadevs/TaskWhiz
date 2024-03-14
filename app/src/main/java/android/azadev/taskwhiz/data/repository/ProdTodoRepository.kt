package android.azadev.taskwhiz.data.repository

import android.azadev.taskwhiz.data.dao.TodoDao
import android.azadev.taskwhiz.data.entity.toTodoData
import android.azadev.taskwhiz.model.TodoData
import android.azadev.taskwhiz.model.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

class ProdTodoRepository(
    private val dao: TodoDao
) : TodoRepository {

    override val getAllData: Flow<List<TodoData>>
        get() = dao.getAllData().map {
            withContext(Dispatchers.IO) {
                it.map { entity ->
                    entity.toTodoData()
                }
            }
        }

    override val sortByHighPriority: Flow<List<TodoData>>
        get() = dao.sortByHighPriority().map {
            withContext(Dispatchers.IO) {
                it.map { entity ->
                    entity.toTodoData()
                }
            }
        }

    override val sortByLowPriority: Flow<List<TodoData>>
        get() = dao.sortByLowPriority().map {
            withContext(Dispatchers.IO) {
                it.map { entity ->
                    entity.toTodoData()
                }
            }
        }

    override suspend fun upsertData(todoData: TodoData) {
        withContext(Dispatchers.IO) {
            dao.upsertData(todoData.toEntity())
        }
    }

    override suspend fun deleteData(todoData: TodoData) {
        withContext(Dispatchers.IO) {
            dao.deleteData(todoData.toEntity())
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    override fun searchDatabase(searchQuery: String): Flow<List<TodoData>> =
        dao.searchDatabase(searchQuery).map { list ->
            withContext(Dispatchers.IO) {
                list.map {
                    it.toTodoData()
                }
            }
        }

}