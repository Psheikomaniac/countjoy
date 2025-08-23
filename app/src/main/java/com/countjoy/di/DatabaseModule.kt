package com.countjoy.di

import android.content.Context
import androidx.room.Room
import com.countjoy.data.local.CountJoyDatabase
import com.countjoy.data.local.dao.CountdownEventDao
import com.countjoy.data.local.migration.DatabaseMigrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideCountJoyDatabase(
        @ApplicationContext context: Context
    ): CountJoyDatabase {
        return Room.databaseBuilder(
            context,
            CountJoyDatabase::class.java,
            "countjoy_database"
        )
        .addMigrations(*DatabaseMigrations.getAllMigrations())
        .build()
    }
    
    @Provides
    @Singleton
    fun provideCountdownEventDao(
        database: CountJoyDatabase
    ): CountdownEventDao {
        return database.countdownEventDao()
    }
}