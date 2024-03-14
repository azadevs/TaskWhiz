package android.azadev.taskwhiz.model

import android.azadev.taskwhiz.data.entity.TodoEntity
import java.io.Serializable

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/14/2024
 */

data class TodoData(
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
) : Serializable

fun TodoData.toEntity(): TodoEntity {
    return TodoEntity(
        id = this.id,
        priority = this.priority.name,
        title = title,
        description = description
    )
}
