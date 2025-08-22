package com.countjoy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countdown_events")
data class CountdownEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val targetDateTime: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)