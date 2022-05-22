package com.ekochkov.appfstr

import android.app.Application
import com.ekochkov.appfstr.di.AppComponent
import com.ekochkov.appfstr.di.DaggerAppComponent
import com.ekochkov.appfstr.di.modules.DatabaseModule
import com.ekochkov.appfstr.di.modules.DomainModule
import com.ekochkov.appfstr.di.modules.RemoteModule

class App: Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        dagger = DaggerAppComponent.builder()
                .remoteModule(RemoteModule())
                .databaseModule(DatabaseModule())
                .domainModule(DomainModule(this))
                .build()
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}