package com.ammar.mybottomnavigation

import android.app.Application
import com.ammar.core.di.databaseModule
import com.ammar.core.di.networkModule
import com.ammar.core.di.preferenceModule
import com.ammar.core.di.repositoryModule
import com.ammar.mybottomnavigation.di.useCaseModule
import com.ammar.mybottomnavigation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    preferenceModule
                )
            )
        }
    }
}