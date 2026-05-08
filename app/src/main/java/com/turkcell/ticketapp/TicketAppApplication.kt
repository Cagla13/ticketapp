package com.turkcell.ticketapp

import android.app.Application
import com.turkcell.data.di.networkModule // Data modülünden gelen
import com.turkcell.data.di.repositoryModule // Data modülünden gelen
import com.turkcell.ticketapp.di.viewModelModule // App modülünden gelen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TicketAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TicketAppApplication)
            // Tüm parçaları burada birleştiriyoruz
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}