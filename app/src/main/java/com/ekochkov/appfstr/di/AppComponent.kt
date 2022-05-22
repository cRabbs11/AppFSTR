package com.ekochkov.appfstr.di

import com.ekochkov.appfstr.di.modules.DatabaseModule
import com.ekochkov.appfstr.di.modules.DomainModule
import com.ekochkov.appfstr.di.modules.RemoteModule
import com.ekochkov.appfstr.viewModel.AddMountainFragmentViewModel
import com.ekochkov.appfstr.viewModel.HomeFragmentViewModel
import com.ekochkov.appfstr.viewModel.LoginFragmentViewModel
import com.ekochkov.appfstr.viewModel.RegisterFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RemoteModule::class,
    DatabaseModule::class,
    DomainModule::class])

interface AppComponent {
    fun inject(loginFragmentViewModel: LoginFragmentViewModel)
    fun inject(registerFragmentViewModel: RegisterFragmentViewModel)
    fun inject(addMountainFragmentViewModel: AddMountainFragmentViewModel)
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
}