package com.ekochkov.appfstr.di.modules

import android.content.Context
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.domain.Interactor
import com.ekochkov.appfstr.utils.PerevalRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideInteractor(
        repository: Repository,
        retrofit: PerevalRetrofit) : Interactor = Interactor(repository, retrofit)
}