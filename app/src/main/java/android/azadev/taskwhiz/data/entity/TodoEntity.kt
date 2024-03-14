package android.azadev.taskwhiz.data.entity

import android.azadev.taskwhiz.model.TodoData
import android.azadev.taskwhiz.utils.Constants.parsePriorityWhenMapToTodoData
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val priority: String,
    val title: String,
    val description: String
)

fun TodoEntity.toTodoData(): TodoData {
    return TodoData(
        id = this.id,
        title = this.title,
        description = this.description,
        priority = parsePriorityWhenMapToTodoData(this.priority)
    )
}
