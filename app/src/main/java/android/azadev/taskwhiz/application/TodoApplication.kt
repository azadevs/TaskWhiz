package android.azadev.taskwhiz.application

import android.app.Application
import android.azadev.taskwhiz.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TodoApplication)
            modules(appModule)
        }
    }
}