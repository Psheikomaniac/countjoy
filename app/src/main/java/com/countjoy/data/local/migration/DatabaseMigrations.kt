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
     * Get all migrations as an array
     */
    fun getAllMigrations(): Array<Migration> {
        return arrayOf(MIGRATION_1_2)
    }
}