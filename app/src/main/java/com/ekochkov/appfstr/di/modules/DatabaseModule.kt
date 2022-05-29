package com.ekochkov.appfstr.di.modules

import android.content.Context
import androidx.room.Room
import com.ekochkov.appfstr.data.AppDatabase
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.data.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideUserDao(context: Context): UserDao {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.MAIN_DB_NAME)
                .build()
                .userDao()
    }

    @Singleton
    @Provides
    fun provideRepository(userDao: UserDao): Repository = Repository(userDao)

}