package android.azadev.taskwhiz.utils

import android.azadev.taskwhiz.model.Priority

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/14/2024
 */

object Constants {

    fun parsePriorityWhenSaveToDatabase(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.HIGH
        }
    }

    fun parsePriorityWhenMapToTodoData(priority: String): Priority {
        return when (priority) {
            "HIGH" -> Priority.HIGH
            "MEDIUM" -> Priority.MEDIUM
            "LOW" -> Priority.LOW
            else -> Priority.HIGH
        }
    }
}