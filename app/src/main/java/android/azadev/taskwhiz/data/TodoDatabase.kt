package android.azadev.taskwhiz.data

import android.azadev.taskwhiz.data.dao.TodoDao
import android.azadev.taskwhiz.data.entity.TodoEntity
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun dao(): TodoDao

}