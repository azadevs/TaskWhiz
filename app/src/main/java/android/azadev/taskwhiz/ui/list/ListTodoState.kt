package android.azadev.taskwhiz.ui.list

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/14/2024
 */

sealed interface ListTodoState<out T> {

    data class Search<T>(val data: T) : ListTodoState<T>

    data object IsEmpty : ListTodoState<Nothing>

    data class Success<T>(val data: T) : ListTodoState<T>

    data object Loading : ListTodoState<Nothing>

}