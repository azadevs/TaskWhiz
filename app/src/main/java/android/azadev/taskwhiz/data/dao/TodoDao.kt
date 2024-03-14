package android.azadev.taskwhiz.data.dao

import android.azadev.taskwhiz.data.entity.TodoEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): Flow<List<TodoEntity>>

    @Upsert
    suspend fun upsertData(todoEntity: TodoEntity)

    @Delete
    suspend fun deleteData(todoEntity: TodoEntity)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority():Flow<List<TodoEntity>>

}