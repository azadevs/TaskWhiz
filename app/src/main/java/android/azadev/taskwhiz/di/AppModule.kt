package android.azadev.taskwhiz.di

import android.azadev.taskwhiz.data.TodoDatabase
import android.azadev.taskwhiz.data.repository.ProdTodoRepository
import android.azadev.taskwhiz.data.repository.TodoRepository
import android.azadev.taskwhiz.ui.add.AddViewModel
import android.azadev.taskwhiz.ui.list.viewmodel.TodoListViewModel
import android.azadev.taskwhiz.ui.update.UpdateViewModel
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */


val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            TodoDatabase::class.java,
            "todo-database"
        ).build()
    }

    single {
        get<TodoDatabase>().dao()
    }

    single<TodoRepository> {
        ProdTodoRepository(get())
    }

    viewModel {
        AddViewModel(get())
    }

    viewModel {
        TodoListViewModel(get())
    }

    viewModel {
        UpdateViewModel(get())
    }

}