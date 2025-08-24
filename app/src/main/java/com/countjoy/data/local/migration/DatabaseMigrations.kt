package com.countjoy.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database migrations for CountJoy database
 */
object DatabaseMigrations {
    
    /**
     * Migration from version 1 to version 2
     * Adds new fields: category, reminder_enabled, reminder_time, color, icon, updated_at, priority
     * Adds indices for better query performance
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create a new table with all the new columns
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS countdown_events_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    title TEXT NOT NULL,
                    description TEXT,
                    category TEXT NOT NULL DEFAULT 'General',
                    target_date_time INTEGER NOT NULL,
                    reminder_enabled INTEGER NOT NULL DEFAULT 0,
                    reminder_time INTEGER,
                    color INTEGER,
                    icon TEXT,
                    created_at INTEGER NOT NULL,
                    updated_at INTEGER NOT NULL,
                    is_active INTEGER NOT NULL DEFAULT 1,
                    priority INTEGER NOT NULL DEFAULT 0
                )
            """.trimIndent())
            
            // Copy data from old table to new table
            database.execSQL("""
                INSERT INTO countdown_events_new (
                    id, title, description, category, target_date_time, 
                    reminder_enabled, reminder_time, color, icon,
                    created_at, updated_at, is_active, priority
                )
                SELECT 
                    id, 
                    title, 
                    description,
                    'General' as category,
                    targetDateTime as target_date_time,
                    0 as reminder_enabled,
                    NULL as reminder_time,
                    NULL as color,
                    NULL as icon,
                    createdAt as created_at,
                    createdAt as updated_at,
                    isActive as is_active,
                    0 as priority
                FROM countdown_events
            """.trimIndent())
            
            // Drop the old table
            database.execSQL("DROP TABLE countdown_events")
            
            // Rename the new table to the original name
            database.execSQL("ALTER TABLE countdown_events_new RENAME TO countdown_events")
            
            // Create indices for better performance
            database.execSQL("CREATE INDEX IF NOT EXISTS index_countdown_events_category ON countdown_events (category)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_countdown_events_target_date_time ON countdown_events (target_date_time)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_countdown_events_is_active ON countdown_events (is_active)")
        }
    }
    
    /**
     * Migration from version 2 to version 3
     * Adds milestones table for tracking countdown milestones
     */
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the milestones table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS milestones (
                    id TEXT PRIMARY KEY NOT NULL,
                    eventId TEXT NOT NULL,
                    type TEXT NOT NULL,
                    value REAL NOT NULL,
                    title TEXT NOT NULL,
                    message TEXT NOT NULL,
                    isNotificationEnabled INTEGER NOT NULL,
                    isAchieved INTEGER NOT NULL,
                    achievedAt INTEGER,
                    celebrationEffect TEXT NOT NULL,
                    FOREIGN KEY(eventId) REFERENCES countdown_events(id) ON DELETE CASCADE
                )
            """.trimIndent())
            
            // Create index for better performance
            database.execSQL("CREATE INDEX IF NOT EXISTS index_milestones_eventId ON milestones (eventId)")
        }
    }
    
    /**
     * Migration from version 3 to version 4
     * Adds recurrence_rules table for recurring events support
     */
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the recurrence_rules table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS recurrence_rules (
                    id TEXT PRIMARY KEY NOT NULL,
                    eventId TEXT NOT NULL,
                    pattern TEXT NOT NULL,
                    intervalValue INTEGER NOT NULL,
                    daysOfWeek TEXT,
                    dayOfMonth INTEGER,
                    weekOfMonth INTEGER,
                    monthOfYear INTEGER,
                    endType TEXT NOT NULL,
                    endDate INTEGER,
                    occurrenceCount INTEGER,
                    exceptions TEXT,
                    skipWeekends INTEGER NOT NULL,
                    skipHolidays INTEGER NOT NULL,
                    lastOccurrenceDate INTEGER,
                    nextOccurrenceDate INTEGER,
                    FOREIGN KEY(eventId) REFERENCES countdown_events(id) ON DELETE CASCADE
                )
            """.trimIndent())
            
            // Create index for better performance
            database.execSQL("CREATE INDEX IF NOT EXISTS index_recurrence_rules_eventId ON recurrence_rules (eventId)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_recurrence_rules_nextOccurrenceDate ON recurrence_rules (nextOccurrenceDate)")
        }
    }
    
    /**
     * Get all migrations as an array
     */
    fun getAllMigrations(): Array<Migration> {
        return arrayOf(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
    }
}