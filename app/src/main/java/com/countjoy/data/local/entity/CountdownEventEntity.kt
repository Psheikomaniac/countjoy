package com.countjoy.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "countdown_events",
    indices = [
        Index(value = ["category"]),
        Index(value = ["target_date_time"]),
        Index(value = ["is_active"])
    ]
)
data class CountdownEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "description")
    val description: String? = null,
    
    @ColumnInfo(name = "category")
    val category: String = "General",
    
    @ColumnInfo(name = "target_date_time")
    val targetDateTime: Long,
    
    @ColumnInfo(name = "reminder_enabled")
    val reminderEnabled: Boolean = false,
    
    @ColumnInfo(name = "reminder_time")
    val reminderTime: Long? = null,
    
    @ColumnInfo(name = "color")
    val color: Int? = null,
    
    @ColumnInfo(name = "icon")
    val icon: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    @ColumnInfo(name = "priority")
    val priority: Int = 0
)