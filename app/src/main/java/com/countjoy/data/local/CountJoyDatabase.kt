package com.countjoy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.countjoy.data.local.converter.DateTimeConverter
import com.countjoy.data.local.dao.CountdownEventDao
import com.countjoy.data.local.entity.CountdownEventEntity

@Database(
    entities = [CountdownEventEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class CountJoyDatabase : RoomDatabase() {
    abstract fun countdownEventDao(): CountdownEventDao
}