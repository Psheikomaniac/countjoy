package com.countjoy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.countjoy.data.local.converter.DateTimeConverter
import com.countjoy.data.local.dao.CountdownEventDao
import com.countjoy.data.local.dao.MilestoneDao
import com.countjoy.data.local.dao.RecurrenceRuleDao
import com.countjoy.data.local.entity.CountdownEventEntity
import com.countjoy.data.local.entity.MilestoneEntity
import com.countjoy.data.local.entity.RecurrenceRuleEntity

@Database(
    entities = [CountdownEventEntity::class, MilestoneEntity::class, RecurrenceRuleEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class CountJoyDatabase : RoomDatabase() {
    abstract fun countdownEventDao(): CountdownEventDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun recurrenceRuleDao(): RecurrenceRuleDao
}