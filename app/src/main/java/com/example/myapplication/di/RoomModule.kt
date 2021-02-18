package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.db.PrepopulateDataBase
import com.example.myapplication.data.db.TaskDataBase
import com.example.myapplication.data.db.migration.Migration1To2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TaskDataBase::class.java, "myBd.db")
            .addCallback(PrepopulateDataBase())
            .addMigrations(Migration1To2())
            .build()

    @Singleton
    @Provides
    fun providePersonStore(db: TaskDataBase) = db.personStore()

    @Singleton
    @Provides
    fun provideStaffStore(db: TaskDataBase) = db.staffStore()

    @Singleton
    @Provides
    fun provideStaffAndPersonsStore(db: TaskDataBase) = db.staffAndPersonsStore()

    @Singleton
    @Provides
    fun providePersonStaffAndStore(db: TaskDataBase) = db.personAndStaffStore()

    @ApplicationScope
    @Singleton
    @Provides
    fun provideAppScope() = CoroutineScope(SupervisorJob())

}
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope